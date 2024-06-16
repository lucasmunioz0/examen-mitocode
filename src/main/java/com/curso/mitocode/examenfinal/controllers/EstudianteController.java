package com.curso.mitocode.examenfinal.controllers;

import com.curso.mitocode.examenfinal.documents.Estudiante;
import com.curso.mitocode.examenfinal.documents.dtos.EstudianteDto;
import com.curso.mitocode.examenfinal.exceptions.ErrorHandler;
import com.curso.mitocode.examenfinal.services.IEstudianteService;
import com.curso.mitocode.examenfinal.validator.RequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/v2/estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private final ModelMapper modelMapper;
    private final RequestValidator requestValidator;

    public EstudianteController(IEstudianteService estudianteService,
                                ModelMapper modelMapper,
                                RequestValidator requestValidator) {
        this.estudianteService = estudianteService;
        this.modelMapper = modelMapper;
        this.requestValidator = requestValidator;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Estudiante>>> findAll(@RequestParam(name = "sort", defaultValue = "asc") String direction){
        return Mono.just(ResponseEntity.ok()
                .body(estudianteService.findAllByEdad(getDirection(direction))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Estudiante>> findById(@PathVariable String id){
        return estudianteService.findById(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<EstudianteDto>> save(@RequestBody EstudianteDto estudianteDto, ServerHttpRequest request){
        return requestValidator.validate(estudianteDto)
                .map(this::convertToEntity)
                .flatMap(estudianteService::save)
                .map(this::convertToDto)
                .map(es -> ResponseEntity.created(URI.create(request.getURI() + "/" + es.getId()))
                        .body(es))
                .onErrorResume(error -> ErrorHandler.handler(error)
                        .map(e -> ResponseEntity.status(e.statusCode().value())
                                .build()));

    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EstudianteDto>> update(@PathVariable String id, @RequestBody EstudianteDto estudianteDto, ServerHttpRequest request){
        return requestValidator.validate(estudianteDto)
                .map(this::convertToEntity)
                .flatMap(e -> estudianteService.update(e, id))
                .map(this::convertToDto)
                .map(es -> ResponseEntity.created(URI.create(request.getURI() + "/" + es.getId()))
                        .body(es));

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id){
        return estudianteService.delete(id)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(error -> ErrorHandler.handler(error)
                        .map(e -> ResponseEntity.status(e.statusCode().value())
                                .build()));
    }

    private Direction getDirection(String direction){
        if(direction != null){
            return Direction.fromString(direction);
        }
        return null;
    }


    private EstudianteDto convertToDto(Estudiante estudiante){
        return modelMapper.map(estudiante, EstudianteDto.class);
    }

    private Estudiante convertToEntity(EstudianteDto estudianteDto){
        return modelMapper.map(estudianteDto, Estudiante.class);
    }
}
