package com.machado0.gamificacao.security;

import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            "VGhpcyBpcyBhIHRlc3Qgc2VjcmV0IHRoYXQgaXMgbG9uZyBlbm91Z2ggZm9yIEpXVA==";

    @Test
    void deveEmitirTokenComEmailEValidarToken() {
        JwtService service = new JwtService(SECRET, 3600);
        Usuario usuario = new Usuario();
        usuario.setEmail("aluno@example.com");
        usuario.setPerfil(Perfil.ALUNO);

        String token = service.gerarToken(usuario);

        assertEquals(usuario.getEmail(), service.extrairEmail(token));
        assertTrue(service.tokenValido(token, usuario.getEmail()));
        assertFalse(service.tokenValido(token, "outro@example.com"));
    }

    @Test
    void deveRecusarTokenMalformado() {
        JwtService service = new JwtService(SECRET, 3600);

        assertFalse(service.tokenValido("token-invalido", "aluno@example.com"));
    }
}
