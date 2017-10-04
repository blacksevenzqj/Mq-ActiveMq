package cn.expopay.messageServer.activemq.adapter;

public interface IQueueListener {

    void receiveMessage(Object message);

}
