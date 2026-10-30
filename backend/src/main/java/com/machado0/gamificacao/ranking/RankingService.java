package com.machado0.gamificacao.ranking;

import com.machado0.gamificacao.turma.TurmaAluno;
import com.machado0.gamificacao.turma.TurmaAlunoRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class RankingService {
    private final TurmaAlunoRepository turmaAlunoRepository;

    public RankingService(TurmaAlunoRepository turmaAlunoRepository) {
        this.turmaAlunoRepository = turmaAlunoRepository;
    }

    @Transactional(readOnly = true)
    public RankingResponseDTO buscar(Usuario aluno) {
        if (aluno == null || aluno.getPerfil() != Perfil.ALUNO) {
            throw new RankingNaoDisponivelException();
        }
        TurmaAluno vinculo = turmaAlunoRepository.findFirstByUsuario(aluno)
                .filter(item -> item.getTurma().isAtivo())
                .orElseThrow(RankingNaoDisponivelException::new);

        List<Usuario> alunos = turmaAlunoRepository.findByTurma(vinculo.getTurma()).stream()
                .map(TurmaAluno::getUsuario)
                .sorted(Comparator.comparingInt(Usuario::getXp).reversed()
                        .thenComparing(Usuario::getId))
                .toList();
        List<RankingItemDTO> itens = java.util.stream.IntStream.range(0, alunos.size())
                .mapToObj(index -> paraItem(alunos.get(index), index + 1))
                .toList();
        RankingItemDTO meuPerfil = itens.stream()
                .filter(item -> item.usuarioId().equals(aluno.getId()))
                .findFirst()
                .orElseThrow(RankingNaoDisponivelException::new);
        return new RankingResponseDTO(vinculo.getTurma().getId(), vinculo.getTurma().getNome(),
                itens, meuPerfil);
    }

    private RankingItemDTO paraItem(Usuario usuario, int posicao) {
        return new RankingItemDTO(posicao, usuario.getId(), usuario.getNome(),
                usuario.getXp(), usuario.getNivel(), usuario.getStreak());
    }
}
