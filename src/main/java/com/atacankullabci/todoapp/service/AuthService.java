package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.common.VerificationToken;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.repository.UserRepository;
import com.atacankullabci.todoapp.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    public void signUpUser(UserLoginDTO userLoginDTO) {
        User user = new User();
        user.setUserName(userLoginDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
        user.setEmail(userLoginDTO.getEmail());
        user.setTodoList(new ArrayList<>());

        userRepository.save(user);

        String token = getGenerateVerificationToken(user);
    }

    private String getGenerateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(Instant.now().plus(1, ChronoUnit.DAYS));

        verificationTokenRepository.save(verificationToken);

        return token;
    }
}
