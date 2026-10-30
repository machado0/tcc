package com.machado0.gamificacao.conquista.usuario;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConquistaUsuarioRepository extends JpaRepository<ConquistaUsuario, Long> {
    List<ConquistaUsuario> findByUsuarioOrderByDataDesbloqueioDesc(Usuario usuario);
    boolean existsByUsuarioIdAndConquistaIdAndDesbloqueadaTrue(Long usuarioId, Long conquistaId);
    java.util.Optional<ConquistaUsuario> findByUsuarioAndConquista(Usuario usuario, com.machado0.gamificacao.conquista.Conquista conquista);
}
