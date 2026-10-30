import { Component, input } from '@angular/core';
import { MissaoEstado } from '../../models/models';

@Component({
  selector: 'app-card-missoes-diarias',
  templateUrl: './home-missoes-diarias.html',
})
export class CardMissoesDiarias {
  missoes = input.required<MissaoEstado[]>();

  percentual(missao: MissaoEstado): number {
    return missao.meta > 0
      ? Math.min(100, Math.round((missao.progressoAtual / missao.meta) * 100))
      : 0;
  }
}