package com.machado0.gamificacao.estudo;

import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.aula.AulaRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class SessaoEstudoService {
    private final SessaoEstudoRepository repository;
    private final AulaRepository aulaRepository;

    public SessaoEstudoService(SessaoEstudoRepository repository, AulaRepository aulaRepository) {
        this.repository = repository;
        this.aulaRepository = aulaRepository;
    }

    @Transactional
    public SessaoEstudoResponse iniciar(Usuario usuario, IniciarSessaoRequest request) {
        validarAluno(usuario);
        Aula aula = request.aulaId() == null ? null : aulaRepository.findById(request.aulaId())
                .orElseThrow(() -> new IllegalArgumentException("Aula não encontrada"));
        if (aula != null) {
            var sessaoAberta = repository
                    .findFirstByUsuarioAndAula_IdAndConcluidaFalseOrderByInicioAsc(usuario, aula.getId());
            if (sessaoAberta.isPresent()) {
                return paraResponse(sessaoAberta.get());
            }
        }
        SessaoEstudo sessao = new SessaoEstudo();
        sessao.setUsuario(usuario);
        sessao.setAula(aula);
        sessao.setInicio(LocalDateTime.now());
        sessao.setDataCriacao(LocalDateTime.now());
        sessao.setConcluida(false);
        return paraResponse(repository.save(sessao));
    }

    @Transactional
    public SessaoEstudoResponse finalizar(Usuario usuario, Long id) {
        validarAluno(usuario);
        SessaoEstudo sessao = repository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        if (sessao.isConcluida()) {
            return paraResponse(sessao);
        }
        LocalDateTime fim = LocalDateTime.now();
        long minutos = Math.max(1, Duration.between(sessao.getInicio(), fim).toMinutes());
        if (minutos > 720) {
            throw new IllegalArgumentException("A duração da sessão excede o limite permitido");
        }
        sessao.setFim(fim);
        sessao.setDuracaoMinutos((int) minutos);
        sessao.setConcluida(true);
        return paraResponse(repository.save(sessao));
    }

    private void validarAluno(Usuario usuario) {
        if (usuario == null || usuario.getPerfil() != Perfil.ALUNO) {
            throw new IllegalArgumentException("Apenas alunos podem registrar sessões");
        }
    }

    private SessaoEstudoResponse paraResponse(SessaoEstudo sessao) {
        return new SessaoEstudoResponse(
                sessao.getId(),
                sessao.getAula() == null ? null : sessao.getAula().getId(),
                sessao.getInicio(),
                sessao.getFim(),
                sessao.getDuracaoMinutos(),
                sessao.isConcluida());
    }
}
