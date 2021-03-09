package com.miz.study.ch5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;

public class UpperHandler implements InvocationHandler {

    Object target;

    public UpperHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if(ret instanceof String && method.getName().startsWith("say"))
            return ((String)ret).toUpperCase();
        else
            return ret;
    }
}
