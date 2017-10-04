package cn.expopay.messageServer.activemq.adapter.thread;

import cn.expopay.messageServer.Interface.localprocess.IQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueListenerThreadSend implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(QueueListenerThreadSend.class);

    private Object message;
    private IQueueLocalProcessing queueLocalProcessing;
    private IProducerService producerServiceSend;
    private IProducerService producerServiceBack;

    public QueueListenerThreadSend(Object message, IQueueLocalProcessing queueLocalProcessing, IProducerService producerServiceSend, IProducerService producerServiceBack) {
        this.message = message;
        this.queueLocalProcessing = queueLocalProcessing;
        this.producerServiceSend = producerServiceSend;
        this.producerServiceBack = producerServiceBack;
    }

    @Override
    public void run() {
        try {
            queueLocalProcessing.queueLocalProcessing(message, producerServiceSend, producerServiceBack );
        }catch (Exception e){
            logger.error("QueueListenerThread run error is " + e.getMessage());
        }
    }
}
