package com.machado0.gamificacao.questao;

import com.machado0.gamificacao.mapper.DtoMapper;
import org.springframework.stereotype.Service;

@Service
public class QuestaoService {
    private final QuestaoRepository repository;
    private final DtoMapper mapper;
    public QuestaoService(QuestaoRepository repository, DtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public QuestaoDto salvar(QuestaoDto dto) { return mapper.toQuestaoDto(repository.save(mapper.toQuestao(dto))); }
    public void excluir(Long id) { repository.deleteById(id); }
}
