package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.conquista.usuario.ConquistaUsuario;
import com.machado0.gamificacao.conquista.usuario.ConquistaUsuarioRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConquistaConsultaService {
    private final ConquistaRepository conquistaRepository;
    private final ConquistaUsuarioRepository usuarioRepository;

    public ConquistaConsultaService(ConquistaRepository conquistaRepository,
                                    ConquistaUsuarioRepository usuarioRepository) {
        this.conquistaRepository = conquistaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ConquistaResponseDTO> listar(Usuario aluno) {
        if (aluno == null || aluno.getPerfil() != Perfil.ALUNO) {
            throw new ConquistaAcessoNegadoException();
        }
        var desbloqueadas = usuarioRepository.findByUsuarioOrderByDataDesbloqueioDesc(aluno)
                .stream().collect(java.util.stream.Collectors.toMap(
                        item -> item.getConquista().getId(), item -> item));
        return conquistaRepository.findAll().stream()
                .map(conquista -> paraResponse(conquista, desbloqueadas.get(conquista.getId())))
                .toList();
    }

    private ConquistaResponseDTO paraResponse(Conquista conquista, ConquistaUsuario desbloqueada) {
        return new ConquistaResponseDTO(
                conquista.getId(), null, conquista.getTitulo(), conquista.getDescricao(),
                conquista.getIcone(), conquista.getMeta(), conquista.getRecompensaXp(),
                desbloqueada != null && desbloqueada.isDesbloqueada(),
                desbloqueada == null ? null : desbloqueada.getDataDesbloqueio());
    }
}
