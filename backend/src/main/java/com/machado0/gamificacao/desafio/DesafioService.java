package com.machado0.gamificacao.desafio;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.alternativa.AlternativaRepository;
import com.machado0.gamificacao.conquista.Conquista;
import com.machado0.gamificacao.conquista.ConquistaDesbloqueadaDTO;
import com.machado0.gamificacao.conquista.TipoConquista;
import com.machado0.gamificacao.conquista.ConquistaRepository;
import com.machado0.gamificacao.conquista.usuario.ConquistaUsuario;
import com.machado0.gamificacao.conquista.usuario.ConquistaUsuarioRepository;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoRepository;
import com.machado0.gamificacao.tentativa.TentativaAluno;
import com.machado0.gamificacao.tentativa.TentativaAlunoRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import com.machado0.gamificacao.progresso.Progresso;
import com.machado0.gamificacao.progresso.ProgressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class DesafioService {
    private final UsuarioRepository usuarioRepository;
    private final QuestaoRepository questaoRepository;
    private final AlternativaRepository alternativaRepository;
    private final TentativaAlunoRepository tentativaRepository;
    private final ConquistaRepository conquistaRepository;
    private final ConquistaUsuarioRepository conquistaUsuarioRepository;
    private final ProgressoRepository progressoRepository;

    @Autowired
    public DesafioService(UsuarioRepository usuarioRepository, QuestaoRepository questaoRepository,
                          AlternativaRepository alternativaRepository,
                          TentativaAlunoRepository tentativaRepository,
                          ConquistaRepository conquistaRepository,
                          ConquistaUsuarioRepository conquistaUsuarioRepository,
                          ProgressoRepository progressoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
        this.tentativaRepository = tentativaRepository;
        this.conquistaRepository = conquistaRepository;
        this.conquistaUsuarioRepository = conquistaUsuarioRepository;
        this.progressoRepository = progressoRepository;
    }

    public DesafioService(UsuarioRepository usuarioRepository, QuestaoRepository questaoRepository,
                          AlternativaRepository alternativaRepository,
                          TentativaAlunoRepository tentativaRepository,
                          ConquistaRepository conquistaRepository,
                          ConquistaUsuarioRepository conquistaUsuarioRepository) {
        this(usuarioRepository, questaoRepository, alternativaRepository, tentativaRepository,
                conquistaRepository, conquistaUsuarioRepository, null);
    }

    @Transactional
    public FeedbackDTO processarSubmissao(Usuario aluno, SubmeterDesafioDTO request) {
        if (aluno == null || aluno.getPerfil() != Perfil.ALUNO) {
            throw new DesafioInvalidoException("Apenas alunos podem submeter desafios");
        }
        if (aluno.getVidas() <= 0) {
            throw new SemVidasDisponiveisException();
        }

        Questao questao = questaoRepository.findById(request.questaoId())
                .orElseThrow(() -> new DesafioInvalidoException("Questao nao encontrada"));
        Alternativa alternativa = alternativaRepository.findById(request.alternativaEscolhidaId())
                .orElseThrow(() -> new DesafioInvalidoException("Alternativa nao encontrada"));
        if (alternativa.getQuestao() == null
                || !questao.getId().equals(alternativa.getQuestao().getId())) {
            throw new DesafioInvalidoException("A alternativa nao pertence a questao informada");
        }

        boolean acertou = alternativa.isCorreta();
        boolean primeiroAcerto = acertou
                && !tentativaRepository.existsByUsuarioAndQuestaoAndAcertouTrue(aluno, questao);
        int xpGanho = primeiroAcerto ? questao.getXpRecompensa() : 0;
        int moedasGanha = primeiroAcerto ? 5 : 0;

        if (!acertou) {
            aluno.setVidas(aluno.getVidas() - 1);
        }
        if (primeiroAcerto) {
            aluno.setXp(aluno.getXp() + xpGanho);
            aluno.setNivel(aluno.getXp() / 100 + 1);
            aluno.setMoedas(aluno.getMoedas() + moedasGanha);
        }
        atualizarStreak(aluno);
        usuarioRepository.save(aluno);

        TentativaAluno tentativa = new TentativaAluno();
        tentativa.setUsuario(aluno);
        tentativa.setQuestao(questao);
        tentativa.setAlternativaEscolhida(alternativa);
        tentativa.setAcertou(acertou);
        tentativa.setDataTentativa(LocalDateTime.now());
        tentativaRepository.save(tentativa);
        atualizarProgresso(aluno, questao);

        Long alternativaCorretaId = alternativaRepository.findByQuestaoOrderByIdAsc(questao).stream()
                .filter(Alternativa::isCorreta)
                .map(Alternativa::getId)
                .findFirst()
                .orElse(null);
        var conquistas = avaliarConquistas(aluno, acertou);
        return new FeedbackDTO(acertou, alternativaCorretaId, alternativa.getJustificativa(),
                xpGanho, moedasGanha, aluno.getVidas(), aluno.getXp(), aluno.getNivel(), conquistas);
    }

    private void atualizarProgresso(Usuario aluno, Questao questao) {
        if (progressoRepository == null) {
            return;
        }
        int totalQuestoes = questaoRepository.findByAulaOrderByIdAsc(questao.getAula()).size();
        long acertos = tentativaRepository.findByUsuarioAndAcertouTrue(aluno).stream()
                .filter(tentativa -> tentativa.getQuestao().getAula().getId().equals(questao.getAula().getId()))
                .map(TentativaAluno::getQuestao)
                .distinct()
                .count();
        Progresso progresso = progressoRepository.findByUsuarioAndAula(aluno, questao.getAula())
                .orElseGet(() -> {
                    Progresso novo = new Progresso();
                    novo.setUsuario(aluno);
                    novo.setAula(questao.getAula());
                    return novo;
                });
        progresso.setPercentualConclusao(totalQuestoes == 0 ? 0
                : Math.min(100, (int) ((acertos * 100) / totalQuestoes)));
        progresso.setDataAtualizacao(LocalDateTime.now());
        progressoRepository.save(progresso);
    }

    private java.util.List<ConquistaDesbloqueadaDTO> avaliarConquistas(Usuario aluno, boolean acertou) {
        if (!acertou || conquistaRepository == null || conquistaUsuarioRepository == null) {
            return java.util.List.of();
        }
        long acertos = tentativaRepository.countByUsuarioAndAcertouTrue(aluno);
        long respostas = tentativaRepository.countByUsuario(aluno);
        return conquistaRepository.findAll().stream()
                .filter(conquista -> atingiu(conquista, aluno, acertos, respostas))
                .filter(conquista -> !conquistaUsuarioRepository
                        .existsByUsuarioIdAndConquistaIdAndDesbloqueadaTrue(aluno.getId(), conquista.getId()))
                .map(conquista -> desbloquear(aluno, conquista))
                .toList();
    }

    private boolean atingiu(Conquista conquista, Usuario aluno, long acertos, long respostas) {
        int meta = Math.max(conquista.getMeta(), 1);
        TipoConquista tipo = conquista.getTipo();
        if (tipo == TipoConquista.OFENSIVA) return aluno.getStreak() >= meta;
        if (tipo == TipoConquista.ACERTOS) return acertos >= meta;
        if (tipo == TipoConquista.QUESTOES) return respostas >= meta;
        return acertos >= meta;
    }

    private ConquistaDesbloqueadaDTO desbloquear(Usuario aluno, Conquista conquista) {
        ConquistaUsuario desbloqueio = conquistaUsuarioRepository
                .findByUsuarioAndConquista(aluno, conquista).orElseGet(ConquistaUsuario::new);
        desbloqueio.setUsuario(aluno);
        desbloqueio.setConquista(conquista);
        desbloqueio.setProgresso(conquista.getMeta());
        desbloqueio.setDesbloqueada(true);
        desbloqueio.setDataDesbloqueio(LocalDateTime.now());
        conquistaUsuarioRepository.save(desbloqueio);
        aluno.setXp(aluno.getXp() + conquista.getRecompensaXp());
        aluno.setNivel(aluno.getXp() / 100 + 1);
        aluno.setMoedas(aluno.getMoedas() + 10);
        usuarioRepository.save(aluno);
        return new ConquistaDesbloqueadaDTO(conquista.getId(), conquista.getTitulo(),
                conquista.getDescricao(), conquista.getRecompensaXp());
    }

    private void atualizarStreak(Usuario aluno) {
        if (aluno.getUltimoAcesso() == null) {
            aluno.setStreak(1);
        } else {
            long horas = Duration.between(aluno.getUltimoAcesso(), LocalDateTime.now()).toHours();
            if (horas >= 24 && horas <= 48) {
                aluno.setStreak(aluno.getStreak() + 1);
            } else if (horas > 48) {
                aluno.setStreak(1);
            }
        }
        aluno.setBestStreak(Math.max(aluno.getBestStreak(), aluno.getStreak()));
        aluno.setUltimoAcesso(LocalDateTime.now());
    }
}
