package cn.expopay.messageServer.util.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtils.applicationContext = context;
    }

    public static Object getBean(String name){
        return SpringUtils.applicationContext.getBean(name);
    }

}
