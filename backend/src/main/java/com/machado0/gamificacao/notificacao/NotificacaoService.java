package com.machado0.gamificacao.notificacao;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService {
    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<NotificacaoDTO> listar(Usuario usuario) {
        return repository.findByUsuarioOrderByCriadaEmDesc(usuario).stream().map(NotificacaoDTO::from).toList();
    }

    @Transactional
    public List<NotificacaoDTO> marcarLida(Usuario usuario, Long id) {
        Notificacao n = repository.findById(id).filter(item -> item.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Notificacao nao encontrada"));
        n.setLida(true);
        repository.save(n);
        return listar(usuario);
    }

    @Transactional
    public List<NotificacaoDTO> marcarTodas(Usuario usuario) {
        repository.findByUsuarioOrderByCriadaEmDesc(usuario).forEach(n -> n.setLida(true));
        return listar(usuario);
    }
}
