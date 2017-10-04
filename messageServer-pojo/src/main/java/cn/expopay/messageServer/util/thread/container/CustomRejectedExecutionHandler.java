package cn.expopay.messageServer.util.thread.container;

import org.slf4j.LoggerFactory;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomRejectedExecutionHandler.class);

    private String customThreadFactoryName;
    public CustomRejectedExecutionHandler(String customThreadFactoryName) {
        this.customThreadFactoryName = customThreadFactoryName;
    }
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.info("线程池" + customThreadFactoryName + "状态已满,"
                + "线程池中计划被执行的任务总数：" + executor.getTaskCount() + ","
                + "执行完毕的任务数：" + executor.getCompletedTaskCount() + ","
                + "线程池中同时存在最大线程数：" + executor.getLargestPoolSize() + ","
                + "当前正在执行的任务数：" + executor.getActiveCount() + ","
                + "线程池中当前线程数：" + executor.getPoolSize()
        );
        try {
            // 核心改造点，由blockingqueue的offer改成put阻塞方法
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
