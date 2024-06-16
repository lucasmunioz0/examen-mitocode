package com.curso.mitocode.examenfinal.repositories;

import com.curso.mitocode.examenfinal.security.documents.Usuario;
import reactor.core.publisher.Mono;

public interface IUsuarioRepository extends IRepositorioGenerico<Usuario, String> {

    Mono<Usuario> findByUsername(String username);
}
