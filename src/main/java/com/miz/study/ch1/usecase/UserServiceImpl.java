package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDao;
import com.miz.study.ch1.dao.UserDaoJdbc;
import com.miz.study.ch1.domain.Level;
import com.miz.study.ch1.domain.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
    private UserDao userDao;

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    private PlatformTransactionManager transactionManager;
    
    public UserServiceImpl(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, PlatformTransactionManager transactionManager) {

        this.userDao = userDao;
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
        this.transactionManager = transactionManager;
    }


    @Override
    public void upgradeLevels() throws Exception{

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user))
                    userLevelUpgradePolicy.upgradeLevel(user);
            }
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
