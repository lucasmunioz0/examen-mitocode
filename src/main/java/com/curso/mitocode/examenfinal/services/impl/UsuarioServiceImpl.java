package com.curso.mitocode.examenfinal.services.impl;

import com.curso.mitocode.examenfinal.repositories.IRolRepository;
import com.curso.mitocode.examenfinal.repositories.IUsuarioRepository;
import com.curso.mitocode.examenfinal.security.documents.Rol;
import com.curso.mitocode.examenfinal.security.documents.Usuario;
import com.curso.mitocode.examenfinal.services.IUsuarioService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioServiceImpl extends ServiceGenericoImpl<Usuario, String> implements IUsuarioService {

    private final IRolRepository rolRepository;

    public UsuarioServiceImpl(IUsuarioRepository repository,
                              IRolRepository rolRepository) {
        super(repository);
        this.rolRepository = rolRepository;
    }

    @Override
    public Mono<Usuario> findByUsername(String username) {
        return getRepository().findByUsername(username)
                .flatMap(u -> Flux.fromIterable(u.getRoles())
                        .flatMap(r -> rolRepository.findById(r.getId()))
                                .collectList()
                                .map(roles -> new Usuario(username, u.getPassword(), roles))
                );
    }

    @Override
    public IUsuarioRepository getRepository() {
        return (IUsuarioRepository) super.getRepository();
    }
}
