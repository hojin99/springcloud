package com.inno.sc.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class JwtEncoderApplication implements CommandLineRunner {

    private final String secret = "TEU2nXtXo9uENw4C8QprZEaxezKwVnGJ3cJ/0+bgxo8=";

    public static void main(String[] args) {
        SpringApplication.run(JwtEncoderApplication.class, args);
    }

    @Override
    public void run(String... args) throws UnsupportedEncodingException {
        log.info("run!!!");

        // Secret Key 생성 (생성 후 보관 및 상대 측에 전달)
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String secretString = Encoders.BASE64.encode(key.getEncoded());
//        log.info(secretString);

        // jwt 생성
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String jws = Jwts.builder()
                .setIssuer("inno")
                .setSubject("hojin")
                .claim("age", "48")
                .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(key)
                .compact();

        log.info(jws);

        // jwt 파싱
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws)
                .getBody();

        String subject = claims.getSubject();

        log.info(subject);

    }

}
