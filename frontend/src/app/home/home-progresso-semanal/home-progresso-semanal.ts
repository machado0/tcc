import { Component, input, computed } from '@angular/core';

import { ProgressoSemanal } from '../../models/models';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-card-progresso-semanal',
  imports: [ DecimalPipe ],
  templateUrl: './home-progresso-semanal.html',
})
export class CardProgressoSemanal {
  progresso = input.required<ProgressoSemanal>();

  maiorXpSemana = computed(() => {
    const dias = this.progresso().dias;
    return Math.max(...dias.map((dia) => dia.xp), 1);
  });

  calcularAlturaBarra(xpGanho: number): number {

    const maiorXp =
      this.maiorXpSemana();

    if (xpGanho === 0) {
      return 0;
    }

    return (xpGanho / maiorXp) * 100;
  }
}