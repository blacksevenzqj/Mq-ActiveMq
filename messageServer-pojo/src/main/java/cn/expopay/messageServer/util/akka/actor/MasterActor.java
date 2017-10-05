package cn.expopay.messageServer.util.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import cn.expopay.messageServer.model.queue.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterActor extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(MasterActor.class);

    private ActorRef dbActor = getContext().actorOf(Props.create(DbActor.class),"db");

    //由ActorContext创建Actor
    private Props httpClientProps = Props.create(HttpClientActor.class, dbActor);
    private ActorRef httpClientActor = getContext().actorOf(httpClientProps, "httpClient");

    private Props bussinessProps = Props.create(BussinessActor.class, httpClientActor);
    private ActorRef bussinessActor = getContext().actorOf(bussinessProps, "bussiness");

    public MasterActor() {
        logger.info("MasterActor 初始化");
    }

    // 阻塞方法：就类似于ActiceMq的单线程成监听器一样
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            logger.info("MasterActor 接收到的是 String");
        }else if(message instanceof QueueMessage){
            logger.info("MasterActor 接收到的是 QueueMessage");
            bussinessActor.tell(message, ActorRef.noSender());

            // 测试Actor的接收方法onReceive，是否为异步方法（同步方法）
            Thread.currentThread().sleep(7000);
        }else{
            unhandled(message);
        }
    }
}
