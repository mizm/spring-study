package com.miz.study.ch1.usecase;

import com.miz.study.ch1.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackForClassName = {"TestUserServiceException"})
public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
    void sendUpgradeEmail(User user);
}
