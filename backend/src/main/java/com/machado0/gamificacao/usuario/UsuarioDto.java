package com.machado0.gamificacao.usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class UsuarioDto {

    private Long id;

    private String nome;

    private String username;

    private String email;

    private String senha;

    private String tituloHonra;

    private OffsetDateTime dataCriacao;
}

