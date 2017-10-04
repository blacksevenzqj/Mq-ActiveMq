package cn.expopay.messageServer.activemq.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@Configuration
public class JmsContainerConfig {

    @Autowired
    Queue queueOne;
    @Autowired
    Queue queueAgain;
    @Autowired
    Queue back;
    @Autowired
    Queue backAgain;

    @Autowired
    MessageListenerAdapter jmsMessageListenerAdapterOne;
    @Autowired
    MessageListenerAdapter jmsMessageListenerAdapterAgain;
    @Autowired
    MessageListenerAdapter jmsMessageListenerAdapterBack;
    @Autowired
    MessageListenerAdapter jmsMessageListenerAdapterBackAgain;


    @Bean(name = "jmsDefaultMessageListenerContainerOne")
    public DefaultMessageListenerContainer jmsDefaultMessageListenerContainerOne(ConnectionFactory activeMQConnectionFactory){
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        defaultMessageListenerContainer.setDestination(queueOne);
        defaultMessageListenerContainer.setMessageListener(jmsMessageListenerAdapterOne);
        return defaultMessageListenerContainer;
    }

    @Bean(name = "jmsDefaultMessageListenerContainerAgain")
    public DefaultMessageListenerContainer jmsDefaultMessageListenerContainerAgain(ConnectionFactory activeMQConnectionFactory){
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        defaultMessageListenerContainer.setDestination(queueAgain);
        defaultMessageListenerContainer.setMessageListener(jmsMessageListenerAdapterAgain);
        return defaultMessageListenerContainer;
    }

    @Bean(name = "jmsDefaultMessageListenerContainerBack")
    public DefaultMessageListenerContainer jmsDefaultMessageListenerContainerBack(ConnectionFactory activeMQConnectionFactory){
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        defaultMessageListenerContainer.setDestination(back);
        defaultMessageListenerContainer.setMessageListener(jmsMessageListenerAdapterBack);
        return defaultMessageListenerContainer;
    }

    @Bean(name = "jmsDefaultMessageListenerContainerBackAgain")
    public DefaultMessageListenerContainer jmsDefaultMessageListenerContainerBackAgain(ConnectionFactory activeMQConnectionFactory){
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        defaultMessageListenerContainer.setDestination(backAgain);
        defaultMessageListenerContainer.setMessageListener(jmsMessageListenerAdapterBackAgain);
        return defaultMessageListenerContainer;
    }
}
