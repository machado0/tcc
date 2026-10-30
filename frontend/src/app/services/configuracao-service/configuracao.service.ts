import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of, tap } from 'rxjs';
import { ConfiguracoesUsuario } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class ConfiguracaoService {
  private readonly http = inject(HttpClient);
  private readonly chave = 'tcc_configuracoes_usuario';

  aplicarConfiguracaoSalva(): void {
    const dadosSalvos = localStorage.getItem(this.chave);

    if (!dadosSalvos) {
      return;
    }

    try {
      this.aplicarConfiguracoes(JSON.parse(dadosSalvos) as ConfiguracoesUsuario);
    } catch {
      localStorage.removeItem(this.chave);
    }
  }

  obterConfiguracoes(): Observable<ConfiguracoesUsuario> {
    return this.http
      .get<ConfiguracoesUsuario>('assets/data/mock_configuracoes.json')
      .pipe(
        map(configuracoes => this.obterConfiguracaoSalva(configuracoes)),
        tap(configuracoes => this.aplicarConfiguracoes(configuracoes))
      );
  }

  salvarConfiguracoes(configuracoes: ConfiguracoesUsuario): Observable<ConfiguracoesUsuario> {
    localStorage.setItem(this.chave, JSON.stringify(configuracoes));
    this.aplicarConfiguracoes(configuracoes);
    return of(configuracoes);
  }

  aplicarConfiguracoes(configuracoes: ConfiguracoesUsuario): void {
    const elemento = document.documentElement;
    const temaEscuro = configuracoes.tema === 'escuro' ||
      (configuracoes.tema === 'sistema' &&
        window.matchMedia('(prefers-color-scheme: dark)').matches);

    elemento.classList.toggle('dark', temaEscuro);
    elemento.classList.toggle('high-contrast', configuracoes.altoContraste);
    elemento.classList.toggle('font-large', configuracoes.tamanhoFonte === 'grande');
    elemento.classList.toggle('reduce-motion', !configuracoes.animacoesGamificacao);
  }

  private obterConfiguracaoSalva(
    configuracoesPadrao: ConfiguracoesUsuario
  ): ConfiguracoesUsuario {
    const dadosSalvos = localStorage.getItem(this.chave);

    if (!dadosSalvos) {
      return configuracoesPadrao;
    }

    try {
      return JSON.parse(dadosSalvos) as ConfiguracoesUsuario;
    } catch {
      localStorage.removeItem(this.chave);
      return configuracoesPadrao;
    }
  }
}

