import { Component, input, computed } from '@angular/core';
import { Materia, AulaMapa, ModuloMapa } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-resumo-materia',
  imports: [MatIconModule, RouterLink],
  templateUrl: './resumo-materia.html',
  styleUrl: './resumo-materia.css',
})
export class ResumoMateria {
  materia = input.required<Materia>();

  proximaAula = computed<{ aula: AulaMapa; modulo: ModuloMapa } | null>(() => {
    for (const mod of this.materia().modulos) {
      const aula = mod.aulas.find((a) => a.status === 'DISPONIVEL');
      if (aula) {
        return { aula, modulo: mod };
      }
    }
    return null;
  });

  totalXpDisponivel = computed(() => {
    let total = 0;
    for (const mod of this.materia().modulos) {
      for (const aula of mod.aulas) {
        total += aula.xpRecompensa;
      }
    }
    return total;
  });
}
