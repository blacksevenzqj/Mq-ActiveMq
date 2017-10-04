package cn.expopay.messageServer.activemq.adapter.impl;

import cn.expopay.messageServer.Interface.localprocess.IQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.activemq.adapter.IQueueListener;
import cn.expopay.messageServer.activemq.adapter.thread.QueueListenerThreadSend;
import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.util.thread.ThreadControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component(value = "queueListenerAdapterAgain")
public class QueueListenerAdapterAgain implements IQueueListener {

	private final Logger logger = LoggerFactory.getLogger(QueueListenerAdapterAgain.class);

	@Autowired
	private IQueueLocalProcessing queueAgainLocalProcessing;

	@Autowired
	private IProducerService producerServiceSend;

	@Autowired
	private IProducerService producerServiceBack;

	@Async("threadTaskControlContainer")
	public void receiveMessage(Object message) {
		logger.info("QueueListenerAdapterAgain 接收到一个消息：\t" + message);
		try {
			if (message != null && message instanceof QueueMessage) {
				QueueListenerThreadSend thread = new QueueListenerThreadSend(message, queueAgainLocalProcessing, producerServiceSend, producerServiceBack);
				ThreadControl.getbalancing().execute(thread);
			}
		}catch (Exception e){
			logger.error("QueueListenerAdapterAgain run error is " + e.getMessage());
		}
	}

}
