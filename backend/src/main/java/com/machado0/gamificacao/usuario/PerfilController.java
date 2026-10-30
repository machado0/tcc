package com.machado0.gamificacao.usuario;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/perfil")
@PreAuthorize("hasAnyRole('ALUNO', 'PROFESSOR')")
public class PerfilController {

    private final UsuarioRepository repository;

    public PerfilController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public PerfilUsuarioDTO buscar(@AuthenticationPrincipal Usuario usuario) {
        return paraDto(usuario);
    }

    @PutMapping
    public PerfilUsuarioDTO atualizar(
            @AuthenticationPrincipal Usuario usuario,
            @Valid @RequestBody PerfilUsuarioDTO request) {
        usuario.setNome(request.nome().trim());
        usuario.setUsername(request.username().trim());
        usuario.setEmail(request.email().trim().toLowerCase());
        usuario.setTituloHonra(request.tituloHonra() == null || request.tituloHonra().isBlank()
                ? usuario.getTituloHonra() : request.tituloHonra().trim());
        return paraDto(repository.save(usuario));
    }

    private PerfilUsuarioDTO paraDto(Usuario usuario) {
        return new PerfilUsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getUsername(),
                usuario.getEmail(), usuario.getTituloHonra());
    }
}
