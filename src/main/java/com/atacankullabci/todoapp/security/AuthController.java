package com.atacankullabci.todoapp.security;

import com.atacankullabci.todoapp.config.JwtUtil;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.exceptions.CustomException;
import com.atacankullabci.todoapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserLoginDTO userLoginDTO) throws CustomException {
        if (userLoginDTO != null) {
            authService.signUpUser(userLoginDTO);
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

}
