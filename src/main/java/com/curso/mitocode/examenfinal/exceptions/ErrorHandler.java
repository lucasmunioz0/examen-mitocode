package com.curso.mitocode.examenfinal.exceptions;

import com.curso.mitocode.examenfinal.exceptions.dtos.ApiError;
import com.curso.mitocode.examenfinal.exceptions.dtos.ValidationDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public abstract class ErrorHandler {

    public static Mono<ServerResponse> handler(Throwable error) {
        ApiError customError = new ApiError();

        if(error instanceof ApiException){
            return handleNotFoundException(error, customError);
        }

        if(error instanceof ConstraintViolationException){
            return handleConstrainViolations(error, customError);
        }

        customError.setMessage(error.getCause().getMessage());
        customError.setCode(HttpStatus.BAD_REQUEST.value());
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .bodyValue(customError);
    }

    private static Mono<ServerResponse> handleConstrainViolations(Throwable error, ApiError customError) {
        ConstraintViolationException errorException = (ConstraintViolationException) error;
        customError.setCode(HttpStatus.BAD_REQUEST.value());


        return Flux.fromIterable(errorException.getConstraintViolations())
                .map(p -> new ValidationDTO(p.getPropertyPath().toString(), p.getMessage()))
                .collectList()
                .map(errores -> new ApiError(HttpStatus.BAD_REQUEST.value(), "Campos no válidos", errores))
                .flatMap(apiError -> ServerResponse.status(HttpStatus.BAD_REQUEST.value())
                        .bodyValue(apiError));
    }

    private static Mono<ServerResponse> handleNotFoundException(Throwable error, ApiError customError){
        customError.setMessage(error.getMessage());
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .bodyValue(customError);
    }
}
