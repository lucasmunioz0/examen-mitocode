package com.curso.mitocode.examenfinal.services;

import com.curso.mitocode.examenfinal.documents.Estudiante;
import org.springframework.data.domain.Sort.Direction;
import reactor.core.publisher.Flux;

public interface IEstudianteService extends IServicioGenerico<Estudiante, String> {

    Flux<Estudiante> findAllByEdad(Direction direction);
}
