package com.curso.mitocode.examenfinal.services.impl;

import com.curso.mitocode.examenfinal.documents.Curso;
import com.curso.mitocode.examenfinal.repositories.ICursoRepository;
import com.curso.mitocode.examenfinal.services.ICursoService;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl extends ServiceGenericoImpl<Curso, String> implements ICursoService {

    public CursoServiceImpl(ICursoRepository repository) {
        super(repository);
    }
}
