package com.machado0.gamificacao.professor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastrarAlunoRequestDTO(
        @NotBlank @Size(max = 250) String nome,
        @NotBlank @Email @Size(max = 250) String email,
        @NotBlank @Size(min = 8, max = 72) String senhaInicial) {
}
