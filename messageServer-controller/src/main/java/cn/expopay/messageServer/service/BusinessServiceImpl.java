package cn.expopay.messageServer.service;

import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.model.returninfo.ReturnMq;
import cn.expopay.messageServer.model.send.SendMessage;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import cn.expopay.messageServer.util.configuration.metric.MetricConfig;
import cn.expopay.messageServer.util.dateutil.DateUtil;
import cn.expopay.messageServer.util.encryption.RsaParameterValidation;
import com.codahale.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "businessService")
public class BusinessServiceImpl implements IBusinessService{

    private Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Autowired
    IProducerService producerServiceSend;

    @Autowired
    IMessageService messageService;

    @Autowired
    Meter producers;

    @Autowired
    Counter requestFailCount;

    @Override
    public ReturnMq sendMessageOne(SendMessage sendMessage) {
//        MetricConfig.getMetricConfig().getMeter().mark();
//        Timer.Context context = null;
        ReturnMq rmq = null;
        boolean sendMessageStatus = false;
        QueueMessage qm = null;
        try {
//            context = MetricConfig.getMetricConfig().getTimer().time();
            if (sendMessage != null) {
                try {
                    rmq = RsaParameterValidation.checkParamter(sendMessage);
                    if (rmq.getCode() == IMessageContent.HttpCodeSucess) {
                        qm = new QueueMessage();
                        qm.setSendMessage(sendMessage);
                        sendMessageStatus = true;
//                    logger.info("消息进队列了！！！！！！");
                    }
                } catch (Exception e) {
                    logger.error(rmq.getMsg() + "。" + e.getMessage());
                    rmq.setCode(IMessageContent.HttpCodeFail);
                    rmq.setMsg(rmq.getMsg() + "。" + e.getMessage());
                }
                // 返回加签
                if (rmq.getCode() != IMessageContent.SendMessageKeyVersionIsError &&
                        rmq.getCode() != IMessageContent.SendMessageSignatureFail &&
                        rmq.getCode() != IMessageContent.SendMessageKeyIsNull) {
                    RsaParameterValidation.returnSignParamterLast(rmq, sendMessage);
                }
            } else {
                rmq = new ReturnMq(IMessageContent.HttpCodeFail, "请求消息为Null，请检查核对");
            }
            // 请求消息入库
            insertMessage(rmq, sendMessage);

            if (sendMessageStatus) {
                producerServiceSend.sendMessage("queueOne", qm);
                producers.mark();
            }else{
                requestFailCount.inc();
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
//            context.stop();
        }
        return rmq;
    }

    private void insertMessage(ReturnMq rmq, SendMessage sendMessage){

        QueueMessageStore queueMessageStore = new QueueMessageStore();
        QueueMessage queueMessage = new QueueMessage();

        if(sendMessage != null) {
            queueMessage.setSendMessage(sendMessage);
            queueMessageStore.setQueueMessage(queueMessage);

            if (rmq.getCode() != IMessageContent.HttpCodeSucess) {
                queueMessageStore.setUpdateTime(DateUtil.formatNoCharDate(new Date()));
                queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
            }
        }else{
            queueMessageStore.setQueueMessage(queueMessage);
            queueMessageStore.setUpdateTime(DateUtil.formatNoCharDate(new Date()));
            queueMessageStore.setEndTime(DateUtil.formatNoCharDate(new Date()));
        }
        queueMessageStore.setReturnRequest(rmq);
        messageService.insertMessageInfo(queueMessageStore);
    }

}
