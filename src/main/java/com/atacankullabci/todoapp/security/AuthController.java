package com.atacankullabci.todoapp.security;

import com.atacankullabci.todoapp.dto.AuthenticationResponseDTO;
import com.atacankullabci.todoapp.dto.LoginRequestDTO;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.exceptions.CustomException;
import com.atacankullabci.todoapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthService authService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, AuthService authService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserLoginDTO userLoginDTO) throws CustomException {
        // compare new coming obj to db to check whether the same obj is trying to add twice
        if (userLoginDTO != null) {
            authService.signupUser(userLoginDTO);
        } else {
            throw new CustomException("Bad request");
        }
        return ResponseEntity.ok("User has been registered");
    }

    @GetMapping("/account-verification/{activationToken}")
    public ResponseEntity<String> activateUser(@PathVariable(name = "activationToken") String activationToken) throws CustomException {
        if (activationToken != null) {
            authService.activateUser(activationToken);
        } else {
            throw new CustomException("Your activation token is either revoked or incorrect");
        }
        return ResponseEntity.ok("Account has been activated");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws CustomException {
        AuthenticationResponseDTO authenticationResponseDTO = null;
        try {
            authenticationResponseDTO = authService.loginUser(loginRequestDTO);
        } catch (DisabledException e) {
            throw new CustomException("The user have not been activated");
        }

        return ResponseEntity.ok().body(authenticationResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LoginRequestDTO loginRequestDTO) {
        authService.logout(loginRequestDTO);
        return ResponseEntity.ok().build();
    }
}
