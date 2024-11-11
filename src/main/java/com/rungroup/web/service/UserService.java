package com.rungroup.web.service;

import com.rungroup.web.dto.RegistrationDTO;
import com.rungroup.web.models.UserEntity;

public interface UserService {

    void saveUser(RegistrationDTO registrationDTO);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String userName);
}
