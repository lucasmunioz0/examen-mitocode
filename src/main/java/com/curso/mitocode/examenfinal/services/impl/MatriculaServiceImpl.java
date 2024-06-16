package com.curso.mitocode.examenfinal.services.impl;

import com.curso.mitocode.examenfinal.documents.Matricula;
import com.curso.mitocode.examenfinal.documents.dtos.MatriculaDto;
import com.curso.mitocode.examenfinal.exceptions.ApiException;
import com.curso.mitocode.examenfinal.repositories.ICursoRepository;
import com.curso.mitocode.examenfinal.repositories.IEstudianteRepository;
import com.curso.mitocode.examenfinal.repositories.IMatriculaRepository;
import com.curso.mitocode.examenfinal.services.IMatriculaService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MatriculaServiceImpl extends ServiceGenericoImpl<Matricula, String> implements IMatriculaService {

    private final IEstudianteRepository estudianteRepository;
    private final ICursoRepository cursoRepository;

    public MatriculaServiceImpl(IMatriculaRepository repository,
                                IEstudianteRepository estudianteRepository,
                                ICursoRepository cursoRepository) {
        super(repository);
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public Flux<Matricula> findAll() {
        return repository.findAll()
                .flatMap(this::buildMatricula);
    }

    @Override
    public Mono<Matricula> findById(String s) {
        return repository.findById(s)
                .flatMap(this::buildMatricula)
                .switchIfEmpty(Mono.error(new ApiException("registro no encontrado")));
    }

    private Mono<Matricula> buildMatricula(Matricula matricula) {
        return Mono.just(matricula)
                .flatMap(m ->
                    estudianteRepository.findById(m.getEstudiante().getId())
                            .map(e -> {
                                m.setEstudiante(e);
                                return m;
                            })
                )
                .flatMap(m ->
                    Flux.fromIterable(m.getCursos())
                            .flatMap(c -> cursoRepository.findById(c.getId()))
                            .collectList()
                            .map(cursos -> {
                                m.setCursos(cursos);
                                return m;
                            })
                );
    }
}
