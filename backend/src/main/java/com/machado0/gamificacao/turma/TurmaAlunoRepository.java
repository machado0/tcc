package com.machado0.gamificacao.turma;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurmaAlunoRepository extends JpaRepository<TurmaAluno, Long> {
    Optional<TurmaAluno> findFirstByUsuario(Usuario usuario);
    List<TurmaAluno> findByTurma(Turma turma);
}
