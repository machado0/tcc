package com.machado0.gamificacao.estudo;

import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessoes_estudo")
public class SessaoEstudo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private Aula aula;

    @Column(nullable = false)
    private LocalDateTime inicio;

    private LocalDateTime fim;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    @Column(nullable = false)
    private boolean concluida;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
}
