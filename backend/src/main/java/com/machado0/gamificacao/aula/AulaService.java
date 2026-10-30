package com.machado0.gamificacao.aula;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class AulaService {
    private final AulaRepository repository;
    private final DtoMapper mapper;
    public AulaService(AulaRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public AulaDto salvar(AulaDto dto) { return mapper.toAulaDto(repository.save(mapper.toAula(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
