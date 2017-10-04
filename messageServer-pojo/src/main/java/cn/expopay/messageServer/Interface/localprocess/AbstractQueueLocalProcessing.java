package cn.expopay.messageServer.Interface.localprocess;

import cn.expopay.messageServer.Interface.producer.IProducerService;

public abstract class AbstractQueueLocalProcessing implements IQueueLocalProcessing{

    @Override
    public void queueLocalProcessing(Object obj, IProducerService producerServiceSend, IProducerService producerServiceBack) {
        
    }

    @Override
    public void queueLocalProcessing(Object obj, IProducerService producerServiceBack) {

    }
}
