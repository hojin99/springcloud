package com.inno.sc.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class JwtUtil {

//    private static String secretKey = "TEU2nXtXo9uENw4C8QprZEaxezKwVnGJ3cJ/0+bgxo8=";

    public static Claims validateJwt(String jws, String secretKey) {

        log.info(secretKey);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jws)
                .getBody();
    }


}