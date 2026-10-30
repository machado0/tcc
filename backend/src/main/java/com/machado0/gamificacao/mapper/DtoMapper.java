package com.machado0.gamificacao.mapper;

import com.machado0.gamificacao.alternativa.*;
import com.machado0.gamificacao.aula.*;
import com.machado0.gamificacao.conquista.*;
import com.machado0.gamificacao.curso.*;
import com.machado0.gamificacao.modulo.*;
import com.machado0.gamificacao.progresso.*;
import com.machado0.gamificacao.questao.*;
import com.machado0.gamificacao.usuario.*;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final AulaRepository aulaRepository;
    private final QuestaoRepository questaoRepository;
    private final UsuarioRepository usuarioRepository;

    public DtoMapper(CursoRepository cursoRepository, ModuloRepository moduloRepository,
                     AulaRepository aulaRepository, QuestaoRepository questaoRepository,
                     UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.moduloRepository = moduloRepository;
        this.aulaRepository = aulaRepository;
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Curso toCurso(CursoDto dto) {
        Curso e = new Curso(); e.setId(dto.getId()); e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao()); e.setIcone(dto.getIcone()); return e;
    }
    public CursoDto toCursoDto(Curso e) {
        CursoDto d = new CursoDto(); d.setId(e.getId()); d.setNome(e.getNome());
        d.setDescricao(e.getDescricao()); d.setIcone(e.getIcone()); return d;
    }
    public Modulo toModulo(ModuloDto dto) {
        Modulo e = new Modulo(); e.setId(dto.getId()); e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao()); e.setOrdem(dto.getOrdem());
        if (dto.getCursoId() != null) e.setCurso(cursoRepository.getReferenceById(dto.getCursoId()));

        return e;
    }
    public ModuloDto toModuloDto(Modulo e) {
        ModuloDto d = new ModuloDto(); d.setId(e.getId()); d.setNome(e.getNome());
        d.setDescricao(e.getDescricao()); d.setOrdem(e.getOrdem());
        if (e.getCurso() != null) d.setCursoId(e.getCurso().getId());

        return d;
    }
    public Aula toAula(AulaDto dto) {
        Aula e = new Aula(); e.setId(dto.getId()); e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao()); e.setOrdem(dto.getOrdem());
        if (dto.getModuloId() != null) e.setModulo(moduloRepository.getReferenceById(dto.getModuloId()));

        return e;
    }
    public AulaDto toAulaDto(Aula e) {
        AulaDto d = new AulaDto(); d.setId(e.getId()); d.setNome(e.getNome());
        d.setDescricao(e.getDescricao()); d.setOrdem(e.getOrdem());
        if (e.getModulo() != null) d.setModuloId(e.getModulo().getId());

        return d;
    }
    public Questao toQuestao(QuestaoDto dto) {
        Questao e = new Questao(); e.setId(dto.getId()); e.setCabecalho(dto.getCabecalho());
        e.setDificuldade(dto.getDificuldade() == null ? Dificuldade.FACIL : dto.getDificuldade());
        e.setXpRecompensa(dto.getXpRecompensa());
        if (dto.getCursoId() != null) e.setCurso(cursoRepository.getReferenceById(dto.getCursoId()));
        if (dto.getAulaId() != null) e.setAula(aulaRepository.getReferenceById(dto.getAulaId()));

        return e;
    }
    public QuestaoDto toQuestaoDto(Questao e) {
        QuestaoDto d = new QuestaoDto(); d.setId(e.getId()); d.setCabecalho(e.getCabecalho());
        d.setDificuldade(e.getDificuldade()); d.setXpRecompensa(e.getXpRecompensa());
        if (e.getCurso() != null) d.setCursoId(e.getCurso().getId());
        if (e.getAula() != null) d.setAulaId(e.getAula().getId());

        return d;
    }
    public Alternativa toAlternativa(AlternativaDto dto) {
        Alternativa e = new Alternativa(); e.setId(dto.getId()); e.setTexto(dto.getTexto());
        e.setJustificativa(dto.getJustificativa()); e.setCorreta(dto.isCorreta());
        if (dto.getQuestaoId() != null) e.setQuestao(questaoRepository.getReferenceById(dto.getQuestaoId()));

        return e;
    }
    public AlternativaDto toAlternativaDto(Alternativa e) {
        AlternativaDto d = new AlternativaDto(); d.setId(e.getId()); d.setTexto(e.getTexto());
        d.setJustificativa(e.getJustificativa()); d.setCorreta(e.isCorreta());
        if (e.getQuestao() != null) d.setQuestaoId(e.getQuestao().getId());

        return d;
    }
    public Conquista toConquista(ConquistaDto dto) {
        Conquista e = new Conquista(); e.setId(dto.getId()); e.setNome(dto.getNome());
        e.setTitulo(dto.getTitulo()); e.setDescricao(dto.getDescricao()); e.setIcone(dto.getIcone());
        e.setMeta(dto.getMeta()); e.setRecompensaXp(dto.getRecompensaXp()); e.setTipo(dto.getTipo());
        if (dto.getModuloId() != null) {
            Modulo m = moduloRepository.getReferenceById(dto.getModuloId()); e.setModulo(m);
        } return e;
    }
    public ConquistaDto toConquistaDto(Conquista e) {
        ConquistaDto d = new ConquistaDto(); d.setId(e.getId()); d.setNome(e.getNome());
        d.setTitulo(e.getTitulo()); d.setDescricao(e.getDescricao()); d.setIcone(e.getIcone());
        d.setMeta(e.getMeta()); d.setRecompensaXp(e.getRecompensaXp()); d.setTipo(e.getTipo());
        if (e.getModulo() != null) d.setModuloId(e.getModulo().getId());

        return d;
    }
    public Progresso toProgresso(ProgressoDto dto) {
        Progresso e = new Progresso(); e.setId(dto.getId()); e.setPercentualConclusao(dto.getPercentualConclusao());
        if (dto.getUsuarioId() != null) e.setUsuario(usuarioRepository.getReferenceById(dto.getUsuarioId()));
        if (dto.getAulaId() != null) e.setAula(aulaRepository.getReferenceById(dto.getAulaId()));

        return e;
    }
    public ProgressoDto toProgressoDto(Progresso e) {
        ProgressoDto d = new ProgressoDto(); d.setId(e.getId()); d.setPercentualConclusao(e.getPercentualConclusao());
        if (e.getUsuario() != null) d.setUsuarioId(e.getUsuario().getId());
        if (e.getAula() != null) d.setAulaId(e.getAula().getId());

        return d;
    }
    public Usuario toUsuario(UsuarioDto dto) {
        Usuario e = new Usuario(); e.setId(dto.getId()); e.setNome(dto.getNome());
        e.setUsername(dto.getUsername()); e.setEmail(dto.getEmail()); e.setSenha(dto.getSenha()); e.setTituloHonra(dto.getTituloHonra()); return e;
    }
    public UsuarioDto toUsuarioDto(Usuario e) {
        UsuarioDto d = new UsuarioDto(); d.setId(e.getId()); d.setNome(e.getNome());
        d.setUsername(e.getUsername()); d.setEmail(e.getEmail()); d.setSenha(e.getSenha());
        d.setTituloHonra(e.getTituloHonra()); d.setDataCriacao(e.getDataCriacao()); return d;
    }
}
