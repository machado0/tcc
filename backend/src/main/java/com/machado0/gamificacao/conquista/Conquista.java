package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.modulo.Modulo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conquistas")
public class Conquista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    public String getNome() {
        return titulo;
    }

    public void setNome(String nome) {
        this.titulo = nome;
    }

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String icone;

    @Column(nullable = false)
    private int meta;

    @Column(name = "recompensa_xp", nullable = false)
    private int recompensaXp;

    @Enumerated(EnumType.STRING)
    private TipoConquista tipo;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;
}
