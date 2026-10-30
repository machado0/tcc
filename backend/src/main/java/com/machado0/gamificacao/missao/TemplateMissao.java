package com.machado0.gamificacao.missao;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "templates_missoes")
public class TemplateMissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMissao tipoMissao;

    @Column(nullable = false)
    private String texto;

    @ElementCollection
    @CollectionTable(name = "metas", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "metas_possiveis")
    private List<Integer> metasPossiveis;

    @Column(name = "xp_recompensa_base", nullable = false)
    private int xpRecompensaBase;

    private String icone;

    @Column(name = "ativo", nullable = false)
    private boolean isAtivo;

}
