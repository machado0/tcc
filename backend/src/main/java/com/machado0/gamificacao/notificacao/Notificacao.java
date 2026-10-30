package com.machado0.gamificacao.notificacao;

import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity @Table(name = "notificacoes")
public class Notificacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false) private Usuario usuario;
    private String titulo;
    private String mensagem;
    private String tipo;
    private boolean lida;
    @Column(name = "criada_em") private LocalDateTime criadaEm;
    private String rota;
}
