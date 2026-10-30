package com.machado0.gamificacao.conquista;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ConquistaService {
    private final ConquistaRepository repository;
    private final DtoMapper mapper;
    public ConquistaService(ConquistaRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public ConquistaDto salvar(ConquistaDto dto) { return mapper.toConquistaDto(repository.save(mapper.toConquista(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
