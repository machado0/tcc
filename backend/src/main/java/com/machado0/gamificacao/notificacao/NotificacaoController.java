package com.machado0.gamificacao.notificacao;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notificacoes")
public class NotificacaoController {
    private final NotificacaoService service;
    public NotificacaoController(NotificacaoService service) { this.service = service; }
    @GetMapping public List<NotificacaoDTO> listar(@AuthenticationPrincipal Usuario usuario) { return service.listar(usuario); }
    @PatchMapping("/{id}/lida") public List<NotificacaoDTO> marcarLida(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) { return service.marcarLida(usuario, id); }
    @PostMapping("/marcar-todas-lidas") public List<NotificacaoDTO> marcarTodas(@AuthenticationPrincipal Usuario usuario) { return service.marcarTodas(usuario); }
}
