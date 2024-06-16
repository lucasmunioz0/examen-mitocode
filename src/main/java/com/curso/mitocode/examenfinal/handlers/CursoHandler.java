package com.curso.mitocode.examenfinal.handlers;

import com.curso.mitocode.examenfinal.documents.Curso;
import com.curso.mitocode.examenfinal.documents.dtos.CursoDto;
import com.curso.mitocode.examenfinal.exceptions.ErrorHandler;
import com.curso.mitocode.examenfinal.services.ICursoService;
import com.curso.mitocode.examenfinal.validator.RequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CursoHandler {

    private final ICursoService cursoService;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public CursoHandler(ICursoService cursoService,
                        ModelMapper modelMapper,
                        RequestValidator requestValidator) {
        this.cursoService = cursoService;
        this.modelMapper = modelMapper;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok().body(cursoService
                        .findAll()
                        .map(this::convertToDto), CursoDto.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        return ServerResponse
                .ok().body(cursoService.findById(request.pathVariable("id"))
                        .map(this::convertToDto), CursoDto.class)
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> save(ServerRequest request){
        return request.bodyToMono(CursoDto.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToEntity)
                .flatMap(cursoService::save)
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> update(ServerRequest request){
        return request.bodyToMono(CursoDto.class)
                .map(this::convertToEntity)
                .flatMap(e -> cursoService.update(e, request.pathVariable("id")))
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);

    }

    public Mono<ServerResponse> delete(ServerRequest request){
        return cursoService.delete(request.pathVariable("id"))
                .then(ServerResponse.noContent().build())
                .onErrorResume(ErrorHandler::handler);

    }

    private CursoDto convertToDto(Curso curso){
        return modelMapper.map(curso, CursoDto.class);
    }

    private Curso convertToEntity(CursoDto cursoDto){
        return modelMapper.map(cursoDto, Curso.class);
    }
}
