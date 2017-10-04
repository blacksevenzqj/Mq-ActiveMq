package cn.expopay.messageServer.activemq.adapter.impl;

import cn.expopay.messageServer.Interface.localprocess.IQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.activemq.adapter.IQueueListener;
import cn.expopay.messageServer.activemq.adapter.thread.QueueListenerThreadBack;
import cn.expopay.messageServer.model.queue.BackQueueMessage;
import cn.expopay.messageServer.util.thread.ThreadControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component(value = "queueListenerAdapterBackAgain")
public class QueueListenerAdapterBackAgain implements IQueueListener {

    private final Logger logger = LoggerFactory.getLogger(QueueListenerAdapterBackAgain.class);

    @Autowired
    private IQueueLocalProcessing queueBackAgainLocalProcessing;

    @Autowired
    private IProducerService producerServiceBack;

    @Async("threadTaskControlContainer")
    public void receiveMessage(Object message) {
        logger.info("QueueListenerAdapterBackAgain 接收到一个消息：\t" + message);
        try{
            if(message != null && message instanceof BackQueueMessage) {
                QueueListenerThreadBack thread = new QueueListenerThreadBack(message, queueBackAgainLocalProcessing, producerServiceBack);
                ThreadControl.getbalancing().execute(thread);
            }
        }catch (Exception e){
            logger.error("QueueListenerAdapterBackAgain run error is " + e.getMessage());
        }
    }
}
