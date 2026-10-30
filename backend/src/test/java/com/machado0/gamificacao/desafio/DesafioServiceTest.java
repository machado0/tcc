package com.machado0.gamificacao.desafio;

import com.machado0.gamificacao.alternativa.Alternativa;
import com.machado0.gamificacao.alternativa.AlternativaRepository;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoRepository;
import com.machado0.gamificacao.tentativa.TentativaAlunoRepository;
import com.machado0.gamificacao.conquista.ConquistaRepository;
import com.machado0.gamificacao.conquista.usuario.ConquistaUsuarioRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesafioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private QuestaoRepository questaoRepository;
    @Mock private AlternativaRepository alternativaRepository;
    @Mock private TentativaAlunoRepository tentativaRepository;
    @Mock private ConquistaRepository conquistaRepository;
    @Mock private ConquistaUsuarioRepository conquistaUsuarioRepository;

    private DesafioService service;
    private Usuario aluno;
    private Questao questao;
    private Alternativa correta;

    @BeforeEach
    void setUp() {
        service = new DesafioService(
                usuarioRepository, questaoRepository, alternativaRepository, tentativaRepository,
                conquistaRepository, conquistaUsuarioRepository);
        aluno = new Usuario();
        aluno.setId(1L);
        aluno.setPerfil(Perfil.ALUNO);
        aluno.setVidas(3);
        aluno.setNivel(1);
        aluno.setXp(90);
        aluno.setMoedas(0);

        questao = new Questao();
        questao.setId(10L);
        questao.setXpRecompensa(15);
        correta = new Alternativa();
        correta.setId(20L);
        correta.setQuestao(questao);
        correta.setCorreta(true);
        correta.setJustificativa("Resolucao correta");
    }

    @Test
    void deveConcederRecompensaEAtualizarNivelNoPrimeiroAcerto() {
        when(questaoRepository.findById(10L)).thenReturn(Optional.of(questao));
        when(alternativaRepository.findById(20L)).thenReturn(Optional.of(correta));
        when(tentativaRepository.existsByUsuarioAndQuestaoAndAcertouTrue(aluno, questao))
                .thenReturn(false);
        when(alternativaRepository.findByQuestaoOrderByIdAsc(questao)).thenReturn(List.of(correta));

        FeedbackDTO feedback = service.processarSubmissao(
                aluno, new SubmeterDesafioDTO(10L, 20L));

        assertTrue(feedback.acertou());
        assertEquals(15, feedback.xpGanho());
        assertEquals(105, feedback.novoXpTotal());
        assertEquals(2, feedback.novoNivel());
        assertEquals(5, feedback.moedasGanha());
        assertEquals(1, aluno.getStreak());
        verify(usuarioRepository).save(aluno);
        verify(tentativaRepository).save(any());
    }

    @Test
    void naoDeveConcederRecompensaEmAcertoRepetido() {
        when(questaoRepository.findById(10L)).thenReturn(Optional.of(questao));
        when(alternativaRepository.findById(20L)).thenReturn(Optional.of(correta));
        when(tentativaRepository.existsByUsuarioAndQuestaoAndAcertouTrue(aluno, questao))
                .thenReturn(true);
        when(alternativaRepository.findByQuestaoOrderByIdAsc(questao)).thenReturn(List.of(correta));

        FeedbackDTO feedback = service.processarSubmissao(
                aluno, new SubmeterDesafioDTO(10L, 20L));

        assertEquals(0, feedback.xpGanho());
        assertEquals(90, aluno.getXp());
        assertEquals(0, aluno.getMoedas());
    }

    @Test
    void deveBloquearSubmissaoSemVidas() {
        aluno.setVidas(0);

        assertThrows(SemVidasDisponiveisException.class, () ->
                service.processarSubmissao(aluno, new SubmeterDesafioDTO(10L, 20L)));

        verifyNoInteractions(questaoRepository, alternativaRepository, tentativaRepository);
    }
}
