package cn.expopay.messageServer.activemq.adapter.thread;

import cn.expopay.messageServer.Interface.localprocess.IQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueListenerThreadBack implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(QueueListenerThreadBack.class);

    private Object message;
    private IQueueLocalProcessing queueLocalProcessing;
    private IProducerService producerServiceBack;

    public QueueListenerThreadBack(Object message, IQueueLocalProcessing queueLocalProcessing, IProducerService producerServiceBack) {
        this.message = message;
        this.queueLocalProcessing = queueLocalProcessing;
        this.producerServiceBack = producerServiceBack;
    }

    @Override
    public void run() {
        try {
            queueLocalProcessing.queueLocalProcessing(message, producerServiceBack);
        }catch (Exception e){
            logger.error("QueueListenerThread run error is " + e.getMessage());
        }
    }
}
