package com.machado0.gamificacao.alternativa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.machado0.gamificacao.questao.Questao;
import java.util.List;

public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
    List<Alternativa> findByQuestaoOrderByIdAsc(Questao questao);
}
