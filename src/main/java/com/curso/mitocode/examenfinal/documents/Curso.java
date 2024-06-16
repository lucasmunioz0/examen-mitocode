package com.curso.mitocode.examenfinal.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("cursos")
public class Curso extends DocumentBase {

    private String nombre;
    private String sigla;
    private Boolean estado;
}
