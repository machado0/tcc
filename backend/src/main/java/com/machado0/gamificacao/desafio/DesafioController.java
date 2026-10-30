package com.machado0.gamificacao.desafio;

import com.machado0.gamificacao.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/desafios")
@PreAuthorize("hasRole('ALUNO')")
public class DesafioController {
    private final DesafioService service;
    public DesafioController(DesafioService service) {
        this.service = service;
    }
    @PostMapping("/submeter")
    public FeedbackDTO submeter(@AuthenticationPrincipal Usuario aluno,
                                @Valid @RequestBody SubmeterDesafioDTO request) {
        return service.processarSubmissao(aluno, request);
    }
}
