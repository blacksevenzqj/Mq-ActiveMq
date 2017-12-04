package cn.expopay.messageServer.util.filter;

import cn.expopay.messageServer.util.filter.impl.NettyFilerSecond;
import cn.expopay.messageServer.util.filter.impl.NettyFilterFirst;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "filterConfiguration")
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean myFilterRegistrationFirst() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new NettyFilterFirst());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("NettyFilterFirst");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean myFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new NettyFilerSecond());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("NettyFilerSecond");
        registration.setOrder(2);
        return registration;
    }

}
