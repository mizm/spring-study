package com.miz.study.ch1.ui;

import com.miz.study.ch1.usecase.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallTest {
    private UserService userService;

    public CallTest(UserService userService) {
        this.userService = userService;
    }

    public int call() {
        return 1;
    }

}
