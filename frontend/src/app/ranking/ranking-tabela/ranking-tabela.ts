import { Component, input } from '@angular/core';
import { AlunoRanking } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-ranking-tabela',
  imports: [MatIconModule],
  templateUrl: './ranking-tabela.html',
  styleUrl: './ranking-tabela.css',
})
export class RankingTabela {
  alunos = input.required<AlunoRanking[]>();

  inicial(nome: string): string {
    return nome ? nome.charAt(0).toUpperCase() : '?';
  }
}

