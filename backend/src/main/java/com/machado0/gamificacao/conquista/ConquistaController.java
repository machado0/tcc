package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conquistas")
@PreAuthorize("hasRole('ALUNO')")
public class ConquistaController {
    private final ConquistaConsultaService service;

    public ConquistaController(ConquistaConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConquistaResponseDTO> listar(@AuthenticationPrincipal Usuario aluno) {
        return service.listar(aluno);
    }
}
