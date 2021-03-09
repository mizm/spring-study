package com.miz.study.ch1.proxy.jdk;

import com.miz.study.ch1.usecase.UserService;
import com.miz.study.ch1.usecase.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class UserServiceFactory {
    @Bean(name="userService")
    public TxPorxyFactoryBean userService(UserServiceImpl userServiceImpl, PlatformTransactionManager platformTransactionManager) throws Exception {
        String pattern = "upgradeLevels";
        TxPorxyFactoryBean txPorxyFactoryBean = new TxPorxyFactoryBean();
        txPorxyFactoryBean.setTarget(userServiceImpl);
        txPorxyFactoryBean.setTransactionManager(platformTransactionManager);
        txPorxyFactoryBean.setPattern(pattern);
        txPorxyFactoryBean.setServiceInterface(UserService.class);
        return txPorxyFactoryBean;
    }
}