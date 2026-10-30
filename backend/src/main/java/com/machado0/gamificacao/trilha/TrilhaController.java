package com.machado0.gamificacao.trilha;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trilhas")
@PreAuthorize("hasAnyRole('ALUNO', 'PROFESSOR')")
public class TrilhaController {
    private final TrilhaService service;

    public TrilhaController(TrilhaService service) {
        this.service = service;
    }

    @GetMapping("/matematica")
    public TrilhaResponseDTO matematica(@AuthenticationPrincipal Usuario usuario) {
        return service.buscarMatematica(usuario);
    }
}
