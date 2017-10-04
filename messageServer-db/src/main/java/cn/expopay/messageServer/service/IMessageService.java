package cn.expopay.messageServer.service;


import cn.expopay.messageServer.model.store.QueueMessageStore;

public interface IMessageService {

   int insertMessageInfo(QueueMessageStore queueMessageStore);

   int updateMessageInfo(QueueMessageStore queueMessageStore);

}
