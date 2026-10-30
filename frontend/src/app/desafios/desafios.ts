import { Component, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { GamificacaoService } from '../services/gamificacao-service/gamificacao.service';
import { MissaoEstado } from '../models/models';

@Component({
  selector: 'app-desafios',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './desafios.html',
  styleUrl: './desafios.css'
})
export class Desafios {
  private readonly gamificacaoService = inject(GamificacaoService);

  estado = this.gamificacaoService.estado;
  carregando = signal(true);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.gamificacaoService.carregar().subscribe({
      next: () => this.carregando.set(false),
      error: () => {
        this.erro.set('Não foi possível carregar seus desafios.');
        this.carregando.set(false);
      }
    });
  }

  percentualMissao(missao: MissaoEstado): number {
    if (missao.meta <= 0) {
      return 0;
    }

    return Math.min(100, Math.round((missao.progressoAtual / missao.meta) * 100));
  }
}
