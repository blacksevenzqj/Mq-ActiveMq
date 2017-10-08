package cn.expopay.messageServer.util.configuration.yumconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "messageContent.http")
public class MessageContentHttp {

    private int connectionRequestTimeout;
    private int socketTimeout;
    private int connectTimeout;
    private int httpCodeSucess;
    private int httpCodeFail;

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getHttpCodeSucess() {
        return httpCodeSucess;
    }

    public void setHttpCodeSucess(int httpCodeSucess) {
        this.httpCodeSucess = httpCodeSucess;
    }

    public int getHttpCodeFail() {
        return httpCodeFail;
    }

    public void setHttpCodeFail(int httpCodeFail) {
        this.httpCodeFail = httpCodeFail;
    }

    @Override
    public String toString() {
        return "MessageContentHttp{" +
                "connectionRequestTimeout=" + connectionRequestTimeout +
                ", socketTimeout=" + socketTimeout +
                ", connectTimeout=" + connectTimeout +
                ", httpCodeSucess=" + httpCodeSucess +
                ", httpCodeFail=" + httpCodeFail +
                '}';
    }
}
