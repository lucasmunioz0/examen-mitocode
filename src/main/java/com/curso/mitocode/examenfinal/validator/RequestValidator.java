package com.curso.mitocode.examenfinal.validator;

import com.curso.mitocode.examenfinal.exceptions.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T t){
        if(t == null){
            return Mono.error(new ApiException("Body may not be null"));
        }

        Set<ConstraintViolation<T>> constraints = validator.validate(t);

        if(constraints == null || constraints.isEmpty()){
            return Mono.just(t);
        }

        return Mono.error(new ConstraintViolationException(constraints));
    }
}
