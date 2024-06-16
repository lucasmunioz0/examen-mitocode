package com.curso.mitocode.examenfinal.repositories;

import com.curso.mitocode.examenfinal.documents.Estudiante;
import reactor.core.publisher.Flux;

public interface IEstudianteRepository extends IRepositorioGenerico<Estudiante, String> {

    Flux<Estudiante> findAllByOrderByEdadAsc();

    Flux<Estudiante> findAllByOrderByEdadDesc();
}
