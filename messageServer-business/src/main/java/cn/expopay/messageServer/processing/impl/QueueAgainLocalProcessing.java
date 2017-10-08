package cn.expopay.messageServer.processing.impl;

import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.Interface.localprocess.AbstractQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.model.back.BackMessage;
import cn.expopay.messageServer.model.queue.BackQueueMessage;
import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.model.returninfo.ReturnSender;
import cn.expopay.messageServer.model.returninfo.ReturnT;
import cn.expopay.messageServer.model.rsa.RsaConfigModel;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import cn.expopay.messageServer.util.configuration.Initializationconfig.ActiveMQDelayConfig;
import cn.expopay.messageServer.util.dateutil.DateUtil;
import cn.expopay.messageServer.util.encryption.RsaParameterValidation;
import cn.expopay.messageServer.util.http.HttpManagerSendClient;
import cn.expopay.messageServer.util.resultsvalidation.ResultsCodeValidation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "queueAgainLocalProcessing")
public class QueueAgainLocalProcessing extends AbstractQueueLocalProcessing {

    private final Logger logger = LoggerFactory.getLogger(QueueAgainLocalProcessing.class);

    @Autowired
    private IMessageService messageService;

    @Autowired
    private HttpManagerSendClient httpManagerSendClient;

    @Override
    public void queueLocalProcessing(Object obj, IProducerService producerServiceSend, IProducerService producerServiceBack) {
        boolean serviceAgain = false;
        long delayTime = 0;
        BackQueueMessage backQueueMessage = null;
        boolean serviceBack = false;

        QueueMessage qm = (QueueMessage)obj;
        int totalNum = qm.getSendMessage().getTotalNum();
        int currentNum = qm.getCurrentNum();

        logger.info("当前正在执行第：" + currentNum + "次HTTP调用");

        ReturnT<String> callbackResult = httpManagerSendClient.sendRequest(qm.getSendMessage().getReceptionUrl(), qm.getSendMessage());
        logger.info("QueueListenerAdapterAgain ReturnT<String> callbackResult is " + callbackResult);
        logger.info("*************************************************************************");

        QueueMessageStore queueMessageStore = new QueueMessageStore();
        queueMessageStore.setReturnAgain(callbackResult);

        if(ResultsCodeValidation.sendRsultsCodeValidation(callbackResult.getCode())){
            queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateThree);
            //如果第一次重试就请求成功，则将返回结果放入到回复队列中。
        } else if (callbackResult == null || callbackResult.getCode() != IMessageContent.HttpCodeSucess){
            if(currentNum < totalNum) {
                int tempNum = currentNum + 1;
                delayTime = ActiveMQDelayConfig.delayConfig.get(String.valueOf(tempNum));
                qm.setCurrentNum(tempNum);
                queueMessageStore.setDelayValue(delayTime);
                serviceAgain = true;
            }else{
                logger.info("消息发送当前重试次数为" + currentNum + "，超过" + totalNum + "了！");
                queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateThree);
            }
        }else{
            queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateTwo);
        }

        if(!serviceAgain){
            if(StringUtils.isNotBlank(qm.getSendMessage().getBackUrl())) {
                queueMessageStore.setBackTime(DateUtil.formatNoCharDate(new Date()));

                String keyVersion = qm.getSendMessage().getKeyVersion().toLowerCase();
                try {
                    RsaConfigModel rsaConfigModel = RsaParameterValidation.getRsaProperties(keyVersion);
                    if(rsaConfigModel == null){
                        throw new RuntimeException(IMessageContent.SendMessageKeyVersionIsErrorStr);
                    }

                    BackMessage backMessage = new BackMessage();
                    backMessage.setSendId(qm.getSendMessage().getSendId());
                    backMessage.setBackTime(DateUtil.formatNoCharDate(new Date()));

                    backMessage.setCode(callbackResult.getCode());
                    backMessage.setMsg(callbackResult.getMsg());
                    backMessage.setContent(callbackResult.getContent());

                    String signBackMq = RsaParameterValidation.signParamter(backMessage, rsaConfigModel.getPrivateKey());
                    if (StringUtils.isNotBlank(signBackMq)) {
                        backMessage.setSignBackMq(signBackMq);

                        backQueueMessage = new BackQueueMessage();
                        backQueueMessage.setBackMessage(backMessage);
                        backQueueMessage.setBackUrl(qm.getSendMessage().getBackUrl());

                        backQueueMessage.setKeyVersion(keyVersion);

                        backQueueMessage.setTotalBackNum(qm.getSendMessage().getTotalBackNum());
                        logger.info("QueueListenerAdapterAgain are back is " + backQueueMessage);
                        serviceBack = true;
                    } else {
                        logger.error("QueueListenerAdapterAgain are back is error " + IMessageContent.BackMessageSignFail);
                        queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                        ReturnSender returnSender = new ReturnSender();
                        returnSender.setCode(IMessageContent.HttpCodeFail);
                        returnSender.setMsg(IMessageContent.BackMessageSignFail);
                        queueMessageStore.setReturnBack(returnSender);
                        queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                    }
                } catch (Exception e) {
                    logger.error("QueueListenerAdapterAgain " + IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                    ReturnSender rs = new ReturnSender();
                    rs.setCode(IMessageContent.HttpCodeFail);
                    rs.setMsg(IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                    queueMessageStore.setReturnBack(rs);
                    queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                }
            }else{
                queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
            }
        }

        queueMessageStore.setQueueMessage(qm);
        queueMessageStore.setUpdateTime(DateUtil.formatNoCharDate(new Date()));
//        logger.info("QueueListenerAdapterAgain are queueMessageStore is " + queueMessageStore);
        messageService.updateMessageInfo(queueMessageStore);

        if(serviceAgain){
            producerServiceSend.publishConvertPostProcessor("queueAgain" ,qm, delayTime);
        }else if(serviceBack){
            producerServiceBack.sendMessage("queueBack", backQueueMessage);
        }
    }
}
