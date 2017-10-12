package cn.expopay.messageServer.util.configuration.metric.springboot;

import com.codahale.metrics.*;
import metrics_influxdb.HttpInfluxdbProtocol;
import metrics_influxdb.InfluxdbReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MetricSpringBootConfig {

    @Bean(name = "metrics")
    public MetricRegistry metrics() {
        return new MetricRegistry();
    }

    /**
     * 在请求阶段直接失败次数统计（没进MQ）
     */
    @Bean(name = "requestFailCount")
    public Counter requestFailCount(MetricRegistry metrics) {
        return metrics.counter("requestFailCount");
    }

    @Bean(name = "producers")
    public Meter producers(MetricRegistry metrics) {
        return metrics.meter("producers");
    }

    /**
     * 在首队列阶段请求成功统计
     */
    @Bean(name = "consumerOne")
    public Meter consumers(MetricRegistry metrics) {
        return metrics.meter("consumerOne");
    }
    @Bean(name = "compareRatioOne")
    public CompareRatio compareRatio(MetricRegistry metrics, Meter consumerOne, Meter producers) {
        CompareRatio compareRatio = new CompareRatio(consumerOne, producers);
        // 生产者消费者比率
        metrics.register("ProducerConsumerRatioOne", compareRatio);
        return compareRatio;
    }

    /**
     * 在重试阶段：请求成功统计
     */
    @Bean(name = "consumerAgain")
    public Meter consumerAgain(MetricRegistry metrics) {
        return metrics.meter("consumerAgain");
    }
    @Bean(name = "compareRatioAgain")
    public CompareRatio compareRatioAgain(MetricRegistry metrics, Meter consumerAgain, Meter producers) {
        CompareRatio compareRatio = new CompareRatio(consumerAgain, producers);
        // 生产者消费者比率
        metrics.register("ProducerConsumerRatioAgain", compareRatio);
        return compareRatio;
    }
    /**
     * 在重试阶段：超过重试次数的请求失败次数统计
     */
    @Bean(name = "againFailCount")
    public Counter againFailCount(MetricRegistry metrics) {
        return metrics.counter("againFailCount");
    }


    /**
     * Reporter 数据的展现位置
     */
    @Bean(name = "consoleReporter")
    public ConsoleReporter consoleReporter(MetricRegistry metrics) {
        return ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean(name = "influxdbReporter")
    public ScheduledReporter influxdbReporter(MetricRegistry metrics) throws Exception {

        return InfluxdbReporter.forRegistry(metrics)
                .protocol(new HttpInfluxdbProtocol("http", "192.168.88.128", 8086, "admin", "admin", "metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .skipIdleMetrics(false)
                .build();
    }

}
