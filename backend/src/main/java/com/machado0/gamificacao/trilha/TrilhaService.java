package com.machado0.gamificacao.trilha;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.alternativa.AlternativaRepository;
import com.machado0.gamificacao.progresso.Progresso;
import com.machado0.gamificacao.progresso.ProgressoRepository;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.aula.AulaRepository;
import com.machado0.gamificacao.curso.Curso;
import com.machado0.gamificacao.curso.CursoRepository;
import com.machado0.gamificacao.modulo.Modulo;
import com.machado0.gamificacao.modulo.ModuloRepository;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrilhaService {
    private static final String TRILHA_MATEMATICA = "Matemática - Ensino Médio";
    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final AulaRepository aulaRepository;
    private final QuestaoRepository questaoRepository;
    private final AlternativaRepository alternativaRepository;
    private final ProgressoRepository progressoRepository;

    @Autowired
    public TrilhaService(CursoRepository cursoRepository, ModuloRepository moduloRepository,
                         AulaRepository aulaRepository, QuestaoRepository questaoRepository,
                         AlternativaRepository alternativaRepository,
                         ProgressoRepository progressoRepository) {
        this.cursoRepository = cursoRepository;
        this.moduloRepository = moduloRepository;
        this.aulaRepository = aulaRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
        this.progressoRepository = progressoRepository;
    }

    public TrilhaService(CursoRepository cursoRepository, ModuloRepository moduloRepository,
                         AulaRepository aulaRepository, QuestaoRepository questaoRepository,
                         AlternativaRepository alternativaRepository) {
        this(cursoRepository, moduloRepository, aulaRepository, questaoRepository,
                alternativaRepository, null);
    }

    @Transactional(readOnly = true)
    public TrilhaResponseDTO buscarMatematica(Usuario usuario) {
        Curso curso = cursoRepository.findByNome(TRILHA_MATEMATICA)
                .orElseThrow(() -> new TrilhaNaoEncontradaException(TRILHA_MATEMATICA));
        return new TrilhaResponseDTO(curso.getId(), curso.getNome(), curso.getDescricao(),
                moduloRepository.findByCursoOrderByOrdemAsc(curso).stream()
                        .map(modulo -> paraModulo(modulo, usuario)).toList());
    }

    @Transactional(readOnly = true)
    public TrilhaResponseDTO buscarMatematica() {
        return buscarMatematica(null);
    }

    private ModuloResponseDTO paraModulo(Modulo modulo, Usuario usuario) {
        var aulas = aulaRepository.findByModuloOrderByOrdemAsc(modulo).stream()
                .map(aula -> paraAula(aula, usuario)).toList();
        int progresso = aulas.isEmpty() ? 0
                : (int) aulas.stream().mapToInt(AulaResponseDTO::progressoPercentual).average().orElse(0);
        return new ModuloResponseDTO(modulo.getId(), modulo.getNome(), modulo.getDescricao(),
                modulo.getOrdem(), aulas, status(progresso), progresso);
    }

    private AulaResponseDTO paraAula(Aula aula, Usuario usuario) {
        Progresso progresso = progressoRepository == null || usuario == null
                ? null : progressoRepository.findByUsuarioAndAula(usuario, aula).orElse(null);
        int percentual = progresso == null ? 0 : progresso.getPercentualConclusao();
        return new AulaResponseDTO(aula.getId(), aula.getNome(), aula.getDescricao(),
                aula.getOrdem(), aula.getDuracaoMinutos(),
                questaoRepository.findByAulaOrderByIdAsc(aula).stream()
                        .map(this::paraQuestao).toList(), status(percentual), percentual);
    }

    private String status(int percentual) {
        return percentual >= 100 ? "CONCLUIDA" : "DISPONIVEL";
    }

    private QuestaoResponseDTO paraQuestao(Questao questao) {
        return new QuestaoResponseDTO(questao.getId(), questao.getCabecalho(),
                questao.getDificuldade().name(), questao.getXpRecompensa(),
                alternativaRepository.findByQuestaoOrderByIdAsc(questao).stream()
                        .map(this::paraAlternativa).toList());
    }

    private AlternativaResponseDTO paraAlternativa(Alternativa alternativa) {
        return new AlternativaResponseDTO(alternativa.getId(), alternativa.getTexto());
    }
}
