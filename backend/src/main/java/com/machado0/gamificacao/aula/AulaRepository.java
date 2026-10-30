package com.machado0.gamificacao.aula;

import org.springframework.data.jpa.repository.JpaRepository;
import com.machado0.gamificacao.modulo.Modulo;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByModuloOrderByOrdemAsc(Modulo modulo);
}
