package com.curso.mitocode.examenfinal.security;

import com.curso.mitocode.examenfinal.exceptions.ApiException;
import com.curso.mitocode.examenfinal.security.documents.Rol;
import com.curso.mitocode.examenfinal.security.documents.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 5 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secretKey;


    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", usuario.getUsername());
        claims.put("roles", usuario.getRoles().stream().map(Rol::getNombre).toList());

        return doGenerateToken(claims, usuario);
    }

    private String doGenerateToken(Map<String, Object> claims, Usuario usuario) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        try{
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token).getPayload();
        }catch (MalformedJwtException e){
            throw new ApiException("Token fue manipulado.");
        }catch (UnsupportedJwtException e){
            throw new ApiException("Token no soportado.");
        }catch (ExpiredJwtException e){
            throw new ApiException("Token expirado.");
        }catch (IllegalArgumentException e){
            throw new ApiException("Token vacío.");
        }catch (SignatureException e){
            throw new ApiException("Fallo con la firma.");
        }
    }

    public boolean validateToken(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }
}
