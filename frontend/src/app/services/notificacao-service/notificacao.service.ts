import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Notificacao } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class NotificacaoService {
  private readonly http = inject(HttpClient);
  obterNotificacoes(): Observable<Notificacao[]> {
    return this.http.get<Notificacao[]>('/api/v1/notificacoes')
      .pipe(map(notificacoes => this.ordenar(notificacoes)));
  }

  marcarComoLida(id: number): Observable<Notificacao[]> {
    return this.http.patch<Notificacao[]>(`/api/v1/notificacoes/${id}/lida`, {});
  }

  marcarTodasComoLidas(): Observable<Notificacao[]> {
    return this.http.post<Notificacao[]>('/api/v1/notificacoes/marcar-todas-lidas', {});
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

}
