package com.machado0.gamificacao.configuracao;

public record ConfiguracaoUsuarioDTO(
        String matricula, String escola, String turma, String emailResponsaveis,
        String tema, String tamanhoFonte, boolean altoContraste, boolean efeitosSonoros,
        boolean animacoesGamificacao, boolean notificarTarefas, boolean notificarProvas,
        boolean resumoSemanalPais) {
    static ConfiguracaoUsuarioDTO from(ConfiguracaoUsuario c) {
        return new ConfiguracaoUsuarioDTO(c.getMatricula(), c.getEscola(), c.getTurma(),
                c.getEmailResponsaveis(), c.getTema(), c.getTamanhoFonte(), c.isAltoContraste(),
                c.isEfeitosSonoros(), c.isAnimacoesGamificacao(), c.isNotificarTarefas(),
                c.isNotificarProvas(), c.isResumoSemanalPais());
    }
}
