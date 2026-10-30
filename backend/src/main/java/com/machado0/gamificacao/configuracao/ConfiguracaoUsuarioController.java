package com.machado0.gamificacao.configuracao;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configuracoes")
public class ConfiguracaoUsuarioController {
    private final ConfiguracaoUsuarioService service;
    public ConfiguracaoUsuarioController(ConfiguracaoUsuarioService service) { this.service = service; }
    @GetMapping public ConfiguracaoUsuarioDTO buscar(@AuthenticationPrincipal Usuario usuario) { return service.buscar(usuario); }
    @PutMapping public ConfiguracaoUsuarioDTO atualizar(@AuthenticationPrincipal Usuario usuario, @RequestBody ConfiguracaoUsuarioDTO dto) {
        return service.atualizar(usuario, dto);
    }
}
