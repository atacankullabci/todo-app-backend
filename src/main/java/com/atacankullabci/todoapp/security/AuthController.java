package com.atacankullabci.todoapp.security;

import com.atacankullabci.todoapp.dto.AuthenticationResponseDTO;
import com.atacankullabci.todoapp.dto.LoginRequestDTO;
import com.atacankullabci.todoapp.dto.RefreshTokenRequestDTO;
import com.atacankullabci.todoapp.dto.UserLoginDTO;
import com.atacankullabci.todoapp.exceptions.CustomException;
import com.atacankullabci.todoapp.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserLoginDTO userLoginDTO) throws CustomException {
        if (userLoginDTO != null) {
            authService.signupUser(userLoginDTO);
        } else {
            throw new CustomException("Bad request");
        }
        return ResponseEntity.ok().build();
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
        AuthenticationResponseDTO authenticationResponseDTO;
        try {
            authenticationResponseDTO = authService.loginUser(loginRequestDTO);
        } catch (DisabledException e) {
            throw new CustomException("The user have not been activated");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authenticationResponseDTO.getAuthenticationToken());
        return ResponseEntity.ok().headers(httpHeaders).body(authenticationResponseDTO);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return ResponseEntity.ok().body(authService.refreshToken(refreshTokenRequestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        authService.logoutUser(refreshTokenRequestDTO);
        return ResponseEntity.ok().build();
    }
}
