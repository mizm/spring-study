package com.miz.study.ch5;

public class Message {
    String text;

    private Message(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //생성자 대신 사용할수있는 static 팩토리 메서드
    public static Message newMessage(String text) {
        return new Message(text);
    }
}
