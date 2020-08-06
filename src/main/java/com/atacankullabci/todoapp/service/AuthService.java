package com.atacankullabci.todoapp.service;

import com.atacankullabci.todoapp.common.NotificationMail;
import com.atacankullabci.todoapp.common.User;
import com.atacankullabci.todoapp.common.VerificationToken;
import com.atacankullabci.todoapp.dto.AuthenticationResponseDTO;
import com.atacankullabci.todoapp.dto.LoginRequestDTO;
import com.atacankullabci.todoapp.dto.RefreshTokenRequestDTO;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.exceptions.CustomException;
import com.atacankullabci.todoapp.repository.UserRepository;
import com.atacankullabci.todoapp.repository.VerificationTokenRepository;
import com.atacankullabci.todoapp.security.jwt.JwtProvider;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       MailService mailService,
                       AuthenticationManagerBuilder authenticationManagerBuilder, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public void signupUser(UserLoginDTO userLoginDTO) {
        if (this.userRepository.findByEmail(userLoginDTO.getEmail()).isPresent()) {
            throw new CustomException("Existing user");
        }

        User user = new User();
        user.setUserName(userLoginDTO.getUsername());
        user.setFirstName(userLoginDTO.getFirstName());
        user.setLastName(userLoginDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
        user.setEmail(userLoginDTO.getEmail());
        user.setTodoList(new ArrayList<>());

        if (userRepository.findByUserNameAndEmail(user.getUserName(), user.getEmail()).isPresent()) {
            throw new CustomException("User exists !");
        }

        userRepository.save(user);

        String token = getGenerateVerificationToken(user);

        String body = "<!DOCTYPE html>" +
                "<html style=\"text-align: center\"><head><h1>Thank you for signing up to Quick-Do App, please click on the link below to activate your account</h1>" +
                "</head><body><a href=\"http:localhost:8080/api/auth/account-verification/" + token + "\">Verify Your Account</a> </body></html>";

        NotificationMail notificationMail = new NotificationMail("Please Activate Your Account",
                user.getEmail(),
                body);

        mailService.sendActivationMail(notificationMail);
    }

    @Transactional
    public void activateUser(String activationToken) throws CustomException {
        // control the token whether it is expired or not
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(activationToken);
        if (Instant.now().plus(1, ChronoUnit.DAYS).compareTo(verificationToken.get().getExpirationDate()) < 0) {
            throw new CustomException("Invalid token");
        }

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

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        return new AuthenticationResponseDTO(jwt,
                refreshTokenService.generateRefreshToken().getToken(),
                Instant.now().plusMillis(jwtProvider.getExpirationTimeInMillis()),
                loginRequestDTO.getUsername());
    }

    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        refreshTokenService.validateRefreshToken(refreshTokenRequestDTO.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequestDTO.getUsername());

        return new AuthenticationResponseDTO(
                token,
                refreshTokenRequestDTO.getRefreshToken(),
                Instant.now().plusMillis(jwtProvider.getExpirationTimeInMillis()),
                refreshTokenRequestDTO.getUsername()
        );
    }

    public void logoutUser(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequestDTO.getRefreshToken());
    }

    //public void logout(LoginRequestDTO loginRequestDTO) {
    //  SecurityContextHolder.clearContext();
    // }
}
