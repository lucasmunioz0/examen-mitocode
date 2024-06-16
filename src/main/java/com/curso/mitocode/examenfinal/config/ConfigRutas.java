package com.curso.mitocode.examenfinal.config;

import com.curso.mitocode.examenfinal.handlers.AuthHandler;
import com.curso.mitocode.examenfinal.handlers.CursoHandler;
import com.curso.mitocode.examenfinal.handlers.EstudianteHandler;
import com.curso.mitocode.examenfinal.handlers.MatriculaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ConfigRutas {

    @Bean
    public RouterFunction<ServerResponse> routerEstudiante(EstudianteHandler estudianteHandler) {
        return route(GET("/v1/estudiantes"), estudianteHandler::findAll)
                .andRoute(GET("/v1/estudiantes/{id}"), estudianteHandler::findById)
                .andRoute(POST("/v1/estudiantes"), estudianteHandler::save)
                .andRoute(PUT("/v1/estudiantes/{id}"), estudianteHandler::update)
                .andRoute(DELETE("/v1/estudiantes/{id}"), estudianteHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routerCurso(CursoHandler cursoHandler) {
        return route(GET("/v1/cursos"), cursoHandler::findAll)
                .andRoute(GET("/v1/cursos/{id}"), cursoHandler::findById)
                .andRoute(POST("/v1/cursos"), cursoHandler::save)
                .andRoute(PUT("/v1/cursos/{id}"), cursoHandler::update)
                .andRoute(DELETE("/v1/cursos/{id}"), cursoHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routerMatricula(MatriculaHandler matriculaHandler) {
        return route(GET("/v1/matriculas"), matriculaHandler::findAll)
                .andRoute(GET("/v1/matriculas/{id}"), matriculaHandler::findById)
                .andRoute(POST("/v1/matriculas"), matriculaHandler::save)
                .andRoute(PUT("/v1/matriculas/{id}"), matriculaHandler::update)
                .andRoute(DELETE("/v1/matriculas/{id}"), matriculaHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routerLogin(AuthHandler authHandler) {
        return route(POST("/login"), authHandler::login);
    }
}
