package com.machado0.gamificacao.dashboard;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alunos")
@PreAuthorize("hasRole('ALUNO')")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public DashboardStatsDTO dashboard(@AuthenticationPrincipal Usuario aluno) {
        return service.buscar(aluno);
    }
}
