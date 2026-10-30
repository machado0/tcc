import { Component, input } from '@angular/core';
import { EstatisticasUsuario } from '../../models/models';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-home-progresso',
  imports: [MatProgressBarModule],
  templateUrl: './home-progresso.html',
})
export class CardProgresso {
  estatisticas = input.required<EstatisticasUsuario>();

  get progressoNivel(): number {
    const xpAtual = this.estatisticas().xpAtual;
    const xpTotal = this.estatisticas().xpProximoNivel;

    if (xpAtual === 0) return 0;
    
    const valor = (xpAtual / xpTotal) * 100;
    return valor > 100 ? 100 : valor;
  }
}
