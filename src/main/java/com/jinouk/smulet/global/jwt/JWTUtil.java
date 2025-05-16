package com.jinouk.smulet.global.jwt;

import io.jsonwebtoken.JwtException;
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

    //Validate Token
    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token);
            return true;
        }
        catch (JwtException e)
        {return false;}
    }

    //GetUserName
    public String getUserName(String token)
    {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
