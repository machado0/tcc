package com.machado0.gamificacao.configuracao;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracaoUsuarioService {
    private final ConfiguracaoUsuarioRepository repository;

    public ConfiguracaoUsuarioService(ConfiguracaoUsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ConfiguracaoUsuarioDTO buscar(Usuario usuario) {
        return ConfiguracaoUsuarioDTO.from(repository.findById(usuario.getId())
                .orElseGet(() -> criarPadrao(usuario)));
    }

    @Transactional
    public ConfiguracaoUsuarioDTO atualizar(Usuario usuario, ConfiguracaoUsuarioDTO dto) {
        ConfiguracaoUsuario config = repository.findById(usuario.getId())
                .orElseGet(() -> criarPadrao(usuario));
        config.setMatricula(dto.matricula()); config.setEscola(dto.escola()); config.setTurma(dto.turma());
        config.setEmailResponsaveis(dto.emailResponsaveis()); config.setTema(dto.tema());
        config.setTamanhoFonte(dto.tamanhoFonte()); config.setAltoContraste(dto.altoContraste());
        config.setEfeitosSonoros(dto.efeitosSonoros()); config.setAnimacoesGamificacao(dto.animacoesGamificacao());
        config.setNotificarTarefas(dto.notificarTarefas()); config.setNotificarProvas(dto.notificarProvas());
        config.setResumoSemanalPais(dto.resumoSemanalPais());
        return ConfiguracaoUsuarioDTO.from(repository.save(config));
    }

    private ConfiguracaoUsuario criarPadrao(Usuario usuario) {
        ConfiguracaoUsuario config = new ConfiguracaoUsuario();
        config.setUsuario(usuario);
        config.setUsuarioId(usuario.getId());
        return repository.save(config);
    }
}
