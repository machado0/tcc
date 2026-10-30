package com.machado0.gamificacao.progresso;

import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.aula.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressoRepository extends JpaRepository<Progresso, Long> {
    List<Progresso> findByUsuario(Usuario usuario);
    java.util.Optional<Progresso> findByUsuarioAndAula(Usuario usuario, Aula aula);
    long countByUsuarioAndPercentualConclusaoGreaterThanEqual(Usuario usuario, int percentual);
}
