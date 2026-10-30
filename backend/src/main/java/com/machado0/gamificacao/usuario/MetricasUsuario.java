package com.machado0.gamificacao.usuario;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "metricas_desempenho")
public class MetricasUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "questoes_respondidas", nullable = false)
    private int questoesRespondidas = 0;

    @Column(name = "questoes_corretas", nullable = false)
    private int questoesCorretas = 0;

    @Column(name = "aulas_concluidas", nullable = false)
    private int aulasConcluidas = 0;

    @Column(name = "tempo_total_estudo_minutos", nullable = false)
    private int estudoTotalMinutos = 0;

}
