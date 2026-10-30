import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of, tap } from 'rxjs';
import { Notificacao } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class NotificacaoService {
  private readonly http = inject(HttpClient);
  private readonly chave = 'tcc_notificacoes';

  obterNotificacoes(): Observable<Notificacao[]> {
    const salvas = this.obterSalvas();

    if (salvas) {
      return of(this.ordenar(salvas));
    }

    return this.http
      .get<Notificacao[]>('assets/data/mock_notificacoes.json')
      .pipe(
        map(notificacoes => this.ordenar(notificacoes)),
        tap(notificacoes => this.persistir(notificacoes))
      );
  }

  marcarComoLida(id: number): Observable<Notificacao[]> {
    return this.obterNotificacoes().pipe(
      map(notificacoes => notificacoes.map(notificacao =>
        notificacao.id === id
          ? { ...notificacao, lida: true }
          : notificacao
      )),
      tap(notificacoes => this.persistir(notificacoes))
    );
  }

  marcarTodasComoLidas(): Observable<Notificacao[]> {
    return this.obterNotificacoes().pipe(
      map(notificacoes => notificacoes.map(notificacao => ({
        ...notificacao,
        lida: true
      }))),
      tap(notificacoes => this.persistir(notificacoes))
    );
  }

  obterNaoLidas(): Observable<number> {
    return this.obterNotificacoes().pipe(
      map(notificacoes => notificacoes.filter(notificacao => !notificacao.lida).length)
    );
  }

  private ordenar(notificacoes: Notificacao[]): Notificacao[] {
    return [...notificacoes].sort(
      (a, b) => new Date(b.criadaEm).getTime() - new Date(a.criadaEm).getTime()
    );
  }

  private obterSalvas(): Notificacao[] | null {
    const dados = localStorage.getItem(this.chave);

    if (!dados) {
      return null;
    }

    try {
      return JSON.parse(dados) as Notificacao[];
    } catch {
      localStorage.removeItem(this.chave);
      return null;
    }
  }

  private persistir(notificacoes: Notificacao[]): void {
    localStorage.setItem(this.chave, JSON.stringify(notificacoes));
  }
}
