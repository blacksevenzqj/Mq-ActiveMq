//package cn.expopay.messageServer.util.thread.container;
//
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import javax.annotation.PreDestroy;
//import java.util.concurrent.*;
//
///**
// * 直接使用ThreadPoolExecutor，可以在@Async注解中使用
// */
//@Component(value = "threadControlContainer")
//public class ThreadControlContainer extends ThreadPoolExecutor {
//
//    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ThreadControlContainer.class);
//
//    // 线程池维护线程的最少数量
//    private static int corePoolSize = 20;
//    // 线程池维护线程的最大数量
//    private static int maximumPoolSize = 50;
//    // 线程池维护线程所允许的空闲时间，单位MS，超时将会终止该线程
//    private static int keepAliveTime = 1000;
//    // 线程池队列大小
//    private static int queueSize = 10;
//
//
//    public ThreadControlContainer() {
//        super(
//                corePoolSize,
//                maximumPoolSize,
//                keepAliveTime,
//                TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<Runnable>(queueSize),
//                new CustomThreadFactory(),
//                new CustomRejectedExecutionHandler()
//        );
//    }
//
//    @PreDestroy
//    public void destroy(){
//        super.shutdown();
//    }
//
//
//}
//
//
