import { Component, input, output } from '@angular/core';
import { DetalheAula } from '../../models/models';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-aula-cabecalho',
  imports: [RouterLink, MatIconModule],
  templateUrl: './aula-cabecalho.html',
  styleUrl: './aula-cabecalho.css',
})
export class AulaCabecalho {
  aula = input.required<DetalheAula>();
  abaAtiva = input.required<'TEORIA' | 'QUESTOES'>();
  trocarAba = output<'TEORIA' | 'QUESTOES'>();

  selecionarAba(aba: 'TEORIA' | 'QUESTOES'): void {
    this.trocarAba.emit(aba);
  }
}

