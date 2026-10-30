package com.machado0.gamificacao.questao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.machado0.gamificacao.aula.Aula;
import java.util.List;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {
    List<Questao> findByAulaOrderByIdAsc(Aula aula);
}
