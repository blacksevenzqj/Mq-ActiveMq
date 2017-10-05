package cn.expopay.messageServer.service;

import cn.expopay.messageServer.Interface.dbservce.IMessageService;
import cn.expopay.messageServer.dao.MessageDao;
import cn.expopay.messageServer.model.store.QueueMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "messageService")
@Transactional
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageDao messageDao;

    @Override
    public int insertMessageInfo(QueueMessageStore queueMessageStore) {
        return messageDao.insertMessageInfo(queueMessageStore);
    }

    @Override
    public int updateMessageInfo(QueueMessageStore queueMessageStore) {
        return messageDao.updateMessageInfo(queueMessageStore);
    }
}
