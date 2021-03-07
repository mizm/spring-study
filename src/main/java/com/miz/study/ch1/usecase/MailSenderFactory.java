package com.miz.study.ch1.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailSenderFactory{
    @Bean
    MailSender mailSender() {
        //JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        DummyMailSender mailSender = new DummyMailSender();
        //mailSender.setHost("mail.server.com");
        return mailSender;
    }

}
