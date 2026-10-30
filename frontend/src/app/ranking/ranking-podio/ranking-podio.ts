import { Component, input, computed } from '@angular/core';
import { AlunoRanking } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-ranking-podio',
  imports: [MatIconModule],
  templateUrl: './ranking-podio.html',
  styleUrl: './ranking-podio.css',
})
export class RankingPodio {
  alunos = input.required<AlunoRanking[]>();

  primeiro = computed(() => this.alunos().find((a) => a.posicao === 1) ?? null);
  segundo = computed(() => this.alunos().find((a) => a.posicao === 2) ?? null);
  terceiro = computed(() => this.alunos().find((a) => a.posicao === 3) ?? null);

  inicial(nome?: string): string {
    return nome ? nome.charAt(0).toUpperCase() : '?';
  }
}

