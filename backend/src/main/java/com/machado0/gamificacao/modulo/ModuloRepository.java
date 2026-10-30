package com.machado0.gamificacao.modulo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.machado0.gamificacao.curso.Curso;
import java.util.List;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    List<Modulo> findByCursoOrderByOrdemAsc(Curso curso);
}
