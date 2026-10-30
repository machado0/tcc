import { Component, input, output } from '@angular/core';
import { ConteudoTeorico } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-aula-teoria',
  imports: [MatIconModule],
  templateUrl: './aula-teoria.html',
  styleUrl: './aula-teoria.css',
})
export class AulaTeoria {
  conteudo = input.required<ConteudoTeorico>();
  irParaExercicios = output<void>();

  iniciarExercicios(): void {
    this.irParaExercicios.emit();
  }
}

