package com.curso.mitocode.examenfinal.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("estudiantes")
public class Estudiante extends DocumentBase{
    private String nombre;
    private String apellido;
    private String documento;
    private Integer edad;
}
