package cn.expopay.messageServer.util.thread.container.task;

import cn.expopay.messageServer.util.thread.container.CustomRejectedExecutionHandler;
import cn.expopay.messageServer.util.thread.container.CustomThreadFactory;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 使用Spring的ThreadPoolTaskExecutor，可以在@Async注解中使用
 */
@Component(value = "threadTaskControlContainer")
public class ThreadTaskControlContainer extends ThreadPoolTaskExecutor {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ThreadTaskControlContainer.class);

    // 线程池维护线程的最少数量
    private static int corePoolSize = 20;
    // 线程池维护线程的最大数量
    private static int maximumPoolSize = 50;
    // 线程池维护线程所允许的空闲时间，单位MS，超时将会终止该线程
    private static int keepAliveTime = 1;
    // 线程池队列大小
    private static int queueSize = 10;

    public ThreadTaskControlContainer() {
    }

    @PostConstruct
    public void init(){
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maximumPoolSize);
        setKeepAliveSeconds(keepAliveTime);
        setQueueCapacity(queueSize);
        setThreadFactory(new CustomThreadFactory("ReceivePool"));
        setRejectedExecutionHandler(new CustomRejectedExecutionHandler("ReceivePool"));
        initialize();
    }

    @PreDestroy
    public void containerDestroy(){
        destroy();
    }

}
