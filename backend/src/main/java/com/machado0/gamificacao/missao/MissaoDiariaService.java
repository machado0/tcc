package com.machado0.gamificacao.missao;

import com.machado0.gamificacao.missao.usuario.MissaoUsuario;
import com.machado0.gamificacao.missao.usuario.MissaoUsuarioRepository;
import com.machado0.gamificacao.progresso.ProgressoRepository;
import com.machado0.gamificacao.tentativa.TentativaAluno;
import com.machado0.gamificacao.tentativa.TentativaAlunoRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MissaoDiariaService {
    private final TemplateMissaoRepository templateRepository;
    private final MissaoUsuarioRepository missaoRepository;
    private final TentativaAlunoRepository tentativaRepository;
    private final ProgressoRepository progressoRepository;
    private final UsuarioRepository usuarioRepository;

    public MissaoDiariaService(TemplateMissaoRepository templateRepository,
                               MissaoUsuarioRepository missaoRepository,
                               TentativaAlunoRepository tentativaRepository,
                               ProgressoRepository progressoRepository,
                               UsuarioRepository usuarioRepository) {
        this.templateRepository = templateRepository;
        this.missaoRepository = missaoRepository;
        this.tentativaRepository = tentativaRepository;
        this.progressoRepository = progressoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public List<MissaoDiariaDTO> buscarEAtualizar(Usuario usuario) {
        validarAluno(usuario);
        OffsetDateTime inicio = hoje().atStartOfDay().atOffset(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
        OffsetDateTime fim = inicio.plusDays(1);
        List<MissaoUsuario> missoes = missaoRepository
                .findByUsuarioAndDataReferenciaGreaterThanEqualAndDataReferenciaLessThan(usuario, inicio, fim);
        if (missoes.isEmpty()) {
            missoes = gerar(usuario, inicio);
        }
        for (MissaoUsuario missao : missoes) {
            atualizar(missao, inicio.toLocalDate());
        }
        return missoes.stream().map(this::paraDto).toList();
    }

    private List<MissaoUsuario> gerar(Usuario usuario, OffsetDateTime inicio) {
        return templateRepository.findByIsAtivoTrueOrderByIdAsc().stream().map(template -> {
            MissaoUsuario missao = new MissaoUsuario();
            missao.setUsuario(usuario);
            missao.setTemplate(template);
            missao.setDataReferencia(inicio);
            missao.setTexto(template.getTexto());
            int meta = template.getMetasPossiveis() == null || template.getMetasPossiveis().isEmpty()
                    ? 1
                    : template.getMetasPossiveis().get(ThreadLocalRandom.current()
                    .nextInt(template.getMetasPossiveis().size()));
            missao.setMeta(meta);
            missao.setRecompensa(template.getXpRecompensaBase());
            missao.setConcluida(false);
            return missaoRepository.save(missao);
        }).toList();
    }

    private void atualizar(MissaoUsuario missao, LocalDate data) {
        Usuario usuario = missao.getUsuario();
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        List<TentativaAluno> tentativas = tentativaRepository
                .findByUsuarioAndDataTentativaGreaterThanEqualAndDataTentativaLessThan(usuario, inicio, fim);
        int progresso = switch (missao.getTemplate().getTipoMissao()) {
            case RESPONDER_QUESTOES -> tentativas.size();
            case ACERTAR_QUESTOES -> (int) tentativas.stream().filter(TentativaAluno::isAcertou)
                    .map(t -> t.getQuestao().getId()).distinct().count();
            case OBTER_XP -> tentativas.stream().filter(TentativaAluno::isAcertou)
                    .map(t -> t.getQuestao().getXpRecompensa()).reduce(0, Integer::sum);
            case CONCLUIR_AULAS -> (int) progressoRepository.findByUsuario(usuario).stream()
                    .filter(item -> item.getPercentualConclusao() >= 100).count();
        };
        missao.setProgresso(Math.min(missao.getMeta(), progresso));
        if (!missao.isConcluida() && missao.getProgresso() >= missao.getMeta()) {
            missao.setConcluida(true);
            missao.setDataConclusao(OffsetDateTime.now());
            usuario.setXp(usuario.getXp() + missao.getRecompensa());
            usuario.setNivel(usuario.getXp() / 100 + 1);
            usuarioRepository.save(usuario);
        }
        missaoRepository.save(missao);
    }

    private MissaoDiariaDTO paraDto(MissaoUsuario missao) {
        return new MissaoDiariaDTO(missao.getId(), missao.getTexto(), missao.getTemplate().getTipoMissao(),
                missao.getMeta(), missao.getProgresso(), missao.getRecompensa(), missao.isConcluida(),
                missao.getDataReferencia(), missao.getDataConclusao());
    }

    private LocalDate hoje() {
        return LocalDate.now();
    }

    private void validarAluno(Usuario usuario) {
        if (usuario == null || usuario.getPerfil() != Perfil.ALUNO) {
            throw new IllegalArgumentException("Apenas alunos possuem missões diárias");
        }
    }
}
