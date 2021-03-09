package com.miz.study.ch5;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("message")
public class MessageFactoryBean implements FactoryBean<Message> {

    @Value("boot factory bean")
    String text;


    // 오브젝트 생성할 때 필요한 정보를 팩토리  빈의 프로터티로 설정해서 대신 DI 받게 한다.
    // 주입된 정보는 오브젝트 생성 중에 사용 된다.
    public void setText(String text) {
        this.text = text;
    }


    // 오브젝트 생성
    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
