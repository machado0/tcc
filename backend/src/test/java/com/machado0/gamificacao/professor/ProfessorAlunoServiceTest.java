package com.machado0.gamificacao.professor;

import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorAlunoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private ProfessorAlunoService service;
    private Usuario professor;

    @BeforeEach
    void setUp() {
        service = new ProfessorAlunoService(usuarioRepository, new BCryptPasswordEncoder());
        professor = new Usuario();
        professor.setId(10L);
        professor.setPerfil(Perfil.PROFESSOR);
        professor.setNome("Professor");
    }

    @Test
    void deveCadastrarAlunoComSenhaCriptografadaEEstadoInicial() {
        when(usuarioRepository.existsByEmail("aluno@example.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario aluno = invocation.getArgument(0);
            aluno.setId(20L);
            return aluno;
        });

        AlunoCadastradoResponseDTO response = service.cadastrarAluno(
                professor,
                new CadastrarAlunoRequestDTO(" Aluno ", " ALUNO@EXAMPLE.COM ", "Senha123!"));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario aluno = captor.getValue();

        assertEquals(20L, response.id());
        assertEquals("Aluno", aluno.getNome());
        assertEquals("aluno@example.com", aluno.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("Senha123!", aluno.getSenha()));
        assertEquals(Perfil.ALUNO, aluno.getPerfil());
        assertSame(professor, aluno.getProfessorResponsavel());
        assertEquals(3, aluno.getVidas());
        assertEquals(1, aluno.getNivel());
        assertEquals(0, aluno.getXp());
    }

    @Test
    void deveDesfazerLogicamenteLoteQuandoHouverEmailDuplicado() {
        when(usuarioRepository.existsByEmail("um@example.com")).thenReturn(false);
        when(usuarioRepository.existsByEmail("dois@example.com")).thenReturn(true);

        List<CadastrarAlunoRequestDTO> requests = List.of(
                new CadastrarAlunoRequestDTO("Um", "um@example.com", "Senha123!"),
                new CadastrarAlunoRequestDTO("Dois", "dois@example.com", "Senha123!"));

        assertThrows(EmailAlunoDuplicadoException.class,
                () -> service.cadastrarAlunosEmLote(professor, requests));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveBloquearAlunoComoUsuarioOperador() {
        Usuario aluno = new Usuario();
        aluno.setPerfil(Perfil.ALUNO);

        assertThrows(ProfessorObrigatorioException.class, () ->
                service.listarAlunos(aluno));
        verifyNoInteractions(usuarioRepository);
    }
}
