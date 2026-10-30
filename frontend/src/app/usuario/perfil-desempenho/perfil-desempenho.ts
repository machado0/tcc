import { Component, input, computed } from '@angular/core';
import { MetricasDesempenho } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-perfil-desempenho',
  imports: [MatIconModule],
  templateUrl: './perfil-desempenho.html',
  styleUrl: './perfil-desempenho.css',
})
export class PerfilDesempenho {
  metricas = input.required<MetricasDesempenho>();

  horasEstudadas = computed(() => {
    const minutos = this.metricas().tempoTotalEstudoMinutos;
    const horas = Math.floor(minutos / 60);
    const minsRestantes = minutos % 60;
    if (horas === 0) return `${minsRestantes}m`;
    return minsRestantes > 0 ? `${horas}h ${minsRestantes}m` : `${horas}h`;
  });
}

