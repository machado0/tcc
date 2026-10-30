import { Component, inject, input, output, signal, computed } from '@angular/core';
import { QuestaoAula, OpcaoQuestao } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { AudioService } from '../../services/audio-service/audio.service';

@Component({
  selector: 'app-aula-questoes',
  imports: [MatIconModule, RouterLink],
  templateUrl: './aula-questoes.html',
  styleUrl: './aula-questoes.css',
})
export class AulaQuestoes {
  private readonly audioService = inject(AudioService);
  questoes = input.required<QuestaoAula[]>();
  aulaConcluida = output<{ acertos: number; total: number; xpTotal: number }>();

  indiceAtual = signal(0);
  opcaoSelecionadaId = signal<number | null>(null);
  respostaConfirmada = signal(false);
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

    const correta = this.opcaoSelecionadaId() === q.opcaoCorretaId;
    const xp = correta ? q.xpRecompensa : 0;
    this.audioService.tocar(correta ? 'acerto' : 'erro');

    if (correta) {
      this.acertos.update((v) => v + 1);
    }

    this.respostasHistorico.update((lista) => [
      ...lista,
      { questaoId: q.id, correta, xp }
    ]);

    this.respostaConfirmada.set(true);
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

