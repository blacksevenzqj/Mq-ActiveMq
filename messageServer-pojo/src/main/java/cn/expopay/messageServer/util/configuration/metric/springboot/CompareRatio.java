package cn.expopay.messageServer.util.configuration.metric.springboot;

import com.codahale.metrics.Meter;
import com.codahale.metrics.RatioGauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareRatio extends RatioGauge {

    private Logger logger = LoggerFactory.getLogger(CompareRatio.class);

    private final Meter consumers;
    private final Meter producers;

    public CompareRatio(Meter consumers, Meter producers) {
        this.consumers = consumers;
        this.producers = producers;
    }

    @Override
    protected Ratio getRatio() {
//        logger.info("consumers is " + consumers.getOneMinuteRate());
//        logger.info("producers is " + producers.getOneMinuteRate());
        return Ratio.of(consumers.getOneMinuteRate(), producers.getOneMinuteRate());
    }
}
