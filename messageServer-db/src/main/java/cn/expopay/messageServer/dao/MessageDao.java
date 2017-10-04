package cn.expopay.messageServer.dao;

import cn.expopay.messageServer.model.store.QueueMessageStore;

public interface MessageDao {

    int insertMessageInfo(QueueMessageStore queueMessageStore);

    int updateMessageInfo(QueueMessageStore queueMessageStore);

}
