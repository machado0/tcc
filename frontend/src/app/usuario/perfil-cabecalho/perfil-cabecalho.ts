import { Component, input, computed } from '@angular/core';
import { Usuario, EstatisticasUsuario } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-perfil-cabecalho',
  imports: [MatIconModule],
  templateUrl: './perfil-cabecalho.html',
  styleUrl: './perfil-cabecalho.css',
})
export class PerfilCabecalho {
  usuario = input.required<Usuario>();
  estatisticas = input.required<EstatisticasUsuario>();

  porcentagemNivel = computed(() => {
    const atual = this.estatisticas().xpAtual;
    const total = this.estatisticas().xpProximoNivel;
    if (!total || total === 0) return 0;
    return Math.min(Math.round((atual / total) * 100), 100);
  });

  inicialNome = computed(() => {
    const nome = this.usuario().nome;
    return nome ? nome.charAt(0).toUpperCase() : 'U';
  });
}

