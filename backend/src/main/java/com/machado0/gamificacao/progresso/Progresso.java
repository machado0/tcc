package com.machado0.gamificacao.progresso;

import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "progresso_aula_usuario")
public class Progresso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @ManyToOne @JoinColumn(name = "aula_id")
    private Aula aula;
    private int percentualConclusao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
