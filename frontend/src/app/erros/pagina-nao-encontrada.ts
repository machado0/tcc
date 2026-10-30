import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-pagina-nao-encontrada',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  templateUrl: './pagina-nao-encontrada.html'
})
export class PaginaNaoEncontrada {}
