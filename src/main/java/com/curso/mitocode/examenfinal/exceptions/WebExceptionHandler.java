package com.curso.mitocode.examenfinal.exceptions;

import com.curso.mitocode.examenfinal.exceptions.dtos.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    public WebExceptionHandler(ErrorAttributes errorAttributes,
                               WebProperties.Resources resources,
                               ApplicationContext applicationContext,
                               ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest req) {
        Map<String, Object> generalError = getErrorAttributes(req, ErrorAttributeOptions.defaults());
        ApiError customError = new ApiError();

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        int statusCode = Integer.parseInt(generalError.get("status").toString());
        Throwable error = getError(req);

        customError.setMessage(error.getMessage());

        switch (statusCode) {
            case 400, 422 -> {
                customError.setCode(HttpStatus.BAD_REQUEST.value());
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            case 404 -> {
                customError.setCode(HttpStatus.NOT_FOUND.value());
                httpStatus = HttpStatus.NOT_FOUND;
            }
            case 401, 403 -> {
                customError.setCode(HttpStatus.UNAUTHORIZED.value());
                httpStatus = HttpStatus.UNAUTHORIZED;
            }
            case 500 -> {
                customError.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            default -> {
                customError.setCode(HttpStatus.I_AM_A_TEAPOT.value());
                httpStatus = HttpStatus.I_AM_A_TEAPOT;
            }
        }

        return ServerResponse.status(httpStatus)
                .bodyValue(customError);
    }
}
