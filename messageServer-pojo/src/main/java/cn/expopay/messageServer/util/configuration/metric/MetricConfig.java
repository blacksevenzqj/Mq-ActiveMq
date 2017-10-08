package cn.expopay.messageServer.util.configuration.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MetricConfig {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MetricConfig.class);

    /**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private final MetricRegistry metrics = new MetricRegistry();
    /**
     * 在控制台上打印输出
     */
    private final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
    /**
     * 实例化一个请求 Meter
     */
//    private final Meter requests = metrics.meter("SendRequest");
    /**
     * 实例化一个错误 Meter
     */
    private final Meter errorMeter = metrics.meter("errorMeter");
    /**
     * 实例化一个 Timer
     */
    private final Timer requests = metrics.timer("SendRequest");

    public MetricConfig() {
        logger.info("MetricConfig 初始化");
    }

    private static class MetricConfigHandler{
        private static MetricConfig metricConfig = new MetricConfig();
    }

    public static MetricConfig getMetricConfig(){
        return MetricConfigHandler.metricConfig;
    }

    public void startMetric(){
        reporter.start(1, TimeUnit.MINUTES);
    }

//    public Meter getMeter(){
//        return requests;
//    }

    public Meter getErrorMeter(){
        return errorMeter;
    }

    public Timer getTimer(){
        return requests;
    }

}
