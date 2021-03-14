package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDao;
import com.miz.study.ch1.domain.User;
import com.miz.study.ch1.usecase.UserLevelUpgradePolicyImpl;
import com.miz.study.ch1.usecase.UserService;
import com.miz.study.ch1.usecase.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class TestUserServiceFactory {
    @Autowired
    UserDao userDao;
    @Autowired
    private MailSender mailSender;

    static class TestUserLevelUpgradePolicy extends UserLevelUpgradePolicyImpl {
        private String id;
        public TestUserLevelUpgradePolicy(UserDao userDao, String id, MailSender mailSender) {

            super(userDao,mailSender);
            this.id = id;
        }

        public void upgradeLevel(User user){
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    @Transactional(rollbackFor = TestUserServiceException.class)
    static class TestUserService extends UserServiceImpl{

        public TestUserService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy) {
            super(userDao, userLevelUpgradePolicy);
        }
    }
    @Bean(name="testUserService")
    public UserService testUserService() {
        TestUserService userService
                = new TestUserService(userDao,new TestUserLevelUpgradePolicy(userDao,"3",mailSender));
        return userService;
    }
    @Bean(name="userService")
    public UserService UserService() throws Exception {
        UserService userService
                = new UserServiceImpl(userDao,new UserLevelUpgradePolicyImpl(userDao,mailSender));
        return userService;
    }
}