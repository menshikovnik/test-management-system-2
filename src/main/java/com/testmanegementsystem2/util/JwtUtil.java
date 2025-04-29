package com.testmanegementsystem2.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {
    private static final Algorithm ALG = Algorithm.HMAC256("секретный_ключ");

    public static String generate(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .sign(ALG);
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(ALG).build().verify(token);
    }
}
