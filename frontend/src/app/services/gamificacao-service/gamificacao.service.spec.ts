import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';
import { GamificacaoService } from './gamificacao.service';
import { DadosHome, TurmaRanking } from '../../models/models';

const home = {
  usuario: { id: 1, nome: 'Aluno' },
  estatisticas: {
    nivel: 1,
    xpAtual: 0,
    xpProximoNivel: 250,
    xpHoje: 0,
    sequenciaDias: 1,
    melhorSequenciaDias: 1,
    moedas: 0,
    vidas: 3
  },
  metricas: {
    questoesRespondidas: 0,
    questoesCorretas: 0,
    aulasConcluidas: 0,
    tempoTotalEstudoMinutos: 0,
    taxaAcerto: 0
  },
  continuarEstudando: null,
  missoesDiarias: [
    {
      id: 1,
      titulo: 'Concluir uma aula',
      xp: 20,
      concluida: false,
      tipo: 'CONCLUIR_AULAS' as const,
      meta: 1,
      progressoAtual: 0,
      dataReferencia: '2026-10-28'
    }
  ],
  conquistas: [
    {
      id: 1,
      titulo: 'Primeira aula',
      descricao: 'Conclua uma aula',
      icone: 'school',
      desbloqueada: false,
      progressoAtual: 0,
      meta: 1,
      recompensaXp: 10,
      categoria: 'GERAL' as const
    }
  ],
  progressoSemanal: {
    dias: [],
    totalXp: 0,
    diasEstudados: 0
  }
} as DadosHome;

const ranking = {
  id: 1,
  nomeTurma: '3A',
  periodo: 'Semana atual',
  terminaEmDias: 3,
  totalAlunos: 1,
  posicaoUsuarioLogado: 1,
  alunos: [
    {
      posicao: 1,
      usuarioId: 1,
      nome: 'Aluno',
      username: '@aluno',
      nivel: 1,
      xpSemana: 0,
      sequenciaDias: 1,
      tendencia: 'MANTEVE' as const,
      ehUsuarioLogado: true
    }
  ]
} as TurmaRanking;

describe('GamificacaoService', () => {
  let service: GamificacaoService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        GamificacaoService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(GamificacaoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('inicializa o snapshot com os mocks', () => {
    service.carregar().subscribe(estado => {
      expect(estado.usuarioId).toBe(1);
      expect(estado.missoes.length).toBe(1);
    });

    http.expectOne('assets/data/mock_usuario.json').flush(home);
    http.expectOne('assets/data/mock_ranking.json').flush(ranking);
  });

  it('atualiza missão, conquista, XP e não duplica a conclusão', () => {
    service.carregar().subscribe();
    http.expectOne('assets/data/mock_usuario.json').flush(home);
    http.expectOne('assets/data/mock_ranking.json').flush(ranking);

    const conclusao = {
      aulaId: 1008,
      acertos: 1,
      total: 1,
      xpTotal: 5
    };

    service.registrarConclusao(conclusao).subscribe(resultado => {
      expect(resultado.missoesAtualizadas[0].concluida).toBe(true);
      expect(resultado.conquistasDesbloqueadas.length).toBe(1);
      expect(resultado.estado.estatisticas.xpAtual).toBe(35);
    });

    service.registrarConclusao(conclusao).subscribe(resultado => {
      expect(resultado.estado.estatisticas.xpAtual).toBe(35);
      expect(resultado.conquistasDesbloqueadas.length).toBe(0);
    });
  });
});
