package com.curso.mitocode.examenfinal.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public AuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(token);

        if (username == null || jwtUtil.validateToken(token)) {
            return Mono.error(new BadCredentialsException("Invalid Token"));
        }

        Claims claims = jwtUtil.getClaimsFromToken(token);
        List<String> roles = claims.get("roles", List.class);
        List<SimpleGrantedAuthority> rolesAuth = roles.stream().map(SimpleGrantedAuthority::new).toList();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, rolesAuth);
        return Mono.just(auth);
    }
}
