import { Component, input } from '@angular/core';
import { ConfiguracoesUsuario } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-config-perfil-escolar',
  imports: [MatIconModule],
  templateUrl: './config-perfil-escolar.html',
  styleUrl: './config-perfil-escolar.css',
})
export class ConfigPerfilEscolar {
  config = input.required<ConfiguracoesUsuario>();
}

