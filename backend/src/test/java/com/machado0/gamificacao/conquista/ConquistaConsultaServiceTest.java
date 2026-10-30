package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.conquista.usuario.ConquistaUsuarioRepository;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ConquistaConsultaServiceTest {
    @Mock
    private ConquistaRepository conquistaRepository;
    @Mock
    private ConquistaUsuarioRepository usuarioRepository;

    @Test
    void deveBloquearConsultaPorProfessor() {
        Usuario professor = new Usuario();
        professor.setPerfil(Perfil.PROFESSOR);

        assertThrows(ConquistaAcessoNegadoException.class,
                () -> new ConquistaConsultaService(conquistaRepository, usuarioRepository)
                        .listar(professor));
        verifyNoInteractions(conquistaRepository, usuarioRepository);
    }
}
