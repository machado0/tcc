package com.machado0.gamificacao.configuracao;

import com.machado0.gamificacao.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "configuracoes_usuario")
public class ConfiguracaoUsuario {
    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String matricula;
    private String escola;
    private String turma;
    @Column(name = "email_responsaveis") private String emailResponsaveis;
    private String tema = "sistema";
    @Column(name = "tamanho_fonte") private String tamanhoFonte = "padrao";
    @Column(name = "alto_contraste") private boolean altoContraste;
    @Column(name = "efeitos_sonoros") private boolean efeitosSonoros = true;
    @Column(name = "animacoes_gamificacao") private boolean animacoesGamificacao = true;
    @Column(name = "notificar_tarefas") private boolean notificarTarefas = true;
    @Column(name = "notificar_provas") private boolean notificarProvas = true;
    @Column(name = "resumo_semanal_pais") private boolean resumoSemanalPais;
}
