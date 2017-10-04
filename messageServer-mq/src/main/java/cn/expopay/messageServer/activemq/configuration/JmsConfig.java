package cn.expopay.messageServer.activemq.configuration;

import cn.expopay.messageServer.activemq.adapter.IQueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConverter;
import org.apache.activemq.command.ActiveMQQueue;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Configuration
public class JmsConfig {

    public final static String QueueOne = "queueOne";
    public final static String QueueAgain = "queueAgain";
    public final static String QueueBack = "queueBack";
    public final static String QueueBackAgain = "queueBackAgain";

    @Bean(name = "queueOne")
    public Queue queueOne() {
        return new ActiveMQQueue(QueueOne);
    }
    @Bean(name = "queueAgain")
    public Queue queueAgain(){
        return new ActiveMQQueue(QueueAgain);
    }
    @Bean(name = "back")
    public Queue queueBack() {
        return new ActiveMQQueue(QueueBack);
    }
    @Bean(name = "backAgain")
    public Queue queueBackAgain() {
        return new ActiveMQQueue(QueueBackAgain);
    }


    @Autowired
    MessageConverter queueMessageConverter;

    @Autowired
    MessageConverter backQueueMessageConverter;

    @Autowired
    IQueueListener queueListenerAdapterOne;

    @Autowired
    IQueueListener queueListenerAdapterAgain;

    @Autowired
    IQueueListener queueListenerAdapterBack;

    @Autowired
    IQueueListener queueListenerAdapterBackAgain;


    // 发送消息生成者
    @Bean(name= "jmsTemplateSend")
    public JmsTemplate jmsTemplateSend(ConnectionFactory activeMQConnectionFactory){
        JmsTemplate jmsTemplateSend = new JmsTemplate();
        jmsTemplateSend.setConnectionFactory(activeMQConnectionFactory);
        jmsTemplateSend.setReceiveTimeout(10000);
        jmsTemplateSend.setMessageConverter(queueMessageConverter);
        return jmsTemplateSend;
    }

    // 回复消息生成者
    @Bean(name = "jmsTemplateBack")
    public JmsTemplate jmsTemplateBack(ConnectionFactory activeMQConnectionFactory){
        JmsTemplate JmsTemplateBack = new JmsTemplate();
        JmsTemplateBack.setConnectionFactory(activeMQConnectionFactory);
        JmsTemplateBack.setReceiveTimeout(10000);
        JmsTemplateBack.setMessageConverter(backQueueMessageConverter);
        return JmsTemplateBack;
    }

    // 发送首次队列监听适配器
    @Bean(name = "jmsMessageListenerAdapterOne")
    public MessageListenerAdapter jmsMessageListenerAdapterOne(){
        MessageListenerAdapter messageListenerAdapterOne = new MessageListenerAdapter();
        messageListenerAdapterOne.setDelegate(queueListenerAdapterOne);
        messageListenerAdapterOne.setDefaultListenerMethod("receiveMessage");
        messageListenerAdapterOne.setMessageConverter(queueMessageConverter);
        return messageListenerAdapterOne;
    }

    // 发送重试队列监听适配器
    @Bean(name = "jmsMessageListenerAdapterAgain")
    public MessageListenerAdapter jmsMessageListenerAdapterAgain(){
        MessageListenerAdapter messageListenerAdapterAgain = new MessageListenerAdapter();
        messageListenerAdapterAgain.setDelegate(queueListenerAdapterAgain);
        messageListenerAdapterAgain.setDefaultListenerMethod("receiveMessage");
        messageListenerAdapterAgain.setMessageConverter(queueMessageConverter);
        return messageListenerAdapterAgain;
    }

    // 回复首次队列监听适配器
    @Bean(name = "jmsMessageListenerAdapterBack")
    public MessageListenerAdapter jmsMessageListenerAdapterBack(){
        MessageListenerAdapter messageListenerAdapterBack = new MessageListenerAdapter();
        messageListenerAdapterBack.setDelegate(queueListenerAdapterBack);
        messageListenerAdapterBack.setDefaultListenerMethod("receiveMessage");
        messageListenerAdapterBack.setMessageConverter(backQueueMessageConverter);
        return messageListenerAdapterBack;
    }

    // 回复重试队列监听适配器
    @Bean(name = "jmsMessageListenerAdapterBackAgain")
    public MessageListenerAdapter jmsMessageListenerAdapterBackAgain(){
        MessageListenerAdapter messageListenerAdapterBackAgain = new MessageListenerAdapter();
        messageListenerAdapterBackAgain.setDelegate(queueListenerAdapterBackAgain);
        messageListenerAdapterBackAgain.setDefaultListenerMethod("receiveMessage");
        messageListenerAdapterBackAgain.setMessageConverter(backQueueMessageConverter);
        return messageListenerAdapterBackAgain;
    }

}
