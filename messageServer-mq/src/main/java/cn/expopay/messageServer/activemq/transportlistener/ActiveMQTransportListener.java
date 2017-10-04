package cn.expopay.messageServer.activemq.transportlistener;

import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ActiveMQTransportListener implements TransportListener {

    protected final Logger logger = LoggerFactory.getLogger(ActiveMQTransportListener.class);

    @Override
    public void onCommand(Object o) {
//        logger.debug("ActiveMQTransportListener are onCommand Object is " + o);
    }

    @Override
    public void onException(IOException e) {
        logger.error("ActiveMQTransportListener are onException IOException is" + e);
    }

    @Override
    public void transportInterupted() {
        logger.error("ActiveMQTransportListener are 断开 run");
    }

    @Override
    public void transportResumed() {
        logger.error("ActiveMQTransportListener are 重连 run");
    }
}
