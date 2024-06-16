package com.curso.mitocode.examenfinal.documents;


import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class DocumentBase {

    @Id
    @EqualsAndHashCode.Include
    protected String id;
}
