package com.machado0.gamificacao.estudo;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoes-estudo")
@PreAuthorize("hasRole('ALUNO')")
public class SessaoEstudoController {
    private final SessaoEstudoService service;

    public SessaoEstudoController(SessaoEstudoService service) {
        this.service = service;
    }

    @PostMapping
    public SessaoEstudoResponse iniciar(@AuthenticationPrincipal Usuario usuario,
                                         @RequestBody IniciarSessaoRequest request) {
        return service.iniciar(usuario, request);
    }

    @PostMapping("/{id}/finalizar")
    public SessaoEstudoResponse finalizar(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) {
        return service.finalizar(usuario, id);
    }
}
