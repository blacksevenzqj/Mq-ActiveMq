package cn.expopay.messageServer.util.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.expopay.messageServer.model.queue.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BussinessActor extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(BussinessActor.class);

    private ActorRef httpClientActor = null;

    public BussinessActor(ActorRef httpClientActor) {
        this.httpClientActor = httpClientActor;
        logger.info("BussinessActor 初始化");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            logger.info("BussinessActor 接收到的是 String");
        }else if(message instanceof QueueMessage){
            logger.info("BussinessActor 接收到的是 QueueMessage");
            httpClientActor.tell(message, ActorRef.noSender());

            // 测试Actor的接收方法onReceive，是否为异步方法（同步方法）
            Thread.currentThread().sleep(10000);
        }else{
            unhandled(message);
        }
    }
}
