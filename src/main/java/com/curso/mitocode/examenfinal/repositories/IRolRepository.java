package com.curso.mitocode.examenfinal.repositories;

import com.curso.mitocode.examenfinal.security.documents.Rol;
import reactor.core.publisher.Mono;

public interface IRolRepository extends IRepositorioGenerico<Rol, String> {

    Mono<Rol> findByNombre(String rol);
}
