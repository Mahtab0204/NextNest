package com.kgm.nextnest.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;


    //Generate JWT Token

    public String generateToken(Authentication authentication) {

        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(getSignKey())
                .compact();
    }


     // Secret Key

    private SecretKey getSignKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }


     // Get Username From Token

    public String getUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }


     // Validate JWT Token

    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (SecurityException ex) {

            throw new BadCredentialsException("Invalid JWT Signature.");

        } catch (MalformedJwtException ex) {

            throw new BadCredentialsException("Invalid JWT Token.");

        } catch (ExpiredJwtException ex) {

            throw new BadCredentialsException("JWT Token Expired.");

        } catch (UnsupportedJwtException ex) {

            throw new BadCredentialsException("Unsupported JWT Token.");

        } catch (IllegalArgumentException ex) {

            throw new BadCredentialsException("JWT Claims String is Empty.");

        }

    }
}
