package com.machado0.gamificacao.missao.usuario;

import com.machado0.gamificacao.missao.TemplateMissao;
import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "missoes_usuario")
public class MissaoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private TemplateMissao template;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_referencia", nullable = false)
    private OffsetDateTime dataReferencia;

    private String texto;

    private int meta;

    private int progresso = 0;

    @Column(name = "concluida", nullable = false)
    private boolean isConcluida;

    private int recompensa;

    private OffsetDateTime dataConclusao;


}
