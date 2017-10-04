package cn.expopay.messageServer.util.http;

import cn.expopay.messageServer.util.configuration.interfice.IMessageContent;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@Component(value = "httpConnectionManager")
public class HttpConnectionManager {

    private static Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);

    private PoolingHttpClientConnectionManager cm = null;

    private RequestConfig rc = null;

    @PostConstruct
    public void init() {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);

        rc = RequestConfig.custom()
                // 指从连接池获取连接的timeout。连接不够用时等待超时时间，一定要设置，如果不设置的话，如果连接池连接不够用，就会线程阻塞。
                .setConnectionRequestTimeout(IMessageContent.ConnectionRequestTimeout)
                .setSocketTimeout(IMessageContent.SocketTimeout) //指客户端从服务器读取数据的timeout，超出后会抛出SocketTimeOutException
                //指客户端和服务器建立连接的timeout，就是http请求的三个阶段，一：建立连接；二：数据传送；三，断开连接。超时后会ConnectionTimeOutException
                .setConnectTimeout(IMessageContent.ConnectTimeout)
                .build();
    }

    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(rc)
                .build();
        return httpClient;
    }

}
