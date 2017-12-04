//package cn.expopay.messageServer.activemq.configuration;
//
//import com.happylifeplat.netty.config.NettyContainerConfig;
//import com.happylifeplat.netty.container.NettyEmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class NettyHttpServerConfig {
//
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer(){
//        NettyContainerConfig nettyContainerConfig = NettyContainerConfig.builder().build();
//        NettyEmbeddedServletContainerFactory factory = new NettyEmbeddedServletContainerFactory(nettyContainerConfig);
//        return factory;
//    }
//
//}
