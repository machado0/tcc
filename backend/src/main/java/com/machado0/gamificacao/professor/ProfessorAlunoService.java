package com.machado0.gamificacao.professor;

import com.machado0.gamificacao.usuario.Perfil;
import com.machado0.gamificacao.usuario.Usuario;
import com.machado0.gamificacao.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ProfessorAlunoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorAlunoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AlunoCadastradoResponseDTO cadastrarAluno(
            Usuario professor,
            CadastrarAlunoRequestDTO request) {
        validarProfessor(professor);
        String email = normalizarEmail(request.email());
        verificarEmailDisponivel(email);
        return paraCadastrado(usuarioRepository.save(novoAluno(professor, request, email)));
    }

    @Transactional
    public List<AlunoCadastradoResponseDTO> cadastrarAlunosEmLote(
            Usuario professor,
            List<CadastrarAlunoRequestDTO> requests) {
        validarProfessor(professor);
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("A lista de alunos nao pode ser vazia");
        }

        Set<String> emailsDoLote = new HashSet<>();
        for (CadastrarAlunoRequestDTO request : requests) {
            String email = normalizarEmail(request.email());
            if (!emailsDoLote.add(email) || usuarioRepository.existsByEmail(email)) {
                throw new EmailAlunoDuplicadoException(email);
            }
        }

        return requests.stream()
                .map(request -> novoAluno(professor, request, normalizarEmail(request.email())))
                .map(usuarioRepository::save)
                .map(this::paraCadastrado)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlunoResumoResponseDTO> listarAlunos(Usuario professor) {
        validarProfessor(professor);
        return usuarioRepository.findByProfessorResponsavelOrderByNomeAsc(professor)
                .stream()
                .map(aluno -> new AlunoResumoResponseDTO(
                        aluno.getId(), aluno.getNome(), aluno.getEmail(),
                        aluno.getXp(), aluno.getNivel(), aluno.getStreak()))
                .toList();
    }

    private Usuario novoAluno(
            Usuario professor,
            CadastrarAlunoRequestDTO request,
            String email) {
        Usuario aluno = new Usuario();
        aluno.setNome(request.nome().trim());
        aluno.setEmail(email);
        aluno.setSenha(passwordEncoder.encode(request.senhaInicial()));
        aluno.setPerfil(Perfil.ALUNO);
        aluno.setProfessorResponsavel(professor);
        aluno.setUsername(email);
        aluno.setTituloHonra("Estudante Dedicado");
        aluno.setXp(0);
        aluno.setNivel(1);
        aluno.setVidas(3);
        aluno.setMoedas(0);
        aluno.setStreak(0);
        aluno.setBestStreak(0);
        return aluno;
    }

    private void validarProfessor(Usuario usuario) {
        if (usuario == null || usuario.getPerfil() != Perfil.PROFESSOR) {
            throw new ProfessorObrigatorioException();
        }
    }

    private void verificarEmailDisponivel(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailAlunoDuplicadoException(email);
        }
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private AlunoCadastradoResponseDTO paraCadastrado(Usuario aluno) {
        return new AlunoCadastradoResponseDTO(
                aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getDataCriacao());
    }
}
