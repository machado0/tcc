import { Component, input, computed } from '@angular/core';
import { TurmaRanking, AlunoRanking } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-ranking-resumo-usuario',
  imports: [MatIconModule],
  templateUrl: './ranking-resumo-usuario.html',
  styleUrl: './ranking-resumo-usuario.css',
})
export class RankingResumoUsuario {
  turma = input.required<TurmaRanking>();

  alunoLogado = computed<AlunoRanking | null>(() => {
    return this.turma().alunos.find((a) => a.ehUsuarioLogado) ?? null;
  });

  alunoAcima = computed<AlunoRanking | null>(() => {
    const logado = this.alunoLogado();
    if (!logado || logado.posicao <= 1) return null;
    return this.turma().alunos.find((a) => a.posicao === logado.posicao - 1) ?? null;
  });

  xpParaProximaPosicao = computed(() => {
    const acima = this.alunoAcima();
    const logado = this.alunoLogado();
    if (!acima || !logado) return 0;
    return Math.max(acima.xpSemana - logado.xpSemana, 0);
  });
}

