package cn.expopay.messageServer.service;

import cn.expopay.messageServer.model.returninfo.ReturnMq;
import cn.expopay.messageServer.model.send.SendMessage;

public interface IBusinessService {

    ReturnMq sendMessageOne(SendMessage sendMessage);

}
