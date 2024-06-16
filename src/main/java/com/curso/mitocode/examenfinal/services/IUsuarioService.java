package com.curso.mitocode.examenfinal.services;

import com.curso.mitocode.examenfinal.security.documents.Usuario;
import reactor.core.publisher.Mono;

public interface IUsuarioService extends IServicioGenerico<Usuario, String> {

    Mono<Usuario> findByUsername(String username);

}
