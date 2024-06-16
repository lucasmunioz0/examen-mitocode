package com.curso.mitocode.examenfinal.services.impl;

import com.curso.mitocode.examenfinal.documents.DocumentBase;
import com.curso.mitocode.examenfinal.exceptions.ApiException;
import com.curso.mitocode.examenfinal.exceptions.ErrorHandler;
import com.curso.mitocode.examenfinal.repositories.IRepositorioGenerico;
import com.curso.mitocode.examenfinal.services.IServicioGenerico;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ServiceGenericoImpl<T extends DocumentBase, ID extends String> implements IServicioGenerico<T, ID> {

    protected IRepositorioGenerico<T, ID> repository;

    public ServiceGenericoImpl(IRepositorioGenerico<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Flux<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ApiException("registro no encontrado")));
    }

    @Override
    public Mono<T> save(T t) {
        return repository.save(t);
    }

    @Override
    public Mono<T> update(T t, ID id) {
        return findById(id)
                .flatMap(it -> {
                    t.setId(id);
                    return repository.save(t);
                });

    }

    @Override
    public Mono<Void> delete(ID id) {
        return findById(id)
                .flatMap(repository::delete);
    }

    @Override
    public IRepositorioGenerico<T, ID> getRepository() {
        return repository;
    }
}
