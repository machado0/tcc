package com.machado0.gamificacao.conquista.usuario;

import com.machado0.gamificacao.conquista.Conquista;
import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "conquistas_usuario")
public class ConquistaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conquista_id", nullable = false)
    private Conquista conquista;

    private int progresso;

    @Column(name = "desbloqueada", nullable = false)
    private boolean isDesbloqueada;

    @Column(name = "data_desbloqueio")
    private LocalDateTime dataDesbloqueio;
}
