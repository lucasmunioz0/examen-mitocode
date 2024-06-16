package com.curso.mitocode.examenfinal.handlers;

import com.curso.mitocode.examenfinal.documents.Matricula;
import com.curso.mitocode.examenfinal.documents.dtos.MatriculaDto;
import com.curso.mitocode.examenfinal.exceptions.ErrorHandler;
import com.curso.mitocode.examenfinal.services.IMatriculaService;
import com.curso.mitocode.examenfinal.validator.RequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class MatriculaHandler {

    private final IMatriculaService matriculaService;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public MatriculaHandler(IMatriculaService matriculaService,
                            ModelMapper modelMapper,
                            RequestValidator requestValidator) {
        this.matriculaService = matriculaService;
        this.modelMapper = modelMapper;
        this.requestValidator = requestValidator;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok().body(matriculaService
                        .findAll()
                        .map(this::convertToDto), MatriculaDto.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        return ServerResponse
                .ok().body(matriculaService.findById(request.pathVariable("id"))
                        .map(this::convertToDto), MatriculaDto.class)
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> save(ServerRequest request){
        return request.bodyToMono(MatriculaDto.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToEntity)
                .flatMap(matriculaService::save)
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);
    }

    public Mono<ServerResponse> update(ServerRequest request){
        return request.bodyToMono(MatriculaDto.class)
                .map(this::convertToEntity)
                .flatMap(e -> matriculaService.update(e, request.pathVariable("id")))
                .map(this::convertToDto)
                .flatMap(es -> ServerResponse
                        .created(URI.create(request.uri() + "/" + es.getId()))
                        .bodyValue(es))
                .onErrorResume(ErrorHandler::handler);

    }

    public Mono<ServerResponse> delete(ServerRequest request){
        return matriculaService.delete(request.pathVariable("id"))
                .then(ServerResponse.noContent().build())
                .onErrorResume(ErrorHandler::handler);

    }

    private MatriculaDto convertToDto(Matricula matricula){
        return modelMapper.map(matricula, MatriculaDto.class);
    }

    private Matricula convertToEntity(MatriculaDto matriculaDto){
        return modelMapper.map(matriculaDto, Matricula.class);
    }
}
