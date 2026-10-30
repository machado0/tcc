package com.machado0.gamificacao.auth;

import com.machado0.gamificacao.security.JwtService;
import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private JwtService jwtService;

    private AuthService service;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        service = new AuthService(usuarioRepository, new BCryptPasswordEncoder(), jwtService);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Ana");
        usuario.setEmail("ana@example.com");
        usuario.setSenha(new BCryptPasswordEncoder().encode("Senha123!"));
        usuario.setPerfil(Perfil.ALUNO);
    }

    @Test
    void deveAutenticarEAtualizarUltimoAcesso() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(jwtService.gerarToken(usuario)).thenReturn("token");

        LoginResponseDTO response = service.autenticar(
                new LoginRequestDTO(usuario.getEmail(), "Senha123!"));

        assertEquals("token", response.token());
        assertEquals("Bearer", response.tipo());
        assertEquals("ALUNO", response.perfil());
        assertNotNull(usuario.getUltimoAcesso());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveRecusarSenhaInvalidaSemSalvarUsuario() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(CredenciaisInvalidasException.class, () ->
                service.autenticar(new LoginRequestDTO(usuario.getEmail(), "errada")));

        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(jwtService);
    }
}
