package com.miz.study.ch2.calc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer ret = callback.doSomethingWithReader(br);
            return ret;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br == null) {
                try { br.close(); }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public <T> T lineReadTemplate(String filepath, LineCallback callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = (T) callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br == null) {
                try { br.close(); }
                catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath,sumCallback,0);
    }
    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath,sumCallback,1);
    }

    public String concat(String filepath) throws IOException {
        LineCallback<String> sumCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };
        return lineReadTemplate(filepath,sumCallback,"");
    }


}
