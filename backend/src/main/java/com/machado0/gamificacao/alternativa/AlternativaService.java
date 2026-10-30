package com.machado0.gamificacao.alternativa;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class AlternativaService {
    private final AlternativaRepository repository;
    private final DtoMapper mapper;
    public AlternativaService(AlternativaRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public AlternativaDto salvar(AlternativaDto dto) { return mapper.toAlternativaDto(repository.save(mapper.toAlternativa(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
