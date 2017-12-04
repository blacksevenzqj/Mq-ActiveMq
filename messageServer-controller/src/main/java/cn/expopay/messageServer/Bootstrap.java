package cn.expopay.messageServer;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration//配置控制
@EnableAutoConfiguration//启用自动配置
@ComponentScan(basePackages = "cn.expopay.messageServer")//组件扫描
@ImportResource(value = { "classpath:spring-consumer.xml" })  // 引入FireFly RPC通信框架
@MapperScan("cn.expopay.messageServer.dao")
public class Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Bootstrap.class, args);
        LOGGER.info("Server running...");
    }

}