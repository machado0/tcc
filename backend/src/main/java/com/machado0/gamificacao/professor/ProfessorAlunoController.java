package com.machado0.gamificacao.professor;

import com.machado0.gamificacao.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professor/alunos")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorAlunoController {

    private final ProfessorAlunoService service;

    public ProfessorAlunoController(ProfessorAlunoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoCadastradoResponseDTO cadastrar(
            @AuthenticationPrincipal Usuario professor,
            @Valid @RequestBody CadastrarAlunoRequestDTO request) {
        return service.cadastrarAluno(professor, request);
    }

    @PostMapping("/lote")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AlunoCadastradoResponseDTO> cadastrarEmLote(
            @AuthenticationPrincipal Usuario professor,
            @Valid @RequestBody List<@Valid CadastrarAlunoRequestDTO> requests) {
        return service.cadastrarAlunosEmLote(professor, requests);
    }

    @GetMapping
    public List<AlunoResumoResponseDTO> listar(@AuthenticationPrincipal Usuario professor) {
        return service.listarAlunos(professor);
    }
}
