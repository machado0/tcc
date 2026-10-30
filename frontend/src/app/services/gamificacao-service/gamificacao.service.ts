import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map, tap } from 'rxjs';
import {
  FeedbackDesafio, GamificacaoEstado, TurmaRanking, EstatisticasUsuario, MetricasDesempenho
} from '../../models/models';

interface DashboardResponse {
  level: number;
  currentXp: number;
  nextLevelXp: number;
  xpToday: number;
  streak: number;
  bestStreak: number;
  coins: number;
  lives: number;
  posicaoRanking: number;
  questoesRespondidas: number;
  questoesCorretas: number;
  aulasConcluidas: number;
  tempoTotalEstudoMinutos: number;
  missoesDiarias: BackendMissao[];
  progressoSemanal: BackendProgressoSemanal;
}

@Injectable({ providedIn: 'root' })
export class GamificacaoService {
  private readonly http = inject(HttpClient);
  private readonly estadoInterno = signal<GamificacaoEstado | null>(null);
  readonly estado = this.estadoInterno.asReadonly();

  carregar(): Observable<GamificacaoEstado> {
    return forkJoin({
      dashboard: this.http.get<DashboardResponse>('/api/v1/alunos/dashboard'),
      ranking: this.http.get<BackendRanking>('/api/v1/ranking'),
      conquistas: this.http.get<BackendConquista[]>('/api/v1/conquistas')
    }).pipe(
      map(({ dashboard, ranking, conquistas }) => this.mapearEstado(dashboard, this.mapearRanking(ranking), conquistas)),
      tap(estado => this.estadoInterno.set(estado))
    );
  }

  submeterDesafio(questaoId: number, alternativaEscolhidaId: number): Observable<FeedbackDesafio> {
    return this.http.post<FeedbackDesafio>('/api/v1/desafios/submeter', {
      questaoId,
      alternativaEscolhidaId
    });
  }

  private mapearEstado(
    dashboard: DashboardResponse,
    ranking: TurmaRanking,
    conquistasApi: BackendConquista[]
  ): GamificacaoEstado {
    const estatisticas: EstatisticasUsuario = {
      nivel: dashboard.level,
      xpAtual: dashboard.currentXp,
      xpProximoNivel: dashboard.nextLevelXp,
      xpHoje: dashboard.xpToday,
      sequenciaDias: dashboard.streak,
      melhorSequenciaDias: dashboard.bestStreak,
      moedas: dashboard.coins,
      vidas: dashboard.lives
    };
    const metricas: MetricasDesempenho = {
      questoesRespondidas: dashboard.questoesRespondidas,
      questoesCorretas: dashboard.questoesCorretas,
      aulasConcluidas: dashboard.aulasConcluidas,
      tempoTotalEstudoMinutos: dashboard.tempoTotalEstudoMinutos,
      taxaAcerto: dashboard.questoesRespondidas
        ? Math.round(dashboard.questoesCorretas / dashboard.questoesRespondidas * 100)
        : 0
    };
    return {
      usuarioId: ranking.alunos.find(aluno => aluno.ehUsuarioLogado)?.usuarioId ?? 0,
      atualizadoEm: new Date().toISOString(),
      estatisticas,
      metricas,
      missoes: dashboard.missoesDiarias.map(missao => ({
        id: missao.id,
        titulo: missao.titulo,
        xp: missao.recompensaXp,
        concluida: missao.concluida,
        tipo: missao.tipo,
        meta: missao.meta,
        progressoAtual: missao.progressoAtual,
        dataReferencia: missao.dataReferencia,
        dataConclusao: missao.dataConclusao
      })),
      conquistas: conquistasApi.map(conquista => ({
        id: conquista.id,
        titulo: conquista.titulo,
        descricao: conquista.descricao,
        icone: conquista.icone ?? 'emoji_events',
        desbloqueada: conquista.desbloqueada,
        progressoAtual: conquista.progressoAtual ?? (conquista.desbloqueada ? conquista.meta : 0),
        meta: conquista.meta,
        recompensaXp: conquista.recompensaXp,
        categoria: 'GERAL' as const
      })),
      progressoSemanal: dashboard.progressoSemanal,
      conclusoes: [],
      rankingUsuario: ranking.alunos.find(aluno => aluno.ehUsuarioLogado) ?? null
    };
  }

  private mapearRanking(ranking: BackendRanking): TurmaRanking {
    return {
      id: ranking.turmaId,
      nomeTurma: ranking.turmaNome,
      periodo: '',
      terminaEmDias: 0,
      totalAlunos: ranking.alunos.length,
      posicaoUsuarioLogado: ranking.meuPerfil?.posicao ?? 0,
      alunos: ranking.alunos.map(aluno => ({
        posicao: aluno.posicao,
        usuarioId: aluno.usuarioId,
        nome: aluno.nome,
        username: aluno.nome,
        nivel: aluno.nivel,
        xpSemana: aluno.xp,
        sequenciaDias: aluno.streak,
        tendencia: 'MANTEVE' as const,
        ehUsuarioLogado: aluno.usuarioId === ranking.meuPerfil?.usuarioId
      }))
    };
  }
}

interface BackendConquista {
  id: number;
  titulo: string;
  descricao: string;
  icone?: string;
  meta: number;
  recompensaXp: number;
  progressoAtual?: number;
  desbloqueada: boolean;
}
interface BackendRanking {
  turmaId: number;
  turmaNome: string;
  alunos: Array<{ posicao: number; usuarioId: number; nome: string; xp: number; nivel: number; streak: number }>;
  meuPerfil?: { posicao: number; usuarioId: number };
}
interface BackendMissao {
  id: number;
  titulo: string;
  tipo: 'RESPONDER_QUESTOES' | 'ACERTAR_QUESTOES' | 'CONCLUIR_AULAS' | 'OBTER_XP';
  meta: number;
  progressoAtual: number;
  recompensaXp: number;
  concluida: boolean;
  dataReferencia: string;
  dataConclusao?: string;
}
interface BackendProgressoSemanal {
  dias: Array<{ data: string; dia: string; xp: number; aulasConcluidas: number; questoesRespondidas: number }>;
  totalXp: number;
  diasEstudados: number;
}
