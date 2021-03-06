package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDaoJdbc;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserDaoJdbc userDao;

    public UserServiceImpl(UserDaoJdbc userDao) {
        this.userDao = userDao;
    }
    @Override
    public int test() {
        System.out.println("hello");
        return 1;
    }
}
