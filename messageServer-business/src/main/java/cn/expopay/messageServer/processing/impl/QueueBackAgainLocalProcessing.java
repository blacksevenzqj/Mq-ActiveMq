package cn.expopay.messageServer.processing.impl;

import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.Interface.localprocess.AbstractQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.model.queue.BackQueueMessage;
import cn.expopay.messageServer.model.returninfo.ReturnSender;
import cn.expopay.messageServer.model.rsa.RsaConfigModel;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import cn.expopay.messageServer.util.configuration.Initializationconfig.ActiveMQDelayConfig;
import cn.expopay.messageServer.util.dateutil.DateUtil;
import cn.expopay.messageServer.util.encryption.RsaParameterValidation;
import cn.expopay.messageServer.util.http.HttpManagerBackClient;
import cn.expopay.messageServer.util.resultsvalidation.ResultsCodeValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "queueBackAgainLocalProcessing")
public class QueueBackAgainLocalProcessing extends AbstractQueueLocalProcessing {

    private final Logger logger = LoggerFactory.getLogger(QueueBackAgainLocalProcessing.class);

    @Autowired
    private IMessageService messageService;

    @Autowired
    private HttpManagerBackClient httpManagerBackClient;

    @Override
    public void queueLocalProcessing(Object obj, IProducerService producerServiceBack) {
        boolean backAgain = false;
        long delayTime = 0;

        QueueMessageStore queueMessageStore = new QueueMessageStore();
        BackQueueMessage bqm = (BackQueueMessage)obj;
        int totalBackNum = bqm.getTotalBackNum();
        int currentBackNum = bqm.getCurrentBackNum();

        queueMessageStore.setTempId(bqm.getBackMessage().getSendId());

        logger.info("消息回复当前正在执行第：" + currentBackNum + "次HTTP调用");

//            String keyVersion = BackUrlMapKeyConfig.url2Key.get(bqm.getBackUrl());
        String keyVersion = bqm.getKeyVersion();
        if(StringUtils.isNotBlank(keyVersion)) {
            try {
                RsaConfigModel rsaConfigModel = RsaParameterValidation.getRsaProperties(keyVersion);
                if(rsaConfigModel == null){
                    throw new RuntimeException(IMessageContent.SendMessageKeyVersionIsError);
                }

                ObjectMapper om = new ObjectMapper();
                ReturnSender callbackResult = httpManagerBackClient.sendRequest(bqm.getBackUrl(), om.writeValueAsString(bqm.getBackMessage()), rsaConfigModel.getPublicKey());
                logger.info("QueueBackAgainLocalProcessing ReturnSender callbackResult is " + callbackResult);
                logger.info("*************************************************************************");

                queueMessageStore.setReturnBackAgain(callbackResult);

                if(ResultsCodeValidation.backRsultsCodeValidation(callbackResult.getCode())){
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                    queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                //如果第一次重试就请求成功，则将返回结果放入到回复队列中。
                } else if (callbackResult == null || callbackResult.getCode() != IMessageContent.HttpCodeSucess){
                    if(currentBackNum < totalBackNum) {
                        int tempNum = currentBackNum + 1;
                        delayTime = ActiveMQDelayConfig.delayBackConfig.get(String.valueOf(tempNum));
                        bqm.setCurrentBackNum(tempNum);

                        queueMessageStore.setCurrentBackNum(tempNum);
                        queueMessageStore.setDelayBackValue(delayTime);
                        backAgain = true;
                    }else{
                        queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                        queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                        logger.info("消息回复当前重试次数为" + totalBackNum + "，超过" + totalBackNum + "了啊！！！");
                    }
                }else {
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateTwo);
                    queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                    logger.info("QueueListenerAdapterBackAgain 整個流程結束");
                }
            }catch (Exception e){
                logger.error("QueueListenerAdapterBackAgain " + IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                ReturnSender rs = new ReturnSender();
                rs.setCode(IMessageContent.HttpCodeFail);
                rs.setMsg(IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                queueMessageStore.setReturnBackAgain(rs);
                queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
            }
        }else{
            logger.error("QueueListenerAdapterBackAgain " + IMessageContent.BackMessageKeyVersion + "null or is ''");
            queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
            ReturnSender rs = new ReturnSender();
            rs.setCode(IMessageContent.HttpCodeFail);
            rs.setMsg(IMessageContent.BackMessageKeyVersion + "null or is ''");
            queueMessageStore.setReturnBackAgain(rs);
            queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
        }

        queueMessageStore.setUpdateTime(DateUtil.formatNoCharDate(new Date()));
//            logger.info("QueueListenerAdapterBackAgain are queueMessageStore is " + queueMessageStore);
        messageService.updateMessageInfo(queueMessageStore);

        if(backAgain){
            producerServiceBack.publishConvertPostProcessor("queueBackAgain", bqm, delayTime);
        }
    }
}
