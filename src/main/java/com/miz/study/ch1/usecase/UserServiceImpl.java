package com.miz.study.ch1.usecase;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public int test() {
        System.out.println("hello");
        return 1;
    }
}
