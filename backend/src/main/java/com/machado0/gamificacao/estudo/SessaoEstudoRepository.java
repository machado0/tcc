package com.machado0.gamificacao.estudo;

import com.machado0.gamificacao.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessaoEstudoRepository extends JpaRepository<SessaoEstudo, Long> {
    List<SessaoEstudo> findByUsuario(Usuario usuario);
    Optional<SessaoEstudo> findByIdAndUsuario(Long id, Usuario usuario);
    Optional<SessaoEstudo> findFirstByUsuarioAndAula_IdAndConcluidaFalseOrderByInicioAsc(
            Usuario usuario, Long aulaId);
    List<SessaoEstudo> findByUsuarioAndInicioGreaterThanEqualAndInicioLessThan(
            Usuario usuario, LocalDateTime inicio, LocalDateTime fim);
    long countByUsuarioAndInicioGreaterThanEqualAndInicioLessThan(
            Usuario usuario, LocalDateTime inicio, LocalDateTime fim);
}
