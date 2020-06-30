package com.atacankullabci.todoapp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Service
public class JwtProvider {

    private final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    // generate keystore : keytool -genkey -v -keystore todo.jks -alias com.atacankullabci.todo -keyalg RSA -keysize 2048

    /*private KeyStore keyStore;

    @Value("${keystore.password}")
    private String keyStorePass;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/todo.jks");
            keyStore.load(inputStream, keyStorePass.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String generateToken(Authentication authentication) {
    //import org.springframework.security.core.userdetails.User;
        User principal = (User) authentication.getPrincipal();
        return Jwts
                .builder()
                .setSubject(principal.getUsername())
                .setAudience("todo-app")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("todo", keyStorePass.toCharArray());
        } catch (Exception e) {
            try {
                throw new CustomException("Exception occured while retrieving public key from keystore");
            } catch (CustomException customException) {
                customException.printStackTrace();
            }
        }
        return null;
    }
    
 */

    @Value("${jwt.signature}")
    private String jwtSecret;

    public String generateToken(Authentication authentication) {

        User principal = (User) authentication.getPrincipal();

        byte[] secret = Base64.getDecoder().decode(jwtSecret);

        Instant expireIn = Instant.now().plus(5, ChronoUnit.MINUTES); // Expire in 5 min

        String jwt = Jwts.builder()
                .setSubject(principal.getUsername())
                .setAudience("todo-app")
                //.claim("Role", "admin")
                //.claim("Departmen", "CS")
                //.claim("rnd-clm", new Random().nextInt(20) + 1)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expireIn))
                .signWith(hmacShaKeyFor(secret))
                .compact();
        return jwt;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token);
    }

    public boolean decodeJwt(String jwt) {
        byte[] secret = Base64.getDecoder().decode(jwtSecret);

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(hmacShaKeyFor(secret))
                    .build()
                    .parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}










