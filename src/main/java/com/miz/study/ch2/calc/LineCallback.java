package com.miz.study.ch2.calc;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
