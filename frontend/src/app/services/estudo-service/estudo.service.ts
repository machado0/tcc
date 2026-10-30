import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessaoEstudo } from '../../models/models';

@Injectable({ providedIn: 'root' })
export class EstudoService {
  private readonly http = inject(HttpClient);

  iniciar(aulaId: number): Observable<SessaoEstudo> {
    return this.http.post<SessaoEstudo>('/api/v1/sessoes-estudo', { aulaId });
  }

  finalizar(sessaoId: number): Observable<SessaoEstudo> {
    return this.http.post<SessaoEstudo>(`/api/v1/sessoes-estudo/${sessaoId}/finalizar`, {});
  }
}
