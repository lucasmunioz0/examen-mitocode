package com.curso.mitocode.examenfinal.security.documents;

import com.curso.mitocode.examenfinal.documents.DocumentBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rol extends DocumentBase {

    private String nombre;
}
