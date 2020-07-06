package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.exceptions.UserNotActivatedException;
import com.atacankullabci.todoapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Authenticate a user from the db
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(loginUser -> createSpringSecurityUser(username, loginUser))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getEnabled()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
