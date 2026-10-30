import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ConfiguracoesUsuario } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class ConfiguracaoService {
  private readonly http = inject(HttpClient);
  private readonly configuracoesAtuais = signal<ConfiguracoesUsuario | null>(null);

  readonly configuracoes = this.configuracoesAtuais.asReadonly();

  aplicarConfiguracaoSalva(): void {
    if (localStorage.getItem('tcc_token')) {
      this.obterConfiguracoes().subscribe();
    }
  }

  obterConfiguracoes(): Observable<ConfiguracoesUsuario> {
    return this.http.get<ConfiguracoesUsuario>('/api/v1/configuracoes')
      .pipe(tap(configuracoes => {
        this.configuracoesAtuais.set(configuracoes);
        this.aplicarConfiguracoes(configuracoes);
      }));
  }

  salvarConfiguracoes(configuracoes: ConfiguracoesUsuario): Observable<ConfiguracoesUsuario> {
    return this.http.put<ConfiguracoesUsuario>('/api/v1/configuracoes', configuracoes)
      .pipe(tap(resposta => {
        this.configuracoesAtuais.set(resposta);
        this.aplicarConfiguracoes(resposta);
      }));
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

}
