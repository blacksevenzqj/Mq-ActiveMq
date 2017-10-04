package cn.expopay.messageServer.util.akka.actor;

import akka.actor.UntypedActor;
import cn.expopay.messageServer.model.queue.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterActor extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(MasterActor.class);

    // 阻塞方法：就类似于ActiceMq的单线程成监听器一样
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            logger.info("actor 接收到的是 String");
        }else if(message instanceof QueueMessage){
            logger.info("actor 接收到的是 QueueMessage");
            Thread.currentThread().sleep(7000);
        }else{
            unhandled(message);
        }
    }
}
