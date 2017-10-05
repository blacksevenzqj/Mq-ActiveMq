package cn.expopay.messageServer.util.akka.actor;

import akka.actor.UntypedActor;
import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import cn.expopay.messageServer.util.container.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbActor extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(DbActor.class);

    public DbActor() {
        logger.info("DbActor 初始化");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            logger.info("DbActor 接收到的是 String");
        }else if(message instanceof QueueMessageStore){
            logger.info("DbActor 接收到的是 QueueMessageStore");
            QueueMessageStore queueMessageStore = (QueueMessageStore) message;
            IMessageService messageService = (IMessageService) SpringUtils.getBean("messageService");
            messageService.updateMessageInfo(queueMessageStore); // 改为更新操作
        }else{
            unhandled(message);
        }
    }
}
