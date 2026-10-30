package com.machado0.gamificacao.mapper;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.alternativa.AlternativaDto;
import com.machado0.gamificacao.aula.Aula;
import com.machado0.gamificacao.aula.AulaDto;
import com.machado0.gamificacao.conquista.Conquista;
import com.machado0.gamificacao.conquista.ConquistaDto;
import com.machado0.gamificacao.curso.Curso;
import com.machado0.gamificacao.curso.CursoDto;
import com.machado0.gamificacao.modulo.Modulo;
import com.machado0.gamificacao.modulo.ModuloDto;
import com.machado0.gamificacao.progresso.Progresso;
import com.machado0.gamificacao.progresso.ProgressoDto;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoDto;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioDto;
import com.machado0.gamificacao.curso.CursoRepository;
import com.machado0.gamificacao.modulo.ModuloRepository;
import com.machado0.gamificacao.aula.AulaRepository;
import com.machado0.gamificacao.questao.QuestaoRepository;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DtoMapperTest {

    private CursoRepository cursoRepository = Mockito.mock(CursoRepository.class);
    private ModuloRepository moduloRepository = Mockito.mock(ModuloRepository.class);
    private AulaRepository aulaRepository = Mockito.mock(AulaRepository.class);
    private QuestaoRepository questaoRepository = Mockito.mock(QuestaoRepository.class);
    private UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);

    private DtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DtoMapper(cursoRepository, moduloRepository, aulaRepository, questaoRepository, usuarioRepository);

        when(cursoRepository.getReferenceById(anyLong())).thenAnswer(inv -> {
            Curso c = new Curso();
            c.setId((Long) inv.getArgument(0));
            return c;
        });
        when(moduloRepository.getReferenceById(anyLong())).thenAnswer(inv -> {
            Modulo m = new Modulo();
            m.setId((Long) inv.getArgument(0));
            return m;
        });
        when(aulaRepository.getReferenceById(anyLong())).thenAnswer(inv -> {
            Aula a = new Aula();
            a.setId((Long) inv.getArgument(0));
            return a;
        });
        when(questaoRepository.getReferenceById(anyLong())).thenAnswer(inv -> {
            Questao q = new Questao();
            q.setId((Long) inv.getArgument(0));
            return q;
        });
        when(usuarioRepository.getReferenceById(anyLong())).thenAnswer(inv -> {
            Usuario u = new Usuario();
            u.setId((Long) inv.getArgument(0));
            return u;
        });
    }

    @Test
    void cursoMapping() {
        CursoDto dto = new CursoDto();
        dto.setId(1L);
        dto.setNome("Matematica");
        dto.setDescricao("Descricao");

        Curso entity = mapper.toCurso(dto);
        assertNotNull(entity);
        assertEquals(dto.getNome(), entity.getNome());

        CursoDto mapped = mapper.toCursoDto(entity);
        assertEquals(dto.getNome(), mapped.getNome());
    }

    @Test
    void usuarioMapping() {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(2L);
        dto.setNome("Aluno");
        dto.setEmail("a@b.com");
        dto.setSenha("123");

        Usuario u = mapper.toUsuario(dto);
        assertNotNull(u);
        assertEquals(dto.getEmail(), u.getEmail());

        UsuarioDto out = mapper.toUsuarioDto(u);
        assertEquals(u.getEmail(), out.getEmail());
    }

    @Test
    void moduloAulaQuestaoMappings() {
        ModuloDto mdto = new ModuloDto();
        mdto.setId(3L);
        mdto.setNome("M1");
        mdto.setDescricao("Desc");
        mdto.setCursoId(10L);

        Modulo m = mapper.toModulo(mdto);
        assertNotNull(m);
        assertNotNull(m.getCurso());
        assertEquals(10L, m.getCurso().getId());

        ModuloDto mdout = mapper.toModuloDto(m);
        assertEquals(m.getNome(), mdout.getNome());

        AulaDto adto = new AulaDto();
        adto.setId(4L);
        adto.setNome("A1");
        adto.setDescricao("D");
        adto.setModuloId(3L);

        Aula a = mapper.toAula(adto);
        assertNotNull(a);
        assertNotNull(a.getModulo());
        assertEquals(3L, a.getModulo().getId());

        AulaDto adout = mapper.toAulaDto(a);
        assertEquals(a.getNome(), adout.getNome());

        QuestaoDto qdto = new QuestaoDto();
        qdto.setId(5L);
        qdto.setCabecalho("Enunciado");
        qdto.setCursoId(10L);
        qdto.setAulaId(4L);

        Questao q = mapper.toQuestao(qdto);
        assertNotNull(q);
        assertEquals("Enunciado", q.getCabecalho());
        assertNotNull(q.getCurso());
        assertNotNull(q.getAula());

        QuestaoDto qout = mapper.toQuestaoDto(q);
        assertEquals(q.getCabecalho(), qout.getCabecalho());
    }

    @Test
    void alternativaConquistaProgressoMappings() {
        AlternativaDto adto = new AlternativaDto();
        adto.setId(6L);
        adto.setTexto("Alt");
        adto.setQuestaoId(5L);
        adto.setJustificativa("Just");
        adto.setCorreta(true);

        Alternativa a = mapper.toAlternativa(adto);
        assertNotNull(a);
        assertEquals(5L, a.getQuestao().getId());

        AlternativaDto adout = mapper.toAlternativaDto(a);
        assertEquals(a.getTexto(), adout.getTexto());

        ConquistaDto cdto = new ConquistaDto();
        cdto.setId(7L);
        cdto.setModuloId(3L);
        cdto.setTipo(null);

        Conquista c = mapper.toConquista(cdto);
        assertNotNull(c);
        assertEquals(3L, c.getModulo().getId());

        ConquistaDto cdout = mapper.toConquistaDto(c);
        assertEquals(c.getModulo().getId(), cdout.getModuloId());

        ProgressoDto pdto = new ProgressoDto();
        pdto.setId(8L);
        pdto.setUsuarioId(2L);
        pdto.setAulaId(4L);

        Progresso p = mapper.toProgresso(pdto);
        assertNotNull(p);
        assertEquals(2L, p.getUsuario().getId());
        assertEquals(4L, p.getAula().getId());

        ProgressoDto pdout = mapper.toProgressoDto(p);
        assertEquals(p.getId(), pdout.getId());
    }
}

