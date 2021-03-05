package com.miz.study.ch1.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CallTestTest {

    @Autowired
    private CallTest callTest;

    @Test
    void call() {
        assertEquals(callTest.call(),1);
    }
}