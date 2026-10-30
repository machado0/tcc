import { Component, input, output } from '@angular/core';
import { Materia } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-seletor-materias',
  imports: [MatIconModule],
  templateUrl: './seletor-materias.html',
  styleUrl: './seletor-materias.css',
})
export class SeletorMaterias {
  materias = input.required<Materia[]>();
  materiaSelecionada = input.required<Materia>();
  selecionarMateria = output<Materia>();

  aoSelecionar(materia: Materia): void {
    if (materia.disponivel) {
      this.selecionarMateria.emit(materia);
    }
  }
}

