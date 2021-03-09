package com.miz.study.ch5;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Proxy;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloTargetTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertEquals(hello.sayHello("miz"), "Hello miz");
        assertEquals(hello.sayHi("miz"), "Hi miz");
        assertEquals(hello.sayThankYou("miz"), "Thank You miz");

        Hello proxy = new HelloUpper(hello);
        assertEquals(proxy.sayHello("miz"), "Hello miz".toUpperCase());
        assertEquals(proxy.sayHi("miz"), "Hi miz".toUpperCase());
        assertEquals(proxy.sayThankYou("miz"), "Thank You miz".toUpperCase());

        // 다이나믹 프록시
        Hello dynamicProxy = (Hello) Proxy.newProxyInstance(
          getClass().getClassLoader(),
          new Class[] { Hello.class },
          new UpperHandler(new HelloTarget())
        );
        assertEquals(dynamicProxy.sayHello("miz"), "Hello miz".toUpperCase());
        assertEquals(dynamicProxy.sayHi("miz"), "Hi miz".toUpperCase());
        assertEquals(dynamicProxy.sayThankYou("miz"), "Thank You miz".toUpperCase());
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
        assertEquals(proxiedHello.sayHello("miz"), "Hello miz".toUpperCase());
        assertEquals(proxiedHello.sayHi("miz"), "Hi miz".toUpperCase());
        assertEquals(proxiedHello.sayThankYou("miz"), "Thank You miz".toUpperCase());
    }

}