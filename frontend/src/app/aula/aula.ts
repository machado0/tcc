import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AulaService } from '../services/aula-service/aula.service';
import { DetalheAula } from '../models/models';
import { AulaCabecalho } from './aula-cabecalho/aula-cabecalho';
import { AulaTeoria } from './aula-teoria/aula-teoria';
import { AulaQuestoes } from './aula-questoes/aula-questoes';
import { MatIconModule } from '@angular/material/icon';
import { EstudoService } from '../services/estudo-service/estudo.service';

@Component({
  selector: 'app-aula',
  imports: [
    AulaCabecalho,
    AulaTeoria,
    AulaQuestoes,
    MatIconModule
  ],
  templateUrl: './aula.html',
  styleUrl: './aula.css',
})
export class Aula implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly aulaService = inject(AulaService);
  private readonly estudoService = inject(EstudoService);

  aula = signal<DetalheAula | null>(null);
  abaAtiva = signal<'TEORIA' | 'QUESTOES'>('TEORIA');
  carregando = signal(true);
  erro = signal<string | null>(null);
  conclusaoRegistrada = signal(false);
  private sessaoId: number | null = null;
  private sessaoEncerrada = false;
  private componenteDestruido = false;

  ngOnInit(): void {
    this.carregarAula();
  }

  carregarAula(): void {
    this.carregando.set(true);
    this.erro.set(null);

    const idParam = this.route.snapshot.paramMap.get('id');
    const aulaId = Number(idParam);

    if (!Number.isInteger(aulaId) || aulaId <= 0) {
      this.erro.set('A aula informada não é válida.');
      this.carregando.set(false);
      return;
    }

    this.aulaService.obterDetalheAula(aulaId).subscribe({
      next: (dados) => {
        this.aula.set(dados);
        this.estudoService.iniciar(dados.id).subscribe({
          next: sessao => {
            this.sessaoId = sessao.id;
            if (this.componenteDestruido) {
              this.finalizarSessao();
            }
          },
          error: err => console.error('Não foi possível iniciar a sessão de estudo:', err)
        });
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar a aula:', err);
        this.erro.set('Não foi possível carregar o conteúdo da aula.');
        this.carregando.set(false);
      }
    });
  }

  aoTrocarAba(aba: 'TEORIA' | 'QUESTOES'): void {
    this.abaAtiva.set(aba);
  }

  aoConcluirAula(resultado: { acertos: number; total: number; xpTotal: number }): void {
    const aulaAtual = this.aula();

    if (!aulaAtual) {
      return;
    }

    this.conclusaoRegistrada.set(true);
    this.finalizarSessao();
  }

  ngOnDestroy(): void {
    this.componenteDestruido = true;
    this.finalizarSessao();
  }

  private finalizarSessao(): void {
    if (this.sessaoId === null || this.sessaoEncerrada) return;
    this.sessaoEncerrada = true;
    this.estudoService.finalizar(this.sessaoId).subscribe({
      error: err => console.error('Não foi possível finalizar a sessão de estudo:', err)
    });
  }
}
