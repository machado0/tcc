package com.machado0.gamificacao.missao.usuario;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface MissaoUsuarioRepository extends JpaRepository<MissaoUsuario, Long> {
    List<MissaoUsuario> findByUsuarioAndDataReferenciaGreaterThanEqualAndDataReferenciaLessThan(
            Usuario usuario, OffsetDateTime inicio, OffsetDateTime fim);
}
