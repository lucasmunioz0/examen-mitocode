package com.curso.mitocode.examenfinal.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IRepositorioGenerico<T, ID> extends ReactiveMongoRepository<T, ID> {
}
