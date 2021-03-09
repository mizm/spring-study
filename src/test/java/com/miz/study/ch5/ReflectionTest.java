package com.miz.study.ch5;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReflectionTest {
    @Test
    void invokeMethod() throws Exception {
        String name = "spring";

        assertEquals(name.length(),6);

        Method lengthMethod = String.class.getMethod("length");
        assertEquals((Integer) lengthMethod.invoke(name),  6);

        assertEquals(name.charAt(0), 's');
        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertEquals((Character) charAtMethod.invoke(name,0),'s');
    }
}