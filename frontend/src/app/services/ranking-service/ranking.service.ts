import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { TurmaRanking } from '../../models/models';

interface BackendRanking {
  turmaId: number;
  turmaNome: string;
  alunos: Array<{ posicao: number; usuarioId: number; nome: string; xp: number; nivel: number; streak: number }>;
  meuPerfil?: { posicao: number; usuarioId: number; nome: string; xp: number; nivel: number; streak: number };
}

@Injectable({ providedIn: 'root' })
export class RankingService {
  private readonly http = inject(HttpClient);

  obterRankingTurma(): Observable<TurmaRanking> {
    return this.http.get<BackendRanking>('/api/v1/ranking').pipe(
      map(ranking => ({
        id: ranking.turmaId,
        nomeTurma: ranking.turmaNome,
        periodo: '',
        terminaEmDias: 0,
        totalAlunos: ranking.alunos.length,
        posicaoUsuarioLogado: ranking.meuPerfil?.posicao ?? 0,
        alunos: ranking.alunos.map(aluno => ({
          posicao: aluno.posicao,
          usuarioId: aluno.usuarioId,
          nome: aluno.nome,
          username: aluno.nome,
          nivel: aluno.nivel,
          xpSemana: aluno.xp,
          sequenciaDias: aluno.streak,
          tendencia: 'MANTEVE' as const,
          ehUsuarioLogado: aluno.usuarioId === ranking.meuPerfil?.usuarioId
        }))
      }))
    );
  }
}
