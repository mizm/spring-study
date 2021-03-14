package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDao;
import com.miz.study.ch1.domain.Level;
import com.miz.study.ch1.domain.User;
import com.miz.study.ch1.proxy.jdk.TxPorxyFactoryBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserService testUserService;
    @Autowired
    private UserDao userDao;



    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
    }


    private List<User> users;
    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new User("1","1121","12", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0,"1@naver.com"),
            new User("2","12","12", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0,"2@naver.com"),
            new User("3","13","12", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1,"3@naver.com"),
            new User("4","14","12", Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD,"4@naver.com"),
            new User("5","15","12", Level.GOLD,100,100,"5@naver.com")
        );
    }

    @Test
    void upgradeLevels() throws Exception{
        //고립된 테스트
//        UserDao mockUserDao = new MockUserDao(users);
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);

//        MockMailSender mockMailSender = new MockMailSender();
        MailSender mockMailSender = mock(MailSender.class);
        UserServiceImpl TestUserServiceImpl = new UserServiceImpl(mockUserDao,new UserLevelUpgradePolicyImpl(mockUserDao,mockMailSender));
        //강제로 userLevelUpgradePolicy di -> @DirtiesContext
//        userDao.deleteAll();
//        for(User user : users) userDao.add(user);
        TestUserServiceImpl.upgradeLevels();
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));

        verify(mockUserDao).update(users.get(1));
        checkUserAndLevel(users.get(1),"2",Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        checkUserAndLevel(users.get(3),"4",Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender,times(2)).send(messageArgumentCaptor.capture());
        List<SimpleMailMessage> mailMessages = messageArgumentCaptor.getAllValues();
        assertEquals(mailMessages.get(0).getTo()[0],users.get(1).getEmail());
        assertEquals(mailMessages.get(1).getTo()[0],users.get(3).getEmail());

//        List<User> updated = mockUserDao.getUpdated();
//        assertEquals(updated.size(),2);
//        checkUserAndLevel(updated.get(0),"2",Level.SILVER);
//        checkUserAndLevel(updated.get(1),"4",Level.GOLD);

//        checkLevelUpgraded(users.get(0), false);
//        checkLevelUpgraded(users.get(1), true);
//        checkLevelUpgraded(users.get(2), false);
//        checkLevelUpgraded(users.get(3), true);
//        checkLevelUpgraded(users.get(4), false);

//        List<String> request = mockMailSender.getRequests();
//        assertEquals(request.size(),2);
//        assertEquals(request.get(0),users.get(1).getEmail());
//        assertEquals(request.get(1),users.get(3).getEmail());
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
    void upgradeAllOrNothing() {
//
//        TestUserService testUserService = new TestUserService(
//                userDao
//                ,new TestUserLevelUpgradePolicy(userDao,users.get(3).getId(),mailSender));
//        userDao.deleteAll();
////
////        TxPorxyFactoryBean txPorxyFactoryBean = context.getBean("&userService", TxPorxyFactoryBean.class);
////        txPorxyFactoryBean.setTarget(testUserService);
////        UserService txUserService = (UserService) txPorxyFactoryBean.getObject();
        //
        for(User user : users) userDao.add(user);
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch( TestUserServiceException e) {
            System.out.println("asdad");
            System.out.println(e.getMessage());
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
    private void checkUserAndLevel(User updated, String expectedId, Level exlevel) {
        assertEquals(updated.getId(),expectedId);
        assertEquals(updated.getLevel(),exlevel);
    }

}