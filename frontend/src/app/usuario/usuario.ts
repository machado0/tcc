import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../services/usuario-service/usuario.service';
import { PerfilUsuario } from '../models/models';
import { PerfilCabecalho } from './perfil-cabecalho/perfil-cabecalho';
import { PerfilEstatisticas } from './perfil-estatisticas/perfil-estatisticas';
import { PerfilDesempenho } from './perfil-desempenho/perfil-desempenho';
import { PerfilConquistas } from './perfil-conquistas/perfil-conquistas';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-usuario',
  imports: [
    FormsModule,
    PerfilCabecalho,
    PerfilEstatisticas,
    PerfilDesempenho,
    PerfilConquistas,
    MatIconModule
  ],
  templateUrl: './usuario.html',
  styleUrl: './usuario.css',
})
export class Usuario implements OnInit {
  private readonly usuarioService = inject(UsuarioService);

  perfil = signal<PerfilUsuario | null>(null);
  carregando = signal(true);
  erro = signal<string | null>(null);
  editando = signal(false);
  salvando = signal(false);
  mensagemSalva = signal<string | null>(null);
  nomeEditado = '';
  usernameEditado = '';
  emailEditado = '';

  ngOnInit(): void {
    this.carregarPerfil();
  }

  carregarPerfil(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.usuarioService.obterPerfil().subscribe({
      next: (dados) => {
        this.perfil.set(dados);
        this.nomeEditado = dados.usuario.nome;
        this.usernameEditado = dados.usuario.username ?? '';
        this.emailEditado = dados.usuario.email ?? '';
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar o perfil:', err);
        this.erro.set('Não foi possível carregar as informações do usuário.');
        this.carregando.set(false);
      }
    });
  }

  iniciarEdicao(): void {
    this.mensagemSalva.set(null);
    this.editando.set(true);
  }

  cancelarEdicao(): void {
    const usuario = this.perfil()?.usuario;

    if (usuario) {
      this.nomeEditado = usuario.nome;
      this.usernameEditado = usuario.username ?? '';
      this.emailEditado = usuario.email ?? '';
    }

    this.editando.set(false);
  }

  salvarPerfil(): void {
    const perfilAtual = this.perfil();

    if (!perfilAtual || !this.nomeEditado.trim() || !this.emailEditado.trim()) {
      this.erro.set('Nome e e-mail são obrigatórios.');
      return;
    }

    this.salvando.set(true);
    this.erro.set(null);

    this.usuarioService.atualizarPerfil({
      ...perfilAtual.usuario,
      nome: this.nomeEditado.trim(),
      username: this.usernameEditado.trim() || undefined,
      email: this.emailEditado.trim()
    }).subscribe({
      next: usuario => {
        this.perfil.update(valor => valor
          ? { ...valor, usuario }
          : valor
        );
        this.editando.set(false);
        this.salvando.set(false);
        this.mensagemSalva.set('Perfil atualizado com sucesso.');
      },
      error: () => {
        this.erro.set('Não foi possível salvar o perfil.');
        this.salvando.set(false);
      }
    });
  }
}

