package com.machado0.gamificacao.tentativa;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tentativas_aluno")
public class TentativaAluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_escolhida_id", nullable = false)
    private Alternativa alternativaEscolhida;
    @Column(nullable = false)
    private boolean acertou;
    @Column(nullable = false)
    private LocalDateTime dataTentativa;
}
