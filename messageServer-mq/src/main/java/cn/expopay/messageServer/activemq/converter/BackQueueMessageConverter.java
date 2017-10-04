package cn.expopay.messageServer.activemq.converter;

import cn.expopay.messageServer.model.back.BackMessage;
import cn.expopay.messageServer.model.queue.BackQueueMessage;
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

@Component(value = "backQueueMessageConverter")
public class BackQueueMessageConverter implements MessageConverter {

    private final Logger logger = LoggerFactory.getLogger(BackQueueMessageConverter.class);

    //接收方法
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        logger.info("BackQueueMessageConverter fromMessage===>>> time is " + new Date(System.currentTimeMillis()));

        MapMessage msg = (MapMessage) message;

        BackQueueMessage bqm = new BackQueueMessage();
        BackMessage bm = new BackMessage();
        bm.setSendId(msg.getString("sendId"));
        bm.setBackTime(msg.getString("backTime"));
        bm.setCode(msg.getInt("code"));
        bm.setMsg(msg.getString("msg"));
        bm.setContent(msg.getString("content"));
        bm.setSignBackMq(msg.getString("signBackMq"));

        bqm.setBackMessage(bm);
        bqm.setBackUrl(msg.getString("backUrl"));

        bqm.setKeyVersion(msg.getString("keyVersion"));

        bqm.setBackAgainTime(msg.getString("backAgainTime"));
        bqm.setTotalBackNum(msg.getInt("totalBackNum"));
        bqm.setCurrentBackNum(msg.getInt("currentBackNum"));

        return bqm;
    }

    // 发送方法
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        logger.info("BackQueueMessageConverter toMessage===>>> time is " + new Date(System.currentTimeMillis()));

        MapMessage msg = session.createMapMessage();

        BackQueueMessage bqm = (BackQueueMessage) object;
        BackMessage bm = bqm.getBackMessage();

        msg.setString("sendId", bm.getSendId());
        msg.setString("backTime", bm.getBackTime());
        msg.setInt("code", bm.getCode());
        msg.setString("msg", bm.getMsg());
        msg.setString("content", bm.getContent());
        msg.setString("signBackMq", bm.getSignBackMq());

        msg.setString("backUrl", bqm.getBackUrl());

        msg.setString("keyVersion", bqm.getKeyVersion());

        msg.setString("backAgainTime", bqm.getBackAgainTime());
        msg.setInt("totalBackNum", bqm.getTotalBackNum());
        msg.setInt("currentBackNum", bqm.getCurrentBackNum());

        return msg;
    }

}
