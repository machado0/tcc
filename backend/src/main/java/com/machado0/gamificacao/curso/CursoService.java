package com.machado0.gamificacao.curso;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class CursoService {
    private final CursoRepository repository;
    private final DtoMapper mapper;
    public CursoService(CursoRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public CursoDto salvar(CursoDto dto) { return mapper.toCursoDto(repository.save(mapper.toCurso(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
