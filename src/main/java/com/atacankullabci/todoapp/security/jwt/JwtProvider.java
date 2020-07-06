package com.atacankullabci.todoapp.security.jwt;

import com.atacankullabci.todoapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtProvider {

    private final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    private Key key;

    @Autowired
    private UserRepository userRepository;

    // generate keystore : keytool -genkey -v -keystore todo.jks -alias com.atacankullabci.todo -keyalg RSA -keysize 2048

    @Value("${jwt.signature}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        byte[] keyBytes = null;

        if (jwtSecret != null) {
            keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        User principal = (User) authentication.getPrincipal();

        Instant expireIn = Instant.now().plus(5, ChronoUnit.MINUTES); // Expire in 5 min

        String jwt = Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("auth", authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expireIn))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return jwt;
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        com.atacankullabci.todoapp.common.User userDetails = this.userRepository.findByUserName(claims.getSubject()).get();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        /*Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token);*/
    }

    public boolean decodeJwt(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}










