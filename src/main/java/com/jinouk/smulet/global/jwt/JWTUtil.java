package com.jinouk.smulet.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.text.DateFormat;
import java.util.Date;

@Component
public class JWTUtil
{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key getSignKey()
    {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    //Generate The Token
    public String generateToken(String username , String role)
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("role" , role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }
}
