package cn.expopay.messageServer.activemq.adapter.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.expopay.messageServer.Interface.localprocess.IQueueLocalProcessing;
import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.activemq.adapter.IQueueListener;
import cn.expopay.messageServer.activemq.adapter.thread.QueueListenerThreadSend;
import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.util.akka.actor.ActorFactory;
import cn.expopay.messageServer.util.akka.actor.MasterActor;
import cn.expopay.messageServer.util.thread.ThreadControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
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

				/**
				 * 1、线程池的方式
 				 */
				QueueListenerThreadSend thread = new QueueListenerThreadSend(message, queueOneLocalProcessing, producerServiceSend, producerServiceBack);
				ThreadControl.getbalancing().execute(thread);

				/**
				 * 2、换为Akka的Actor的方式测试：
				 * tell发送方法为：异步方法（非阻塞）。但是Actor的onReceive接收方法是单线程阻塞方法
				 */
//				ActorFactory.getMasterActorRef().tell(message, ActorRef.noSender());

				// 测试本类中注解了@Async的receiveMessage方法为异步方法。
//				Thread.currentThread().sleep(6000);

				long timeEndNew = System.currentTimeMillis();
				logger.info("EndTime is " + String.valueOf((timeEndNew- timeStartNew)/1000));
			}
		}catch (Exception e){
			logger.error("QueueListenerAdapterOne run error is " + e.getMessage());
		}
	}
}
