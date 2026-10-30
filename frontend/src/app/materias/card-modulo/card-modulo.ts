import { Component, input } from '@angular/core';
import { ModuloMapa } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-card-modulo',
  imports: [MatIconModule, RouterLink],
  templateUrl: './card-modulo.html',
  styleUrl: './card-modulo.css',
})
export class CardModulo {
  modulo = input.required<ModuloMapa>();
}
