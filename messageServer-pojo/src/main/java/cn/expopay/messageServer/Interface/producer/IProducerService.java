package cn.expopay.messageServer.Interface.producer;

public interface IProducerService {

  /**
   * 发消息，向默认的 destination
   */
  void sendMessage(String destinationName, Object obj);

  void publishConvertPostProcessor(String destinationName, Object obj, long delayTime);

}
