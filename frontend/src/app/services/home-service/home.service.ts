import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap } from 'rxjs';

import { DadosHome } from '../../models/models';
import { GamificacaoService } from '../gamificacao-service/gamificacao.service';

@Injectable({
  providedIn: 'root'
})
export class HomeService {
  private readonly http = inject(HttpClient);
  private readonly gamificacaoService = inject(GamificacaoService);

  obterDadosDashboard(): Observable<DadosHome> {
    return this.http.get<DadosHome>('assets/data/mock_usuario.json').pipe(
      switchMap(dados => this.gamificacaoService.carregar().pipe(
        map(estado => ({
          ...dados,
          estatisticas: estado.estatisticas,
          missoesDiarias: estado.missoes,
          progressoSemanal: estado.progressoSemanal
        }))
      ))
    );
  }
}