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

import java.util.concurrent.atomic.AtomicInteger;

@Component(value = "queueListenerAdapterOne")
public class QueueListenerAdapterOne implements IQueueListener {

	private final Logger logger = LoggerFactory.getLogger(QueueListenerAdapterOne.class);

	@Autowired
	private IQueueLocalProcessing queueOneLocalProcessing;

	@Autowired
	private IProducerService producerServiceSend;

	@Autowired
	private IProducerService producerServiceBack;

	private AtomicInteger ai = new AtomicInteger(0);

//	@Async("threadControlContainer")
	@Async("threadTaskControlContainer")
	public void receiveMessage(Object message) {
//		logger.info("QueueListenerAdapterOne 接收到一个消息：\t" + message);
		try {
			if (message != null && message instanceof QueueMessage) {
				long timeStartNew = System.currentTimeMillis();
				logger.info("多线程 " + ai.incrementAndGet() + " 进入了");
				Thread.currentThread().sleep(6000);

				QueueListenerThreadSend thread = new QueueListenerThreadSend(message, queueOneLocalProcessing, producerServiceSend, producerServiceBack);
				ThreadControl.getbalancing().execute(thread);

				long timeEndNew = System.currentTimeMillis();
				logger.info("EndTime is " + String.valueOf((timeEndNew- timeStartNew)/1000));
			}
		}catch (Exception e){
			logger.error("QueueListenerAdapterOne run error is " + e.getMessage());
		}
	}
}
