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
import com.codahale.metrics.Meter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "queueOneLocalProcessing")
public class QueueOneLocalProcessing extends AbstractQueueLocalProcessing {

    private final Logger logger = LoggerFactory.getLogger(QueueOneLocalProcessing.class);

    @Autowired
    private IMessageService messageService;

    @Autowired
    private HttpManagerSendClient httpManagerSendClient;

    @Autowired
    Meter consumerOne;

    @Override
    public void queueLocalProcessing(Object obj, IProducerService producerServiceSend, IProducerService producerServiceBack) {
        boolean serviceAgain = false;
        long delayTime = 0;
        BackQueueMessage backQueueMessage = null;
        boolean serviceBack = false;

        QueueMessage qm = (QueueMessage) obj;
        QueueMessageStore queueMessageStore = new QueueMessageStore();

        ReturnT<String> callbackResult = httpManagerSendClient.sendRequest(qm.getSendMessage().getReceptionUrl(), qm.getSendMessage());
        logger.info("QueueOneLocalProcessing ReturnT<String> callbackResult is " + callbackResult);

        queueMessageStore.setReturnOne(callbackResult);
        queueMessageStore.setCurrentStage(IMessageContent.GeneralStateOne);
        queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateOne);

        if (ResultsCodeValidation.sendRsultsCodeValidation(callbackResult.getCode())) {
            queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateThree);
        } else if (callbackResult == null || callbackResult.getCode() != IMessageContent.HttpCodeSucess) {
            delayTime = ActiveMQDelayConfig.delayConfig.get(String.valueOf(IMessageContent.GeneralStateOne));
            logger.info("QueueOneLocalProcessing 子线程第一次延迟队列执行：");
            qm.setCurrentNum(IMessageContent.GeneralStateOne);
            qm.setAgainTime(DateUtil.formatNowDate());
            queueMessageStore.setDelayValue(delayTime);
            queueMessageStore.setCurrentStage(IMessageContent.GeneralStateTwo);
            serviceAgain = true;
        } else {
            queueMessageStore.setProcessEndSend(IMessageContent.GeneralStateTwo);
            consumerOne.mark();
        }

        if(!serviceAgain){
            if (StringUtils.isNotBlank(qm.getSendMessage().getBackUrl())) {
                queueMessageStore.setBackTime(DateUtil.formatNowDate());

                String keyVersion = qm.getSendMessage().getKeyVersion().toLowerCase();
                try {
                    RsaConfigModel rsaConfigModel = RsaParameterValidation.getRsaProperties(IMessageContent.MqRsaId);
                    if(rsaConfigModel == null){
                        throw new RuntimeException(IMessageContent.SendMessageKeyVersionIsErrorStr);
                    }

                    BackMessage backMessage = new BackMessage();
                    backMessage.setSendId(qm.getSendMessage().getSendId());
                    backMessage.setBackTime(DateUtil.formatNowDate());

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

                        serviceBack = true;
                    } else {
                        logger.error("QueueOneLocalProcessing are back is error " + IMessageContent.BackMessageSignFail);
                        queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                        ReturnSender returnSender = new ReturnSender();
                        returnSender.setCode(IMessageContent.HttpCodeFail);
                        returnSender.setMsg(IMessageContent.BackMessageSignFail);
                        queueMessageStore.setReturnBack(returnSender);
                        queueMessageStore.setEndTime(DateUtil.formatNowDate());
                    }
                } catch (Exception e) {
                    logger.error("QueueOneLocalProcessing " + IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                    ReturnSender rs = new ReturnSender();
                    rs.setCode(IMessageContent.HttpCodeFail);
                    rs.setMsg(IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                    queueMessageStore.setReturnBack(rs);
                    queueMessageStore.setEndTime(DateUtil.formatNowDate());
                }
            }else{
                queueMessageStore.setEndTime(DateUtil.formatNowDate());
            }
        }

        queueMessageStore.setQueueMessage(qm);
        queueMessageStore.setUpdateTime(DateUtil.formatNowDate());
        messageService.updateMessageInfo(queueMessageStore); // 改为更新操作

        if (serviceAgain) {
            producerServiceSend.publishConvertPostProcessor(IMessageContent.QueueAgain, qm, delayTime);
        } else if (serviceBack) {
            producerServiceBack.sendMessage(IMessageContent.QueueBack, backQueueMessage);
        }
    }
}
