package com.machado0.gamificacao.progresso;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ProgressoService {
    private final ProgressoRepository repository;
    private final DtoMapper mapper;
    public ProgressoService(ProgressoRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public ProgressoDto salvar(ProgressoDto dto) { return mapper.toProgressoDto(repository.save(mapper.toProgresso(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
