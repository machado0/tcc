import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AulaService } from '../services/aula-service/aula.service';
import { DetalheAula } from '../models/models';
import { AulaCabecalho } from './aula-cabecalho/aula-cabecalho';
import { AulaTeoria } from './aula-teoria/aula-teoria';
import { AulaQuestoes } from './aula-questoes/aula-questoes';
import { MatIconModule } from '@angular/material/icon';
import { ProgressoService } from '../services/progresso-service/progresso.service';
import { GamificacaoService } from '../services/gamificacao-service/gamificacao.service';

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
export class Aula implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly aulaService = inject(AulaService);
  private readonly progressoService = inject(ProgressoService);
  private readonly gamificacaoService = inject(GamificacaoService);

  aula = signal<DetalheAula | null>(null);
  abaAtiva = signal<'TEORIA' | 'QUESTOES'>('TEORIA');
  carregando = signal(true);
  erro = signal<string | null>(null);
  conclusaoRegistrada = signal(false);

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

    this.progressoService.registrarConclusao(aulaAtual.id, resultado);
    this.gamificacaoService.registrarConclusao({
      aulaId: aulaAtual.id,
      moduloId: aulaAtual.moduloId,
      acertos: resultado.acertos,
      total: resultado.total,
      xpTotal: resultado.xpTotal
    }).subscribe({
      next: () => this.conclusaoRegistrada.set(true),
      error: erro => console.error('Erro ao atualizar a gamificação:', erro)
    });
  }
}

