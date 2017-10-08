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

@Service(value = "queueBackLocalProcessing")
public class QueueBackLocalProcessing extends AbstractQueueLocalProcessing {

    private final Logger logger = LoggerFactory.getLogger(QueueBackLocalProcessing.class);

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

        queueMessageStore.setTempId(bqm.getBackMessage().getSendId());
        queueMessageStore.setCurrentStage(IMessageContent.GeneralStateThree);
        queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateOne);

//			String keyVersion = BackUrlMapKeyConfig.url2Key.get(bqm.getBackUrl());
        String keyVersion = bqm.getKeyVersion();
        if(StringUtils.isNotBlank(keyVersion)){
            try {
                RsaConfigModel rsaConfigModel = RsaParameterValidation.getRsaProperties(keyVersion);
                if(rsaConfigModel == null){
                    throw new RuntimeException(IMessageContent.SendMessageKeyVersionIsErrorStr);
                }

                ObjectMapper om = new ObjectMapper();
                ReturnSender callbackResult = httpManagerBackClient.sendRequest(bqm.getBackUrl(), om.writeValueAsString(bqm.getBackMessage()), rsaConfigModel.getPublicKey());
                logger.info("QueueBackLocalProcessing ReturnSender callbackResult is " + callbackResult);

                queueMessageStore.setReturnBack(callbackResult);

                if(ResultsCodeValidation.backRsultsCodeValidation(callbackResult.getCode())){
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                    queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                } else if (callbackResult == null || callbackResult.getCode() != IMessageContent.HttpCodeSucess) {
                    delayTime = ActiveMQDelayConfig.delayBackConfig.get(String.valueOf(IMessageContent.GeneralStateOne));
                    logger.info("回復消息：第一次延迟队列执行：");
                    bqm.setCurrentBackNum(IMessageContent.GeneralStateOne);
                    bqm.setBackAgainTime(DateUtil.formatNoCharDate(new Date()));

                    queueMessageStore.setCurrentBackNum(IMessageContent.GeneralStateOne);
                    queueMessageStore.setBackAgainTime(DateUtil.formatNoCharDate(new Date()));
                    queueMessageStore.setDelayBackValue(delayTime);
                    queueMessageStore.setCurrentStage(IMessageContent.GeneralStateFour);
                    backAgain = true;
                } else {
                    queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateTwo);
                    queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
                    // 結束
                    logger.info("QueueListenerAdapterBack 整個流程結束");
                }
            }catch (Exception e){
                logger.error("QueueListenerAdapterBack " + IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
                ReturnSender rs = new ReturnSender();
                rs.setCode(IMessageContent.HttpCodeFail);
                rs.setMsg(IMessageContent.BackMessageKeyVersion + keyVersion + " and Exception is " + e.getMessage());
                queueMessageStore.setReturnBack(rs);
                queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
            }
        }else{
            logger.error("QueueListenerAdapterBack " + IMessageContent.BackMessageKeyVersion + "null or is ''");
            queueMessageStore.setProcessEndBack(IMessageContent.GeneralStateThree);
            ReturnSender rs = new ReturnSender();
            rs.setCode(IMessageContent.HttpCodeFail);
            rs.setMsg(IMessageContent.BackMessageKeyVersion + "null or is ''");
            queueMessageStore.setReturnBack(rs);
            queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
        }

        queueMessageStore.setUpdateTime(DateUtil.formatNoCharDate(new Date()));
//        logger.info("QueueListenerAdapterBack are queueMessageStore is " + queueMessageStore);
        messageService.updateMessageInfo(queueMessageStore);

        if(backAgain){
            producerServiceBack.publishConvertPostProcessor("queueBackAgain" ,bqm, delayTime);
        }
    }
}
