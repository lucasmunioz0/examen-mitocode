package com.curso.mitocode.examenfinal.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("matriculas")
public class Matricula extends DocumentBase{

    private LocalDateTime fecha;
    private Estudiante estudiante;
    private List<Curso> cursos;
    private Boolean estado;
}
