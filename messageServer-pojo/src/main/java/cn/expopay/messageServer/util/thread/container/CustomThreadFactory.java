package cn.expopay.messageServer.util.thread.container;

import cn.expopay.messageServer.util.thread.ThreadControl;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomThreadFactory.class);

    private AtomicInteger count = new AtomicInteger(0);
    private String customThreadFactoryName;
    public CustomThreadFactory(String customThreadFactoryName) {
        this.customThreadFactoryName = customThreadFactoryName;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        String threadName = ThreadControl.class.getSimpleName() + ":" + customThreadFactoryName + ":" + count.addAndGet(1);
        logger.info(threadName);
        t.setName(threadName);
        return t;
    }
}