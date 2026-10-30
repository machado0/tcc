package com.machado0.gamificacao.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.machado0.gamificacao.mapper.DtoMapper;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioService(UsuarioRepository repository, DtoMapper mapper) {
        this(repository);
    }

    public UsuarioDto salvar(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha() == null ? "" : dto.getSenha());
        usuario.setTituloHonra(dto.getTituloHonra());
        Usuario salvo = repository.save(usuario);

        UsuarioDto resposta = new UsuarioDto();
        resposta.setId(salvo.getId());
        resposta.setNome(salvo.getNome());
        resposta.setUsername(salvo.getUsername());
        resposta.setEmail(salvo.getEmail());
        resposta.setSenha(salvo.getSenha());
        resposta.setTituloHonra(salvo.getTituloHonra());
        resposta.setDataCriacao(salvo.getDataCriacao());
        return resposta;
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }


}
