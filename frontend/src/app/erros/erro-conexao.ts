import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-erro-conexao',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  templateUrl: './erro-conexao.html'
})
export class ErroConexao {
  recarregar(): void {
    window.location.reload();
  }
}
