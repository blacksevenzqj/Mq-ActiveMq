package cn.expopay.messageServer.util.configuration;

import cn.expopay.messageServer.util.akka.actor.ActorFactory;
import cn.expopay.messageServer.util.configuration.Initializationconfig.ActiveMQDelayConfig;
import cn.expopay.messageServer.util.configuration.Initializationconfig.RsaKeyConfig;
import cn.expopay.messageServer.util.thread.ThreadControl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class InitializationConfiguration {

    @PostConstruct
    public void init(){
        ActiveMQDelayConfig activeMQDelayConfig = new ActiveMQDelayConfig();
        activeMQDelayConfig.init();

        RsaKeyConfig RsaKeyConfig = new RsaKeyConfig();
        RsaKeyConfig.init();

        ThreadControl.getThreadPoolExecutor();

        // Akka初始化
        ActorFactory.getMasterActorRef();
    }

    @PreDestroy
    public void dostory(){
        ThreadControl.destory();

        ActorFactory.destoryActorSystem();
    }



}
