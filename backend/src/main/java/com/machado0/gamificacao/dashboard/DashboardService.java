package com.machado0.gamificacao.dashboard;

import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.tentativa.TentativaAluno;
import com.machado0.gamificacao.tentativa.TentativaAlunoRepository;
import com.machado0.gamificacao.turma.TurmaAluno;
import com.machado0.gamificacao.turma.TurmaAlunoRepository;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.progresso.ProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.machado0.gamificacao.estudo.SessaoEstudo;
import com.machado0.gamificacao.estudo.SessaoEstudoRepository;
import com.machado0.gamificacao.missao.MissaoDiariaService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DashboardService {
    private final TentativaAlunoRepository tentativaRepository;
    private final TurmaAlunoRepository turmaAlunoRepository;
    private final ProgressoRepository progressoRepository;
    private final SessaoEstudoRepository sessaoRepository;
    private final MissaoDiariaService missaoService;

    @Autowired
    public DashboardService(TentativaAlunoRepository tentativaRepository,
                            TurmaAlunoRepository turmaAlunoRepository,
                            ProgressoRepository progressoRepository,
                            SessaoEstudoRepository sessaoRepository,
                            MissaoDiariaService missaoService) {
        this.tentativaRepository = tentativaRepository;
        this.turmaAlunoRepository = turmaAlunoRepository;
        this.progressoRepository = progressoRepository;
        this.sessaoRepository = sessaoRepository;
        this.missaoService = missaoService;
    }

    public DashboardService(TentativaAlunoRepository tentativaRepository,
                            TurmaAlunoRepository turmaAlunoRepository) {
        this(tentativaRepository, turmaAlunoRepository, null, null, null);
    }

    @Transactional
    public DashboardStatsDTO buscar(Usuario aluno) {
        if (aluno == null || aluno.getPerfil() != Perfil.ALUNO) {
            throw new DashboardAcessoNegadoException();
        }
        int nextLevelXp = aluno.getNivel() * 100;
        int xpToday = calcularXpHoje(aluno);
        Integer posicao = calcularPosicao(aluno);
        List<com.machado0.gamificacao.missao.MissaoDiariaDTO> missoes =
                missaoService == null ? List.of() : missaoService.buscarEAtualizar(aluno);
        return new DashboardStatsDTO(
                aluno.getNivel(),
                aluno.getXp(),
                nextLevelXp,
                xpToday,
                aluno.getStreak(),
                aluno.getBestStreak(),
                aluno.getMoedas(),
                aluno.getVidas(),
                posicao,
                Math.toIntExact(tentativaRepository.countByUsuario(aluno)),
                Math.toIntExact(tentativaRepository.countByUsuarioAndAcertouTrue(aluno)),
                progressoRepository == null ? 0 : Math.toIntExact(progressoRepository
                        .countByUsuarioAndPercentualConclusaoGreaterThanEqual(aluno, 100)),
                calcularTempoTotal(aluno),
                missoes,
                calcularProgressoSemanal(aluno));
    }

    private int calcularTempoTotal(Usuario aluno) {
        if (sessaoRepository == null) return 0;
        return sessaoRepository.findByUsuario(aluno).stream()
                .filter(SessaoEstudo::isConcluida)
                .map(SessaoEstudo::getDuracaoMinutos)
                .filter(java.util.Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private ProgressoSemanalDTO calcularProgressoSemanal(Usuario aluno) {
        LocalDate hoje = LocalDate.now();
        LocalDate segunda = hoje.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime inicio = segunda.atStartOfDay();
        LocalDateTime fim = segunda.plusDays(7).atStartOfDay();
        List<TentativaAluno> tentativas = tentativaRepository
                .findByUsuarioAndDataTentativaGreaterThanEqualAndDataTentativaLessThan(aluno, inicio, fim);
        List<ProgressoDiaDTO> dias = new ArrayList<>();
        int totalXp = 0;
        int diasEstudados = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate data = segunda.plusDays(i);
            List<TentativaAluno> doDia = tentativas.stream()
                    .filter(item -> item.getDataTentativa().toLocalDate().equals(data)).toList();
            int xp = doDia.stream().filter(TentativaAluno::isAcertou)
                    .collect(java.util.stream.Collectors.toMap(
                            item -> item.getQuestao().getId(),
                            item -> item.getQuestao().getXpRecompensa(),
                            Integer::max))
                    .values().stream().mapToInt(Integer::intValue).sum();
            int aulas = progressoRepository == null ? 0 : (int) progressoRepository.findByUsuario(aluno).stream()
                    .filter(item -> item.getPercentualConclusao() >= 100
                            && item.getDataAtualizacao() != null
                            && item.getDataAtualizacao().toLocalDate().equals(data))
                    .count();
            dias.add(new ProgressoDiaDTO(data,
                    data.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, new Locale("pt", "BR")),
                    xp, aulas, doDia.size()));
            totalXp += xp;
            if (!doDia.isEmpty() || aulas > 0) diasEstudados++;
        }
        return new ProgressoSemanalDTO(dias, totalXp, diasEstudados);
    }

    private int calcularXpHoje(Usuario aluno) {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        return tentativaRepository.findByUsuarioAndAcertouTrue(aluno).stream()
                .filter(tentativa -> tentativa.getDataTentativa().toLocalDate().equals(hoje))
                .collect(java.util.stream.Collectors.toMap(
                        tentativa -> tentativa.getQuestao().getId(),
                        TentativaAluno::getQuestao,
                        (primeira, segunda) -> primeira))
                .values().stream()
                .mapToInt(Questao::getXpRecompensa)
                .sum();
    }

    private Integer calcularPosicao(Usuario aluno) {
        TurmaAluno vinculo = turmaAlunoRepository.findFirstByUsuario(aluno)
                .filter(item -> item.getTurma().isAtivo()).orElse(null);
        if (vinculo == null) return null;
        return (int) turmaAlunoRepository.findByTurma(vinculo.getTurma()).stream()
                .map(TurmaAluno::getUsuario)
                .sorted(java.util.Comparator.comparingInt(Usuario::getXp).reversed()
                        .thenComparing(Usuario::getId))
                .takeWhile(usuario -> !usuario.getId().equals(aluno.getId()))
                .count() + 1;
    }
}
