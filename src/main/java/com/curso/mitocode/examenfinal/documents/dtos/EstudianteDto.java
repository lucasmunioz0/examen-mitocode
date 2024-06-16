package com.curso.mitocode.examenfinal.documents.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EstudianteDto {

    private String id;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String nombre;

    @NotEmpty
    @Size(min = 3, max = 20)
    private String apellido;

    @NotEmpty
    @Size(min = 6, max = 10)
    private String documento;

    @NotNull
    @Positive
    private Integer edad;
}
