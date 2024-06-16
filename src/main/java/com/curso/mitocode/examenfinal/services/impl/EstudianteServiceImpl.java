package com.curso.mitocode.examenfinal.services.impl;

import com.curso.mitocode.examenfinal.documents.Estudiante;
import com.curso.mitocode.examenfinal.repositories.IEstudianteRepository;
import com.curso.mitocode.examenfinal.services.IEstudianteService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EstudianteServiceImpl extends ServiceGenericoImpl<Estudiante, String> implements IEstudianteService {

    public EstudianteServiceImpl(IEstudianteRepository repository) {
        super(repository);
    }

    @Override
    public Flux<Estudiante> findAllByEdad(Direction direction) {
        return direction == null? findAll() :
                direction.isAscending()? getRepository().findAllByOrderByEdadAsc() :
                getRepository().findAllByOrderByEdadDesc();
    }

    @Override
    public IEstudianteRepository getRepository() {
        return (IEstudianteRepository) repository;
    }
}
