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
import org.springframework.transaction.annotation.Transactional;
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


    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    public UserServiceImpl(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userDao = userDao;
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }


    @Override
    public void upgradeLevels() throws Exception {
        List<User> users = userDao.getAll();

            for (User user : users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user))
                    userLevelUpgradePolicy.upgradeLevel(user);
            }

    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }
}
