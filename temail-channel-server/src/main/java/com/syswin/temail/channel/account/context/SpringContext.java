package com.syswin.temail.channel.account.context;

import com.syswin.temail.channel.account.service.ConnectionStatusServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by juaihua on 2018/8/21.
 */
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }


    public static ConnectionStatusServiceImpl getConnectionStatusServiceImpl(){
        return applicationContext.getBean(ConnectionStatusServiceImpl.class);
    }


}

