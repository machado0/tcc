import { Component, input } from '@angular/core';
import { Usuario } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-saudacao',
  imports: [MatIconModule],
  templateUrl: './home-saudacao.html',
})
export class Saudacao {
  usuario = input.required<Usuario>();

  getSaudacao(): string {
    const hora = new Date().getHours();

    if (hora < 12) return 'Bom dia';
    if (hora < 18) return 'Boa tarde';

    return 'Boa noite';
  }

  getMensagem(): string {
    return "Pronto para continuar sua jornada?";
  }
}
