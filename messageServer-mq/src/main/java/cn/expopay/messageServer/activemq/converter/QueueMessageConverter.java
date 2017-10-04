package cn.expopay.messageServer.activemq.converter;

import cn.expopay.messageServer.model.queue.QueueMessage;
import cn.expopay.messageServer.model.send.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;


/**
 * 自定义消息转换器MessageConverter实现
 * 在JmsTemplate提供了各种send方法，供发送消息使用，其中JmsTemplate中的
 * convertAndSend和receiveAndConvert方法能够借助于MessageConverter接口实现消息做相应的转换。
 * JMS1.1中SimpleMessageConverter实现String与TextMessage,等之间的转换
 */
@Component(value = "queueMessageConverter")
public class QueueMessageConverter implements MessageConverter {

    private final Logger logger = LoggerFactory.getLogger(QueueMessageConverter.class);

    //接收方法
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        logger.info("QueueMessageConverter fromMessage===>>> time is " + new Date(System.currentTimeMillis()));

        MapMessage msg = (MapMessage) message;

        SendMessage sm = new SendMessage();
        sm.setSendId(msg.getString("sendId"));
        sm.setSendTime(msg.getString("sendTime"));
        sm.setReceptionUrl(msg.getString("receptionUrl"));
        sm.setBackUrl(msg.getString("backUrl"));
        sm.setDataContent(msg.getString("dataContent"));
        sm.setHeaderContent(msg.getString("headerContent"));
        sm.setTotalNum(msg.getInt("totalNum"));
        sm.setTotalBackNum(msg.getInt("totalBackNum"));
        sm.setRequestMode(msg.getInt("requestMode"));
        sm.setReqSucessMatch(msg.getString("reqSucessMatch"));
        sm.setKeyVersion(msg.getString("keyVersion"));
        sm.setSignSendSender(msg.getString("signSendSender"));

        QueueMessage qm = new QueueMessage();
        //把对象属性分解了：
        qm.setSendMessage(sm);
        qm.setAgainTime(msg.getString("againTime"));
        qm.setCurrentNum(msg.getInt("currentNum"));

        return qm;
    }

    // 发送方法
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        logger.info("QueueMessageConverter toMessage===>>> time is " + new Date(System.currentTimeMillis()));

        MapMessage msg = session.createMapMessage();

        QueueMessage qm = (QueueMessage) object;
        SendMessage sm = qm.getSendMessage();

        msg.setString("sendId", sm.getSendId());
        msg.setString("sendTime", sm.getSendTime());
        msg.setString("receptionUrl", sm.getReceptionUrl());
        msg.setString("backUrl", sm.getBackUrl());
        msg.setString("dataContent", sm.getDataContent());
        msg.setString("headerContent", sm.getHeaderContent());
        msg.setInt("totalNum", sm.getTotalNum());
        msg.setInt("totalBackNum", sm.getTotalBackNum());
        msg.setInt("requestMode", sm.getRequestMode());
        msg.setString("reqSucessMatch", sm.getReqSucessMatch());
        msg.setString("keyVersion", sm.getKeyVersion());
        msg.setString("signSendSender", sm.getSignSendSender());

        msg.setString("againTime", qm.getAgainTime());
        msg.setInt("currentNum", qm.getCurrentNum());

        return msg;
    }

}