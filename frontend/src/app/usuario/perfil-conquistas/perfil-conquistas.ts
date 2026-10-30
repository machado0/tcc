import { Component, input, signal, computed } from '@angular/core';
import { Conquista } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

type FiltroConquista = 'TODAS' | 'DESBLOQUEADAS' | 'BLOQUEADAS';

@Component({
  selector: 'app-perfil-conquistas',
  imports: [MatIconModule],
  templateUrl: './perfil-conquistas.html',
  styleUrl: './perfil-conquistas.css',
})
export class PerfilConquistas {
  conquistas = input.required<Conquista[]>();

  filtroAtual = signal<FiltroConquista>('TODAS');

  totalDesbloqueadas = computed(() => {
    return this.conquistas().filter((c) => c.desbloqueada).length;
  });

  conquistasFiltradas = computed(() => {
    const filtro = this.filtroAtual();
    const lista = this.conquistas();

    if (filtro === 'DESBLOQUEADAS') {
      return lista.filter((c) => c.desbloqueada);
    }
    if (filtro === 'BLOQUEADAS') {
      return lista.filter((c) => !c.desbloqueada);
    }
    return lista;
  });

  definirFiltro(novoFiltro: FiltroConquista): void {
    this.filtroAtual.set(novoFiltro);
  }

  calcularProgresso(conquista: Conquista): number {
    if (conquista.desbloqueada) return 100;
    if (!conquista.meta || conquista.meta === 0) return 0;
    const pct = Math.round((conquista.progressoAtual / conquista.meta) * 100);
    return Math.min(pct, 100);
  }
}

