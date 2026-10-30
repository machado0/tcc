import { Component, inject, signal } from '@angular/core';
import { Saudacao } from "./home-saudacao/home-saudacao";
import { CardProgresso } from "./home-progresso/home-progresso";
import { CardContinuar } from "./home-continuar/home-continuar";
import { CardMissoesDiarias } from "./home-missoes-diarias/home-missoes-diarias";
import { CardProgressoSemanal } from "./home-progresso-semanal/home-progresso-semanal";
import { HomeService } from '../services/home-service/home.service';
import { DadosHome } from '../models/models';

@Component({
  selector: 'app-home',
  imports: [Saudacao, CardProgresso, CardContinuar, CardMissoesDiarias, CardProgressoSemanal],
  templateUrl: './home.html',
})
export class Home {
  private readonly servicoHome =
    inject(HomeService);

  dados = signal<DadosHome | null>(null);
  carregando = signal(true);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregarDadosHome();
  }

  carregarDadosHome(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.servicoHome
      .obterDadosDashboard()
      .subscribe({
        next: (dados) => {
          this.dados.set(dados);
          this.carregando.set(false);
        },
        error: (erro) => {
          console.error('Erro ao carregar os dados da dashboard:', erro);
          this.erro.set('Não foi possível carregar a home.');
          this.carregando.set(false);
        }
      });
  }
}