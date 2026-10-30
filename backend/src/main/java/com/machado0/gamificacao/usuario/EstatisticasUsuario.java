package com.machado0.gamificacao.usuario;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "estatisticas_usuario")
public class EstatisticasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "nivel", nullable = false)
    private int nivel = 1;

    @Column(name = "xp_atual", nullable = false)
    private long xpAtual = 0;

    @Column(name = "xp_proximo_nivel", nullable = false)
    private long xpProximoNivel = 500;

    @Column(name = "xp_hoje", nullable = false)
    private int xpHoje = 0;

    @Column(name = "sequencia_dias", nullable = false)
    private int sequenciaDias = 0;

    @Column(name = "melhor_sequencia_dias", nullable = false)
    private int melhorSequenciaDias = 0;

    @Column(nullable = false)
    private int moedas = 0;

    @Column(nullable = false)
    private int vidas = 3;

    @Column(name = "ultima_data_estudo")
    private OffsetDateTime ultimaDataEstudo;

    @Column(name = "ultima_vida_perdida_em")
    private OffsetDateTime ultimaVidaPerdidaEm;

}
