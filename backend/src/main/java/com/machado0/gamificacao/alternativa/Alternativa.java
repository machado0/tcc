package com.machado0.gamificacao.alternativa;

import com.machado0.gamificacao.questao.Questao;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alternativas")
public class Alternativa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String texto;
    private String justificativa;
    @Column(nullable = false)
    private boolean correta;
    @ManyToOne @JoinColumn(name = "questao_id")
    private Questao questao;
}
