package com.machado0.gamificacao.trilha;

import java.util.List;

public record TrilhaResponseDTO(
        Long id, String nome, String descricao, List<ModuloResponseDTO> modulos) {
}
