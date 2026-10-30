import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { DadosHome } from '../../models/models';
import { GamificacaoService } from '../gamificacao-service/gamificacao.service';
import { AuthService } from '../auth-service/auth.service';

@Injectable({ providedIn: 'root' })
export class HomeService {
  private readonly gamificacaoService = inject(GamificacaoService);
  private readonly authService = inject(AuthService);

  obterDadosDashboard(): Observable<DadosHome> {
    return this.gamificacaoService.carregar().pipe(
      map(estado => ({
        usuario: this.authService.obterUsuario() ?? {
          id: estado.usuarioId,
          nome: 'Aluno',
          email: ''
        },
        estatisticas: estado.estatisticas,
        continuarEstudando: null,
        missoesDiarias: estado.missoes,
        progressoSemanal: estado.progressoSemanal,
        metricas: estado.metricas,
        conquistas: estado.conquistas
      }))
    );
  }
}
