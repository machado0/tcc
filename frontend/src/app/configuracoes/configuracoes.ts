import { Component, inject, signal, OnInit } from '@angular/core';
import { ConfiguracaoService } from '../services/configuracao-service/configuracao.service';
import { ConfiguracoesUsuario } from '../models/models';
import { ConfigPerfilEscolar } from './config-perfil-escolar/config-perfil-escolar';
import { ConfigPreferencias } from './config-preferencias/config-preferencias';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-configuracoes',
  imports: [
    ConfigPerfilEscolar,
    ConfigPreferencias,
    MatIconModule
  ],
  templateUrl: './configuracoes.html',
  styleUrl: './configuracoes.css',
})
export class Configuracoes implements OnInit {
  private readonly configuracaoService = inject(ConfiguracaoService);

  config = signal<ConfiguracoesUsuario | null>(null);
  carregando = signal(true);
  erro = signal<string | null>(null);
  mensagemSalvo = signal<string | null>(null);

  ngOnInit(): void {
    this.carregarConfiguracoes();
  }

  carregarConfiguracoes(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.configuracaoService.obterConfiguracoes().subscribe({
      next: (dados) => {
        this.config.set(dados);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar configurações:', err);
        this.erro.set('Não foi possível carregar as configurações do sistema.');
        this.carregando.set(false);
      }
    });
  }

  aoSalvar(atualizado: ConfiguracoesUsuario): void {
    this.configuracaoService.salvarConfiguracoes(atualizado).subscribe({
      next: configuracoes => {
        this.config.set(configuracoes);
        this.mensagemSalvo.set('Preferências salvas com sucesso!');
        setTimeout(() => {
          this.mensagemSalvo.set(null);
        }, 3500);
      },
      error: () => {
        this.erro.set('Não foi possível salvar as configurações.');
      }
    });
  }
}

