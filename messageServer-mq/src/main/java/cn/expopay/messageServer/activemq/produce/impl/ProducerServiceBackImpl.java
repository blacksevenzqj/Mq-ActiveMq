package cn.expopay.messageServer.activemq.produce.impl;

import cn.expopay.messageServer.Interface.producer.IProducerService;
import cn.expopay.messageServer.model.queue.BackQueueMessage;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component(value = "producerServiceBack")
public class ProducerServiceBackImpl implements IProducerService {

    protected final Logger logger = LoggerFactory.getLogger(ProducerServiceBackImpl.class);

    @Autowired
    private JmsTemplate jmsTemplateBack;

    public void sendMessage(String destinationName, Object obj) {
        jmsTemplateBack.convertAndSend(destinationName, obj);
    }

    public void publishConvertPostProcessor(String destinationName, Object obj, long delayTime) {
        if(obj != null && obj instanceof BackQueueMessage) {
            jmsTemplateBack.convertAndSend(destinationName, obj, new MessagePostProcessor(){
                public Message postProcessMessage(Message message) throws JMSException {
                    message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayTime);
                    return message;
                }
            });
        }
    }

}
