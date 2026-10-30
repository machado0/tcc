import { Component, input, output, signal, OnInit } from '@angular/core';
import { ConfiguracoesUsuario } from '../../models/models';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-config-preferencias',
  imports: [MatIconModule],
  templateUrl: './config-preferencias.html',
  styleUrl: './config-preferencias.css',
})
export class ConfigPreferencias implements OnInit {
  config = input.required<ConfiguracoesUsuario>();
  salvarPreferencias = output<ConfiguracoesUsuario>();

  tema = signal<'claro' | 'escuro' | 'sistema'>('sistema');
  tamanhoFonte = signal<'padrao' | 'grande'>('padrao');
  altoContraste = signal(false);
  notificarTarefas = signal(true);
  notificarProvas = signal(true);
  resumoSemanalPais = signal(true);
  efeitosSonoros = signal(true);
  animacoesGamificacao = signal(true);

  ngOnInit(): void {
    const c = this.config();
    this.tema.set(c.tema);
    this.tamanhoFonte.set(c.tamanhoFonte);
    this.altoContraste.set(c.altoContraste);
    this.notificarTarefas.set(c.notificarTarefas);
    this.notificarProvas.set(c.notificarProvas);
    this.resumoSemanalPais.set(c.resumoSemanalPais);
    this.efeitosSonoros.set(c.efeitosSonoros);
    this.animacoesGamificacao.set(c.animacoesGamificacao);
  }

  definirTema(novoTema: 'claro' | 'escuro' | 'sistema'): void {
    this.tema.set(novoTema);
  }

  definirFonte(novaFonte: 'padrao' | 'grande'): void {
    this.tamanhoFonte.set(novaFonte);
  }

  alternarContraste(): void {
    this.altoContraste.update((v) => !v);
  }

  alternarTarefas(): void {
    this.notificarTarefas.update((v) => !v);
  }

  alternarProvas(): void {
    this.notificarProvas.update((v) => !v);
  }

  alternarResumoPais(): void {
    this.resumoSemanalPais.update((v) => !v);
  }

  alternarSons(): void {
    this.efeitosSonoros.update((v) => !v);
  }

  alternarAnimacoes(): void {
    this.animacoesGamificacao.update((v) => !v);
  }

  aoSalvar(): void {
    const atualizado: ConfiguracoesUsuario = {
      ...this.config(),
      tema: this.tema(),
      tamanhoFonte: this.tamanhoFonte(),
      altoContraste: this.altoContraste(),
      notificarTarefas: this.notificarTarefas(),
      notificarProvas: this.notificarProvas(),
      resumoSemanalPais: this.resumoSemanalPais(),
      efeitosSonoros: this.efeitosSonoros(),
      animacoesGamificacao: this.animacoesGamificacao(),
    };
    this.salvarPreferencias.emit(atualizado);
  }
}

