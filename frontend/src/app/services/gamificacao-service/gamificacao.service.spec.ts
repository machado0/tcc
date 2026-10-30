import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { GamificacaoService } from './gamificacao.service';

describe('GamificacaoService', () => {
  let service: GamificacaoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GamificacaoService, provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(GamificacaoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('carrega o estado pelos endpoints reais', () => {
    service.carregar().subscribe(estado => {
      expect(estado.estatisticas.nivel).toBe(2);
      expect(estado.conquistas.length).toBe(1);
      expect(estado.conquistas[0].recompensaXp).toBe(50);
    });
    http.expectOne('/api/v1/alunos/dashboard').flush({
      level: 2, currentXp: 100, nextLevelXp: 250, xpToday: 20,
      streak: 3, bestStreak: 5, coins: 10, lives: 3, posicaoRanking: 1,
      questoesRespondidas: 5, questoesCorretas: 4, aulasConcluidas: 1,
      tempoTotalEstudoMinutos: 12,
      missoesDiarias: [],
      progressoSemanal: { dias: [], totalXp: 20, diasEstudados: 1 }
    });
    http.expectOne('/api/v1/ranking').flush({
      turmaId: 1, turmaNome: '3A',
      alunos: [{ posicao: 1, usuarioId: 1, nome: 'Aluno', xp: 100, nivel: 2, streak: 3 }],
      meuPerfil: { posicao: 1, usuarioId: 1 }
    });
    http.expectOne('/api/v1/conquistas').flush([{
      id: 1, titulo: 'Primeira', descricao: 'Teste', icone: 'school',
      meta: 1, recompensaXp: 50, progressoAtual: 1, desbloqueada: true
    }]);
  });

  it('submete respostas ao backend', () => {
    service.submeterDesafio(10, 20).subscribe(resposta => expect(resposta.acertou).toBe(true));
    http.expectOne('/api/v1/desafios/submeter').flush({
      acertou: true, alternativaCorretaId: 20, xpGanho: 10,
      moedasGanha: 1, vidasRestantes: 3, novoXpTotal: 110, novoNivel: 2,
      conquistasDesbloqueadas: []
    });
  });
});
