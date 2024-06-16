package com.curso.mitocode.examenfinal.handlers;

import com.curso.mitocode.examenfinal.security.AuthRequest;
import com.curso.mitocode.examenfinal.security.AuthResponse;
import com.curso.mitocode.examenfinal.security.JwtUtil;
import com.curso.mitocode.examenfinal.services.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

    private final IUsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthHandler(IUsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(ar -> usuarioService.findByUsername(ar.username())
                        .flatMap(u -> {
                            if (BCrypt.checkpw(ar.password(), u.getPassword())){
                                String token = jwtUtil.generateToken(u);
                                return ServerResponse.ok()
                                        .bodyValue(new AuthResponse(token));
                            }

                            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                        }))
                .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
}
