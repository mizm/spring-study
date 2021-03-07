package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDao;
import com.miz.study.ch1.domain.Level;
import com.miz.study.ch1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MailSender mailSender;
    static class TestUserService extends UserServiceImpl {
        public TestUserService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy, PlatformTransactionManager transactionManager) {
            super(userDao, userLevelUpgradePolicy,transactionManager);
        }
    }
    static class TestUserLevelUpgradePolicy extends UserLevelUpgradePolicyImpl {
        private String id;
        public TestUserLevelUpgradePolicy(UserDao userDao, String id, MailSender mailSender) {
            super(userDao,mailSender);
            this.id = id;
        }

        public void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }


    private List<User> users;
    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new User("1","11","12", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0,"1@naver.com"),
            new User("2","12","12", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0,"2@naver.com"),
            new User("3","13","12", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1,"3@naver.com"),
            new User("4","14","12", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD,"4@naver.com"),
            new User("5","15","12", Level.GOLD,100,100,"5@naver.com")
        );
    }

    @Test
    @DirtiesContext
    void upgradeLevels() throws Exception{
        MockMailSender mockMailSender = new MockMailSender();
        userService.setUserLevelUpgradePolicy(new UserLevelUpgradePolicyImpl(userDao,mockMailSender));
        //강제로 userLevelUpgradePolicy di -> @DirtiesContext
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertEquals(request.size(),2);
        assertEquals(request.get(0),users.get(1).getEmail());
        assertEquals(request.get(1),users.get(3).getEmail());
    }

    @Test
    void add() {
        userDao.deleteAll();
        User userWithLevel = users.get(4);
        User userWithOutLevel = users.get(3);
        userWithOutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithOutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithOutLevelRead = userDao.get(userWithOutLevel.getId());

        assertEquals(userWithLevel.getLevel(),userWithLevelRead.getLevel());
        assertEquals(userWithOutLevelRead.getLevel(),Level.BASIC);
    }

    @Test
    void upgradeAllOrNothing() throws Exception{

        TestUserService testUserService = new TestUserService(
                userDao
                ,new TestUserLevelUpgradePolicy(userDao,users.get(3).getId(),mailSender)
                ,this.transactionManager);
        userDao.deleteAll();
        for(User user : users) userDao.add(user);
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch( TestUserServiceException e) {

        }
        checkLevelUpgraded(users.get(1), false);
    }
    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(userUpdate.getLevel(), user.getLevel());
        }
    }

}