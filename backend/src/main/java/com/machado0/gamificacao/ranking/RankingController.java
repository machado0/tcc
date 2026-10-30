package com.machado0.gamificacao.ranking;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@PreAuthorize("hasRole('ALUNO')")
public class RankingController {
    private final RankingService service;

    public RankingController(RankingService service) {
        this.service = service;
    }

    @GetMapping
    public RankingResponseDTO ranking(@AuthenticationPrincipal Usuario aluno) {
        return service.buscar(aluno);
    }
}
