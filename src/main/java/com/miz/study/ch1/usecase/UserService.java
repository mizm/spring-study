package com.miz.study.ch1.usecase;

import com.miz.study.ch1.domain.User;

import java.sql.SQLException;

public interface UserService {

    void upgradeLevels() throws Exception;
    void add(User user);
    void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy);
}
