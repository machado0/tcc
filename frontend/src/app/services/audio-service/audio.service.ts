import { Injectable, inject } from '@angular/core';
import { ConfiguracaoService } from '../configuracao-service/configuracao.service';

export type TipoFeedbackAudio = 'acerto' | 'erro' | 'conquista';

@Injectable({
  providedIn: 'root'
})
export class AudioService {
  private readonly configuracaoService = inject(ConfiguracaoService);

  tocar(tipo: TipoFeedbackAudio): void {
    if (!this.sonsAtivos()) {
      return;
    }

    try {
      const contexto = new AudioContext();
      const oscilador = contexto.createOscillator();
      const ganho = contexto.createGain();
      const frequencia = tipo === 'acerto'
        ? 660
        : tipo === 'conquista'
          ? 880
          : 220;

      oscilador.frequency.value = frequencia;
      oscilador.type = 'sine';
      ganho.gain.setValueAtTime(0.08, contexto.currentTime);
      ganho.gain.exponentialRampToValueAtTime(0.001, contexto.currentTime + 0.18);
      oscilador.connect(ganho);
      ganho.connect(contexto.destination);
      oscilador.start();
      oscilador.stop(contexto.currentTime + 0.18);
      oscilador.addEventListener('ended', () => void contexto.close());
    } catch {
      
    }
  }

  private sonsAtivos(): boolean {
    return this.configuracaoService.configuracoes()?.efeitosSonoros !== false;
  }
}
