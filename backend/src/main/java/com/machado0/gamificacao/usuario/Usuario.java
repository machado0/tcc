package com.machado0.gamificacao.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 250)
    private String nome;

    @NotNull
    @Column(nullable = false, length = 250)
    private String username;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil = Perfil.ALUNO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario professorResponsavel;

    @Column(nullable = false)
    private int xp = 0;

    @Column(nullable = false)
    private int nivel = 1;

    @Column(nullable = false)
    private int vidas = 3;

    @Column(nullable = false)
    private int moedas = 0;

    @Column(nullable = false)
    private int streak = 0;

    @Column(name = "best_streak", nullable = false)
    private int bestStreak = 0;

    @Column(name = "ultimo_acesso")
    private java.time.LocalDateTime ultimoAcesso;

    @Column(name = "data_atualizacao")
    private java.time.LocalDateTime dataAtualizacao;

    @NotNull
    @Column(name = "titulo_honra", nullable = false, length = 80)
    private String tituloHonra;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private OffsetDateTime dataCriacao;

}
