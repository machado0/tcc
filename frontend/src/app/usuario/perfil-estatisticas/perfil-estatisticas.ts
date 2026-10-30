import { Component, input } from '@angular/core';
import { EstatisticasUsuario } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-perfil-estatisticas',
  imports: [MatIconModule],
  templateUrl: './perfil-estatisticas.html',
  styleUrl: './perfil-estatisticas.css',
})
export class PerfilEstatisticas {
  estatisticas = input.required<EstatisticasUsuario>();
}

