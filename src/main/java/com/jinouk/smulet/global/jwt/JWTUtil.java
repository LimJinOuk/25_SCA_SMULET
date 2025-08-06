package com.jinouk.smulet.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil
{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh}")
    private long refresh;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Key getSignKey() {
        return key;
    }

    //Generate The Token
    public String generateToken(String username)
    {
        ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Date now = Date.from(nowKST.toInstant());
        Date expiryDate = Date.from(nowKST.plusSeconds(expiration / 1000).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    //Generate Refresh Token
    public String generateRefresh(String username)
    {
        ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        Date now = Date.from(nowKST.toInstant());
        Date expiryDate = Date.from(nowKST.plusSeconds(refresh / 1000).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    //Validate Token
    public boolean validateToken(String token)
    {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            Date expiryDate = claimsJws.getBody().getExpiration();
            return expiryDate.after(new Date());
        }
        catch (JwtException | IllegalArgumentException e)
        {
            return false;
        }

    }

    public boolean validateRefresh(String Refresh)
    {
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(Refresh);
            Date expiryDate = claimsJws.getBody().getExpiration();
            return expiryDate.after(new Date());
        }
        catch (JwtException | IllegalArgumentException e)
        {
            return false;
        }

    }

    //GetUserName
    public String getUserName(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
