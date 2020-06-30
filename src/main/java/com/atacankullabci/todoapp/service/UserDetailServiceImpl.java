package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

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
        Optional<User> user = userRepository.findByUserName(username);

        // users 'enabled' property should be true when authorizing the user. otherwise spring throws exception
        return new org.springframework.security.core.userdetails.User(user.get().getUserName(),
                user.get().getPassword(),
                user.get().getEnabled(),
                true, true, true,
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
