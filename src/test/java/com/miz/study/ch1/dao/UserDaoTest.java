package com.miz.study.ch1.dao;

import com.miz.study.ch1.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private User user, user1, user2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setName("Ee");
        user.setPassword("11");
        user1 = new User();
        user1.setId("2");
        user1.setName("Ees");
        user1.setPassword("11");
        user2 = new User();
        user2.setId("3");
        user2.setName("Eeq");
        user2.setPassword("11");
    }
    @Test
    void addAndGet()  {
        userDao.deleteAll();
        userDao.add(user);
        checkSameUser(user,userDao.get("1"));
        userDao.add(user1);
        checkSameUser(user1,userDao.get("2"));

        userDao.add(user2);
        checkSameUser(user2,userDao.get("3"));

    }

    @Test
    void deleteAll() throws SQLException {
        assertEquals(userDao.getCount(),3);
        userDao.deleteAll();
        assertEquals(userDao.getCount(),0);
    }

    @Test
    void getUserFailure()  {
        //junit5에서의 exception 테스
        assertThrows(EmptyResultDataAccessException.class , () -> {
            userDao.get("44");
        });

    }

    @Test
    void addUserFailure()  {
        //junit5에서의 exception 테스
        assertThrows(
                DataAccessException.class , () -> {
            userDao.add(user);
            userDao.add(user);
        });

    }
    @Test
    void getUserAll() {
        userDao.deleteAll();

        //negative test case
        List<User> users0 = userDao.getAll();
        assertEquals(users0.size(),0);


        userDao.add(user);
        List<User> users1 = userDao.getAll();
        assertEquals(users1.size(), 1);
        checkSameUser(user, users1.get(0));

        userDao.add(user1);
        List<User> users2 = userDao.getAll();
        assertEquals(users2.size(), 2);
        checkSameUser(user, users2.get(0));
        checkSameUser(user1, users2.get(1));

        userDao.add(user2);
        List<User> users3 = userDao.getAll();
        assertEquals(users3.size(), 3);
        checkSameUser(user, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));

    }

    private void checkSameUser(User user1, User user) {
        assertEquals(user.getId(),user1.getId());
        assertEquals(user.getName(),user1.getName());
        assertEquals(user.getPassword(),user1.getPassword());
    }

    //@DirtiesContext -> 특정 테스트에서 어플리케이션 컨텍스트에서 가져온 (@Autowired userDao)의 상태를 변경하고 싶을때 다는 어노테이션
}