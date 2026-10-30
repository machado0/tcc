import { Component, inject, signal, OnInit } from '@angular/core';
import { RankingService } from '../services/ranking-service/ranking.service';
import { TurmaRanking } from '../models/models';
import { RankingPodio } from './ranking-podio/ranking-podio';
import { RankingTabela } from './ranking-tabela/ranking-tabela';
import { RankingResumoUsuario } from './ranking-resumo-usuario/ranking-resumo-usuario';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-ranking',
  imports: [
    RankingPodio,
    RankingTabela,
    RankingResumoUsuario,
    MatIconModule
  ],
  templateUrl: './ranking.html',
  styleUrl: './ranking.css',
})
export class Ranking implements OnInit {
  private readonly rankingService = inject(RankingService);

  turma = signal<TurmaRanking | null>(null);
  carregando = signal(true);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregarRanking();
  }

  carregarRanking(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.rankingService.obterRankingTurma().subscribe({
      next: (dados) => {
        this.turma.set(dados);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar o ranking:', err);
        this.erro.set('Não foi possível carregar a classificação da turma.');
        this.carregando.set(false);
      }
    });
  }
}

