package com.machado0.gamificacao.desafio;

import com.machado0.gamificacao.alternativa.*;
import com.machado0.gamificacao.conquista.*;
import com.machado0.gamificacao.conquista.usuario.*;
import com.machado0.gamificacao.questao.Questao;
import com.machado0.gamificacao.questao.QuestaoRepository;
import com.machado0.gamificacao.tentativa.*;
import com.machado0.gamificacao.usuario.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesafioConquistaTest {
    @Mock UsuarioRepository usuarioRepository;
    @Mock QuestaoRepository questaoRepository;
    @Mock AlternativaRepository alternativaRepository;
    @Mock TentativaAlunoRepository tentativaRepository;
    @Mock ConquistaRepository conquistaRepository;
    @Mock ConquistaUsuarioRepository conquistaUsuarioRepository;

    @Test
    void deveDesbloquearConquistaUmaUnicaVezNaMesmaTransacao() {
        Usuario aluno = new Usuario();
        aluno.setId(1L);
        aluno.setPerfil(Perfil.ALUNO);
        aluno.setVidas(3);
        Questao questao = new Questao();
        questao.setId(2L);
        questao.setXpRecompensa(10);
        Alternativa alternativa = new Alternativa();
        alternativa.setId(3L);
        alternativa.setQuestao(questao);
        alternativa.setCorreta(true);
        alternativa.setJustificativa("ok");
        Conquista conquista = new Conquista();
        conquista.setId(4L);
        conquista.setTitulo("Primeiro acerto");
        conquista.setDescricao("Acertou");
        conquista.setMeta(1);
        conquista.setTipo(TipoConquista.ACERTOS);
        conquista.setRecompensaXp(50);

        when(questaoRepository.findById(2L)).thenReturn(Optional.of(questao));
        when(alternativaRepository.findById(3L)).thenReturn(Optional.of(alternativa));
        when(alternativaRepository.findByQuestaoOrderByIdAsc(questao)).thenReturn(List.of(alternativa));
        when(tentativaRepository.existsByUsuarioAndQuestaoAndAcertouTrue(aluno, questao)).thenReturn(false);
        when(tentativaRepository.countByUsuarioAndAcertouTrue(aluno)).thenReturn(1L);
        when(tentativaRepository.countByUsuario(aluno)).thenReturn(1L);
        when(conquistaRepository.findAll()).thenReturn(List.of(conquista));
        when(conquistaUsuarioRepository.existsByUsuarioIdAndConquistaIdAndDesbloqueadaTrue(1L, 4L))
                .thenReturn(false);
        when(conquistaUsuarioRepository.findByUsuarioAndConquista(aluno, conquista))
                .thenReturn(Optional.empty());

        FeedbackDTO feedback = new DesafioService(usuarioRepository, questaoRepository,
                alternativaRepository, tentativaRepository, conquistaRepository,
                conquistaUsuarioRepository).processarSubmissao(aluno,
                new SubmeterDesafioDTO(2L, 3L));

        assertEquals(1, feedback.conquistasDesbloqueadas().size());
        assertEquals(60, aluno.getXp());
        verify(conquistaUsuarioRepository).save(any(ConquistaUsuario.class));
    }
}
