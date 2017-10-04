package cn.expopay.messageServer.util.thread;

import cn.expopay.messageServer.util.thread.container.CustomRejectedExecutionHandler;
import cn.expopay.messageServer.util.thread.container.CustomThreadFactory;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * 线程管理类
 */
public class ThreadControl {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ThreadControl.class);

    // 线程池维护线程的最少数量
    private static int corePoolSize = 60;
    // 线程池维护线程的最大数量
    private static int maximumPoolSize = 100;
    // 线程池维护线程所允许的空闲时间，单位MS，超时将会终止该线程
    private static int keepAliveTime = 1000;
    // 线程池队列大小
    private static int queueSize = 100;

    private ThreadPoolExecutor tpec1 = null;
    private ThreadPoolExecutor tpec2 = null;
    private ThreadPoolExecutor tpec3 = null;

    private List<ThreadPoolExecutor> tpecList = new ArrayList<ThreadPoolExecutor>();

    private ThreadControl(){
        factoryMethod();
    }

    private void factoryMethod(){
        final int numberOfCores = Runtime.getRuntime().availableProcessors();
        final double blockingCoefficient = 0.9;
        final int pollSize = (int) (numberOfCores/(1 - blockingCoefficient));
        //40个线程：CPU核心数/(1 - 阻塞系数)
        if(pollSize > corePoolSize){
            corePoolSize = pollSize;
        }
        tpec1 = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize),
                new CustomThreadFactory("BusinessPoolOne"),
                new CustomRejectedExecutionHandler("BusinessPoolOne")
        );
        tpec2 = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize),
                new CustomThreadFactory("BusinessPoolTwo"),
                new CustomRejectedExecutionHandler("BusinessPoolTwo")
        );
        tpec3 = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize),
                new CustomThreadFactory("BusinessPoolThree"),
                new CustomRejectedExecutionHandler("BusinessPoolThree")
        );
        tpecList.add(tpec1);
        tpecList.add(tpec2);
        tpecList.add(tpec3);
    }

    private static class ThreadControlHandler{
        private static ThreadControl tc = new ThreadControl();
    }

    public static List<ThreadPoolExecutor> getThreadPoolExecutor(){
        return ThreadControlHandler.tc.newRetractedThreadPool();
    }

    private List<ThreadPoolExecutor> newRetractedThreadPool() {
        return tpecList;
    }

    public static ThreadPoolExecutor getbalancing(){
        List<ThreadPoolExecutor> list = getThreadPoolExecutor();
        Map<Integer, Integer> activeCountMap = new HashMap<Integer, Integer>();
        for(int i = 0; i < list.size(); i++){
            activeCountMap.put(i, list.get(i).getActiveCount());
        }
        Integer key = getMinValue2Key(activeCountMap);
        if(key != null){
            activeCountMap = null;
            return list.get(key);
        }
        return null;
    }

    public static Integer getMinValue2Key(Map<Integer, Integer> map) {
        if (map == null) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        int minValue = Integer.valueOf(obj[0].toString());

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
            if (entry != null && entry.getValue() == minValue) {
                return(entry.getKey());
            }
        }
        return null;
    }


    public static void destory() {
        if(ThreadControlHandler.tc.newRetractedThreadPool() != null && ThreadControlHandler.tc.newRetractedThreadPool().size() > 0) {
            for(ThreadPoolExecutor tpe:ThreadControlHandler.tc.newRetractedThreadPool()){
                tpe.shutdown();
            }
        }
    }

}


