package com.machado0.gamificacao.missao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateMissaoRepository extends JpaRepository<TemplateMissao, Long> {
    List<TemplateMissao> findByIsAtivoTrueOrderByIdAsc();
}
