import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, switchMap } from 'rxjs';
import { TurmaRanking } from '../../models/models';
import { GamificacaoService } from '../gamificacao-service/gamificacao.service';

@Injectable({
  providedIn: 'root'
})
export class RankingService {
  private readonly http = inject(HttpClient);
  private readonly gamificacaoService = inject(GamificacaoService);

  obterRankingTurma(): Observable<TurmaRanking> {
    return this.http.get<TurmaRanking>('assets/data/mock_ranking.json').pipe(
      switchMap(ranking => this.gamificacaoService.carregar().pipe(
        map(estado => this.aplicarRankingLocal(ranking, estado.rankingUsuario))
      ))
    );
  }

  private aplicarRankingLocal(
    ranking: TurmaRanking,
    usuarioAtualizado: TurmaRanking['alunos'][number] | null
  ): TurmaRanking {
    if (!usuarioAtualizado) {
      return ranking;
    }

    const alunos = ranking.alunos
      .map(aluno => aluno.usuarioId === usuarioAtualizado.usuarioId
        ? { ...aluno, ...usuarioAtualizado }
        : aluno)
      .sort((primeiro, segundo) => segundo.xpSemana - primeiro.xpSemana)
      .map((aluno, indice) => ({
        ...aluno,
        posicao: indice + 1
      }));
    const posicaoUsuarioLogado = alunos.findIndex(
      aluno => aluno.ehUsuarioLogado
    ) + 1;

    return {
      ...ranking,
      alunos,
      posicaoUsuarioLogado
    };
  }
}

