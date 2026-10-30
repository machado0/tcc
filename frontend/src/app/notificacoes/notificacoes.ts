import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { Notificacao, TipoNotificacao } from '../models/models';
import { NotificacaoService } from '../services/notificacao-service/notificacao.service';

@Component({
  selector: 'app-notificacoes',
  standalone: true,
  imports: [DatePipe, MatIconModule],
  templateUrl: './notificacoes.html'
})
export class Notificacoes {
  private readonly notificacaoService = inject(NotificacaoService);
  private readonly router = inject(Router);

  notificacoes = signal<Notificacao[]>([]);
  carregando = signal(true);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.notificacaoService.obterNotificacoes().subscribe({
      next: notificacoes => {
        this.notificacoes.set(notificacoes);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Não foi possível carregar as notificações.');
        this.carregando.set(false);
      }
    });
  }

  marcarTodasComoLidas(): void {
    this.notificacaoService.marcarTodasComoLidas().subscribe(notificacoes => {
      this.notificacoes.set(notificacoes);
    });
  }

  abrir(notificacao: Notificacao): void {
    this.notificacaoService.marcarComoLida(notificacao.id).subscribe(notificacoes => {
      this.notificacoes.set(notificacoes);

      if (notificacao.rota) {
        this.router.navigateByUrl(notificacao.rota);
      }
    });
  }

  icone(tipo: TipoNotificacao): string {
    switch (tipo) {
      case 'CONQUISTA':
        return 'emoji_events';
      case 'MISSAO':
        return 'flag';
      case 'AULA':
        return 'school';
      default:
        return 'info';
    }
  }

  temNaoLidas(): boolean {
    return this.notificacoes().some(notificacao => !notificacao.lida);
  }
}