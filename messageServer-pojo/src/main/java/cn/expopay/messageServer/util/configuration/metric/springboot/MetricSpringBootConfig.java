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

    @Bean(name = "consumers")
    public Meter consumers(MetricRegistry metrics) {
        return metrics.meter("consumers");
    }
    @Bean(name = "producers")
    public Meter producers(MetricRegistry metrics) {
        return metrics.meter("producers");
    }

    @Bean(name = "compareRatio")
    public CompareRatio compareRatio(MetricRegistry metrics, Meter consumers, Meter producers) {
        CompareRatio compareRatio = new CompareRatio(consumers, producers);
        // 生产者消费者比率
        metrics.register("ProducerConsumerRatio", compareRatio);
        return compareRatio;
    }

    @Bean(name = "requestFailCount")
    public Counter requestFailCount(MetricRegistry metrics) {
        return metrics.counter("requestFailCount");
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
