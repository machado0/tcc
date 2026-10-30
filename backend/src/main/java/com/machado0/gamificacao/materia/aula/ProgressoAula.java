package com.machado0.gamificacao.materia.aula;

import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "progresso_aula_usuario")
public class ProgressoAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @Enumerated(EnumType.STRING)
    private StatusAula status;

    @Column(name = "data_conclusao")
    private OffsetDateTime dataConclusao;

}
