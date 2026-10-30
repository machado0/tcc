import { Component, inject, input, output, signal, computed } from '@angular/core';
import { QuestaoAula, OpcaoQuestao } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { AudioService } from '../../services/audio-service/audio.service';
import { GamificacaoService } from '../../services/gamificacao-service/gamificacao.service';

@Component({
  selector: 'app-aula-questoes',
  imports: [MatIconModule, RouterLink],
  templateUrl: './aula-questoes.html',
  styleUrl: './aula-questoes.css',
})
export class AulaQuestoes {
  private readonly audioService = inject(AudioService);
  private readonly gamificacaoService = inject(GamificacaoService);
  questoes = input.required<QuestaoAula[]>();
  aulaConcluida = output<{ acertos: number; total: number; xpTotal: number }>();

  indiceAtual = signal(0);
  opcaoSelecionadaId = signal<number | null>(null);
  respostaConfirmada = signal(false);
  enviandoResposta = signal(false);
  acertos = signal(0);
  sessaoFinalizada = signal(false);
  respostasHistorico = signal<{ questaoId: number; correta: boolean; xp: number }[]>([]);

  questaoAtual = computed<QuestaoAula | null>(() => {
    return this.questoes()[this.indiceAtual()] ?? null;
  });

  ehUltimaQuestao = computed(() => {
    return this.indiceAtual() >= this.questoes().length - 1;
  });

  acertouResposta = computed(() => {
    const q = this.questaoAtual();
    return q ? this.opcaoSelecionadaId() === q.opcaoCorretaId : false;
  });

  xpGanhoTotal = computed(() => {
    return this.respostasHistorico().reduce((acc, item) => acc + item.xp, 0);
  });

  selecionarOpcao(opcao: OpcaoQuestao): void {
    if (this.respostaConfirmada()) return;
    this.opcaoSelecionadaId.set(opcao.id);
  }

  verificarResposta(): void {
    const q = this.questaoAtual();
    if (!q || this.opcaoSelecionadaId() === null || this.respostaConfirmada()) return;

    const alternativaId = this.opcaoSelecionadaId();
    if (alternativaId === null) return;
    this.enviandoResposta.set(true);
    this.gamificacaoService.submeterDesafio(q.id, alternativaId).subscribe({
      next: feedback => {
        this.audioService.tocar(feedback.acertou ? 'acerto' : 'erro');
        if (feedback.acertou) this.acertos.update(v => v + 1);
        this.respostasHistorico.update(lista => [
          ...lista,
          { questaoId: q.id, correta: feedback.acertou, xp: feedback.xpGanho }
        ]);
        this.respostaConfirmada.set(true);
        this.enviandoResposta.set(false);
      },
      error: () => this.enviandoResposta.set(false)
    });
  }

  avancar(): void {
    if (this.ehUltimaQuestao()) {
      this.sessaoFinalizada.set(true);
      this.aulaConcluida.emit({
        acertos: this.acertos(),
        total: this.questoes().length,
        xpTotal: this.xpGanhoTotal()
      });
    } else {
      this.indiceAtual.update((i) => i + 1);
      this.opcaoSelecionadaId.set(null);
      this.respostaConfirmada.set(false);
    }
  }

  reiniciar(): void {
    this.indiceAtual.set(0);
    this.opcaoSelecionadaId.set(null);
    this.respostaConfirmada.set(false);
    this.acertos.set(0);
    this.sessaoFinalizada.set(false);
    this.respostasHistorico.set([]);
  }
}
