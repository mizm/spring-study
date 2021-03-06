package com.miz.study.ch2.calc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculatorTest {

    Calculator calculator;
    String numFilePath;
    //junit4 @Before 과 동일 각 테스트 메소드가 시작하기 전에 하는 과
    @BeforeEach
    void setUp() {
        this.calculator = new Calculator();
        this.numFilePath = getClass().getResource("/numbers.txt").getPath();
    }
    @Test
    void calcSum() throws IOException {
        int sum = this.calculator.calcSum(this.numFilePath);
        assertEquals(sum,10);
    }
    @Test
    void calcMultiple() throws IOException {
        int sum = this.calculator.calcMultiply(this.numFilePath);
        assertEquals(sum,24);
    }

    @Test
    void concat() throws IOException {
        String sum = this.calculator.concat(this.numFilePath);
        assertEquals(sum,"1234");
    }

}