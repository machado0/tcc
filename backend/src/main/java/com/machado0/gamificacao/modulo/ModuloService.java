package com.machado0.gamificacao.modulo;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ModuloService {
    private final ModuloRepository repository;
    private final DtoMapper mapper;
    public ModuloService(ModuloRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public ModuloDto salvar(ModuloDto dto) { return mapper.toModuloDto(repository.save(mapper.toModulo(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
