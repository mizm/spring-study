package com.miz.study.ch5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageFactoryBeanTest {

    @Autowired
    ApplicationContext context;
    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertEquals(message instanceof Message, true);
        assertEquals(((Message)message).getText(), "boot factory bean");
    }
}