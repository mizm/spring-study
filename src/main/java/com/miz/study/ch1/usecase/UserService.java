package com.miz.study.ch1.usecase;

import com.miz.study.ch1.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface UserService {
    void add(User user);
    void deleteAll();
    void update(User user);

    //@Transactional(readOnly=true)
    User get(String id);

    //@Transactional(readOnly=true)
    List<User> getAll();

    void upgradeLevels();
}
