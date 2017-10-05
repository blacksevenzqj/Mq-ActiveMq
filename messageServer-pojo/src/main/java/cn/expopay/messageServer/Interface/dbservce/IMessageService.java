package cn.expopay.messageServer.Interface.dbservce;


import cn.expopay.messageServer.model.store.QueueMessageStore;

public interface IMessageService {

   int insertMessageInfo(QueueMessageStore queueMessageStore);

   int updateMessageInfo(QueueMessageStore queueMessageStore);

}
