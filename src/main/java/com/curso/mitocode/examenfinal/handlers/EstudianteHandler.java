package com.curso.mitocode.examenfinal.handlers;

import com.curso.mitocode.examenfinal.documents.Estudiante;
import com.curso.mitocode.examenfinal.documents.dtos.EstudianteDto;
import com.curso.mitocode.examenfinal.exceptions.ErrorHandler;
import com.curso.mitocode.examenfinal.services.IEstudianteService;
import com.curso.mitocode.examenfinal.validator.RequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class EstudianteHandler {

    private final IEstudianteService estudianteService;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public EstudianteHandler(IEstudianteService estudianteService,
                             ModelMapper modelMapper,
                             RequestValidator requestValidator) {
        this.estudianteService = estudianteService;
        this.modelMapper = modelMapper;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok().body(estudianteService
                        .findAllByEdad(getDirection(request))
                        .map(this::convertToDto), EstudianteDto.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return ServerResponse
                .ok().body(estudianteService.findById(request.pathVariable("id"))
                        .map(this::convertToDto), EstudianteDto.class)
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(EstudianteDto.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToEntity)
                .flatMap(estudianteService::save)
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return request.bodyToMono(EstudianteDto.class)
                .map(this::convertToEntity)
                .flatMap(e -> estudianteService.update(e, request.pathVariable("id")))
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);

    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return estudianteService.delete(request.pathVariable("id"))
                .then(ServerResponse.noContent().build())
                .onErrorResume(ErrorHandler::handler);

    }

    private Direction getDirection(ServerRequest request) {
        if (request.queryParam("sort").isPresent()) {
            return Direction.fromString(request.queryParam("sort").orElse("ASC"));
        }
        return null;
    }


    private EstudianteDto convertToDto(Estudiante estudiante) {
        return modelMapper.map(estudiante, EstudianteDto.class);
    }

    private Estudiante convertToEntity(EstudianteDto estudianteDto) {
        return modelMapper.map(estudianteDto, Estudiante.class);
    }
}
