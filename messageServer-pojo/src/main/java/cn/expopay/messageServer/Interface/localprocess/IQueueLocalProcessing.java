package cn.expopay.messageServer.Interface.localprocess;

import cn.expopay.messageServer.Interface.producer.IProducerService;

public interface IQueueLocalProcessing {

    void queueLocalProcessing(Object obj, IProducerService producerServiceSend, IProducerService producerServiceBack);

    void queueLocalProcessing(Object obj, IProducerService producerServiceBack);
}
