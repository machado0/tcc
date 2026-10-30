package com.machado0.gamificacao.notificacao;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.*;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioOrderByCriadaEmDesc(Usuario usuario);
}
