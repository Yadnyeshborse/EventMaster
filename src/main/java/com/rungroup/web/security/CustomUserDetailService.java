package com.rungroup.web.security;

import com.rungroup.web.models.UserEntity;
import com.rungroup.web.repository.UserRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    private final UserRepositery userRepositery;

    @Autowired
    public CustomUserDetailService(UserRepositery userRepositery) {
        this.userRepositery = userRepositery;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            logger.debug("Attempting to load user by username: {}", userName); // Log username

            UserEntity user = userRepositery.findFirstByuserName(userName);
            System.out.println("BOR ="+user);

            if (user != null) {
                // Log user details before returning
                logger.info("User found: {}", user); // Logs user entity details to console

                return new User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toList())
                );
            } else {
                logger.error("User not found: {}", userName);
                throw new UsernameNotFoundException("Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Error occurred while loading user by username: {}", userName, e);
            throw new UsernameNotFoundException("Error occurred while loading user details", e);
        }
    }

}
