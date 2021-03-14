package com.miz.study.ch1.usecase;

import com.miz.study.ch1.dao.UserDao;
import com.miz.study.ch1.domain.Level;
import com.miz.study.ch1.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.miz.study.ch1.usecase.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

@Service
public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{

    private UserDao userDao;
    private MailSender mailSender;
    public UserLevelUpgradePolicyImpl(UserDao userDao, MailSender mailSender){

        this.userDao = userDao;
        this.mailSender = mailSender;
    }
    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch(currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown level : " + currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    @Override
    public void sendUpgradeEmail(User user) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("admin@mail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("등급이 " + user.getLevel().name() + "로 올랐어용~~!" );
        this.mailSender.send(mailMessage);
    }
}
