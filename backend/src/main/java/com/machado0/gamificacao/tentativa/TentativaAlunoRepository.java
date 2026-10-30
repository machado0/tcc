package com.machado0.gamificacao.tentativa;

import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TentativaAlunoRepository extends JpaRepository<TentativaAluno, Long> {
    boolean existsByUsuarioAndQuestaoAndAcertouTrue(Usuario usuario, Questao questao);
    List<TentativaAluno> findByUsuarioAndAcertouTrue(Usuario usuario);
    List<TentativaAluno> findByUsuarioAndDataTentativaGreaterThanEqualAndDataTentativaLessThan(
            Usuario usuario, LocalDateTime inicio, LocalDateTime fim);
    long countByUsuarioAndAcertouTrue(Usuario usuario);
    long countByUsuario(Usuario usuario);
}
