package com.atacankullabci.todoapp.security;

import com.atacankullabci.todoapp.exceptions.CustomException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtProvider {

    // generate keystore : keytool -genkey -v -keystore todo.jks -alias com.atacankullabci.todo -keyalg RSA -keysize 2048

    private KeyStore keyStore;

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
    
 /*   public static String createJWT(User user) {

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        byte[] secret = Base64.getDecoder().decode(key.toString());

        System.out.println(key.toString());

        Instant expireIn = Instant.now().plus(5, ChronoUnit.MINUTES); // Expire in 5 min

        String jwt = Jwts.builder()
                .setSubject(user.getFullName())
                .setAudience("todo-app")
                .claim("Role", "admin")
                .claim("Departmen", "CS")
                //.claim("rnd-clm", new Random().nextInt(20) + 1)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expireIn))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
        return jwt;
    }

    public static void decodeJwt(String jwt) {
        byte[] secret = Base64.getDecoder().decode("w7zONawSOaRrgORvqPnKuo+u+U38KgN3gwYQxd6UTcE=");

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .build()
                .parseClaimsJws(jwt);

        System.out.println(claims);
    }

  */
}










