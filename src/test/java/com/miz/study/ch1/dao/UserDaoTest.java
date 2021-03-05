package com.miz.study.ch1.dao;

import com.miz.study.ch1.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void add() throws SQLException {
        User user = new User();
        user.setId("1");
        user.setName("Ee");
        user.setPassword("11");
        System.out.println(userDao.getDataSource());
        userDao.add(user);
    }

    @Test
    void get() {
    }
}