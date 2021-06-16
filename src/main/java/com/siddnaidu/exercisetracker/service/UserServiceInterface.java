package com.siddnaidu.exercisetracker.service;

import com.siddnaidu.exercisetracker.config.UserRegistration;
import com.siddnaidu.exercisetracker.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceInterface extends UserDetailsService {
    User save(UserRegistration registration);
}
