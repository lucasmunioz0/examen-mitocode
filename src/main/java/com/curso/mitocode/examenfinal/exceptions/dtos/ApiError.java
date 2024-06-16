package com.curso.mitocode.examenfinal.exceptions.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private Integer code;
    private String message;
    private List<ValidationDTO> details;
}
