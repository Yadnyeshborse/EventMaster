package com.rungroup.web.repository;

import com.rungroup.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositery extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserName(String userName);
    UserEntity findByEmail(String email);

    UserEntity findFirstByuserName(String userName);
}
