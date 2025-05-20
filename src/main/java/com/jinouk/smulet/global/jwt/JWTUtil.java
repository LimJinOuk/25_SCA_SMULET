package com.jinouk.smulet.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil
{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSignKey()
    {
        System.out.println("Key" + Keys.hmacShaKeyFor(secret.getBytes()));
        return Keys.hmacShaKeyFor(secret.getBytes());

    }


    //Generate The Token
    public String generateToken(String username)
    {
        System.out.println(username);
        Date now = new Date();
        System.out.println(now);
        Date expiryDate = new Date(now.getTime() + expiration);
        System.out.println(expiryDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    //Validate Token
    public void validateToken(String token)
    {
        Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token);

    }

    //GetUserName
    public String getUserName(String token)
    {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
