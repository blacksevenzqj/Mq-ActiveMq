package cn.expopay.messageServer.util.configuration;

import cn.expopay.messageServer.util.akka.actor.ActorFactory;
import cn.expopay.messageServer.util.configuration.Initializationconfig.ActiveMQDelayConfig;
import cn.expopay.messageServer.util.configuration.Initializationconfig.RsaKeyConfig;
import cn.expopay.messageServer.util.configuration.metric.MetricConfig;
import cn.expopay.messageServer.util.thread.ThreadControl;
import com.codahale.metrics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class InitializationConfiguration {

    @Autowired
    ConsoleReporter consoleReporter;

    @Autowired
    ScheduledReporter influxdbReporter;

    @PostConstruct
    public void init(){
        ActiveMQDelayConfig activeMQDelayConfig = new ActiveMQDelayConfig();
        activeMQDelayConfig.init();

        RsaKeyConfig RsaKeyConfig = new RsaKeyConfig();
        RsaKeyConfig.init();

        ThreadControl.getThreadPoolExecutor();

        // Akka初始化
        ActorFactory.getMasterActorRef();

        // 自己实现的：启动Metric，单例形式
//        MetricConfig.getMetricConfig().startMetric();

        // 使用Spring Boot配置的
//        consoleReporter.start(10, TimeUnit.SECONDS);

//        influxdbReporter.start(10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void dostory(){
        ThreadControl.destory();

        ActorFactory.destoryActorSystem();
    }



}
