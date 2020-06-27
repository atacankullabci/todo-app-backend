package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.NotificationMail;
import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.common.VerificationToken;
import com.atacankullabci.todoapp.dto.AuthenticationResponseDTO;
import com.atacankullabci.todoapp.dto.LoginRequestDTO;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.exceptions.CustomException;
import com.atacankullabci.todoapp.repository.UserRepository;
import com.atacankullabci.todoapp.repository.VerificationTokenRepository;
import com.atacankullabci.todoapp.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, MailService mailService, AuthenticationManager authenticationManager, AuthenticationManagerBuilder authenticationManagerBuilder, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public void signupUser(UserLoginDTO userLoginDTO) {
        User user = new User();
        user.setUserName(userLoginDTO.getUsername());
        user.setFirstName(userLoginDTO.getFirstName());
        user.setLastName(userLoginDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
        user.setEmail(userLoginDTO.getEmail());
        user.setTodoList(new ArrayList<>());

        userRepository.save(user);

        String token = getGenerateVerificationToken(user);

        NotificationMail notificationMail = new NotificationMail("Please Activate Your Account",
                user.getEmail(),
                "Thank you for signing up to Quick-Do, please click on the link below to activate your account" +
                        "\nhttp:localhost:8080/api/auth/account-verification/" + token);

        mailService.sendActivationMail(notificationMail);
    }

    @Transactional
    public void activateUser(String activationToken) throws CustomException {
        // control the token whether it is expired or not
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(activationToken);
        verificationToken.orElseThrow(() -> new CustomException("Invalid token"));

        User activationUser = verificationToken.get().getUser();
        activationUser.setEnabled(true);

        userRepository.save(activationUser);
    }

    public String getGenerateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(Instant.now().plus(1, ChronoUnit.DAYS));

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public AuthenticationResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        // Spring calls UserDetailServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Authentication context has been set
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        return new AuthenticationResponseDTO(token, loginRequestDTO.getUsername());
    }

    public void logout(LoginRequestDTO loginRequestDTO) {
        SecurityContextHolder.clearContext();
    }
}
