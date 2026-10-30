package com.machado0.gamificacao.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    java.util.Optional<Curso> findByNome(String nome);
}
