package com.curso.mitocode.examenfinal.services;

import com.curso.mitocode.examenfinal.repositories.IRepositorioGenerico;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServicioGenerico<T, ID> {

    Flux<T> findAll();

    Mono<T> findById(ID id);

    Mono<T> save(T t);

    Mono<T> update(T t, ID id);

    Mono<Void> delete(ID id);

    IRepositorioGenerico<T, ID> getRepository();
}
