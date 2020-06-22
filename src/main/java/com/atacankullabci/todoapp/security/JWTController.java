package com.atacankullabci.todoapp.security;

import com.atacankullabci.todoapp.config.JwtUtil;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class JWTController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public JWTController(JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserLoginDTO userLoginDTO) {

    }

}
