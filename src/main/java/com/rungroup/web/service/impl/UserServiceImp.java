package com.rungroup.web.service.impl;

import com.rungroup.web.dto.RegistrationDTO;
import com.rungroup.web.models.Role;
import com.rungroup.web.models.UserEntity;
import com.rungroup.web.repository.RoleRepositery;
import com.rungroup.web.repository.UserRepositery;
import com.rungroup.web.security.CustomUserDetailService;
import com.rungroup.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImp implements UserService {

    private UserRepositery userRepositery;

    private RoleRepositery roleRepositery;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepositery userRepositery, RoleRepositery roleRepositery,PasswordEncoder passwordEncoder) {
        this.userRepositery = userRepositery;
        this.roleRepositery = roleRepositery;
        this.passwordEncoder=passwordEncoder;
    }


    @Override
    public void saveUser(RegistrationDTO registrationDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(registrationDTO.getUserName());
        userEntity.setEmail(registrationDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        Role role = roleRepositery.findByName("USER");
        userEntity.setRoles(Arrays.asList(role));

        // Printing to console for debugging
        System.out.println("Saving user with username: " + userEntity.getUserName());
        System.out.println("User email: " + userEntity.getEmail());
        System.out.println("Assigned role: " + role.getName());

        userRepositery.save(userEntity);
    }


    @Override
    public UserEntity findByUsername(String userName){
        return userRepositery.findByUserName(userName);
    }

    @Override
    public UserEntity findByEmail(String email){
        return userRepositery.findByEmail(email);
    }
}
