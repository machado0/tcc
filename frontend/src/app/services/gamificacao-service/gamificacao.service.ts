import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map, of, tap } from 'rxjs';
import {
  AlunoRanking,
  Conquista,
  ConclusaoAula,
  DadosHome,
  EstatisticasUsuario,
  GamificacaoEstado,
  MetricasDesempenho,
  Missao,
  MissaoEstado,
  ResultadoGamificacao,
  TipoMissao,
  TurmaRanking
} from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class GamificacaoService {
  private readonly http = inject(HttpClient);
  private readonly chave = 'tcc_gamificacao_estado';
  private readonly estadoInterno = signal<GamificacaoEstado | null>(null);

  readonly estado = this.estadoInterno.asReadonly();

  carregar(): Observable<GamificacaoEstado> {
    const salvo = this.obterEstadoSalvo();

    if (salvo) {
      this.estadoInterno.set(salvo);
      return of(salvo);
    }

    return forkJoin({
      home: this.http.get<DadosHome>('assets/data/mock_usuario.json'),
      ranking: this.http.get<TurmaRanking>('assets/data/mock_ranking.json')
    }).pipe(
      map(({ home, ranking }) => this.criarEstadoInicial(home, ranking)),
      tap(estado => this.persistir(estado))
    );
  }

  registrarConclusao(
    conclusao: Omit<ConclusaoAula, 'dataConclusao'>
  ): Observable<ResultadoGamificacao> {
    return this.carregar().pipe(
      map(estado => this.aplicarConclusao(estado, {
        ...conclusao,
        dataConclusao: new Date().toISOString()
      })),
      tap(resultado => this.persistir(resultado.estado))
    );
  }

  private criarEstadoInicial(
    home: DadosHome,
    ranking: TurmaRanking
  ): GamificacaoEstado {
    const usuarioRanking = ranking.alunos.find(aluno => aluno.ehUsuarioLogado) ?? null;

    return {
      usuarioId: home.usuario.id,
      atualizadoEm: new Date().toISOString(),
      estatisticas: home.estatisticas,
      metricas: home.metricas ?? {
        questoesRespondidas: 0,
        questoesCorretas: 0,
        aulasConcluidas: 0,
        tempoTotalEstudoMinutos: 0,
        taxaAcerto: 0
      },
      missoes: home.missoesDiarias.map(missao => this.criarMissaoEstado(missao)),
      conquistas: home.conquistas ?? [],
      progressoSemanal: home.progressoSemanal,
      conclusoes: [],
      rankingUsuario: usuarioRanking
    };
  }

  private criarMissaoEstado(missao: Missao): MissaoEstado {
    const tipo = this.obterTipoMissao(missao);
    const metaInformada = (missao as Partial<MissaoEstado>).meta;
    const numeroNoTitulo = missao.titulo.match(/\d+/)?.[0];
    const meta = metaInformada && metaInformada > 0
      ? metaInformada
      : numeroNoTitulo
        ? Number(numeroNoTitulo)
        : 1;

    return {
      ...missao,
      tipo,
      meta,
      progressoAtual: missao.concluida ? meta : 0,
      dataReferencia: this.obterDataAtual()
    };
  }

  private obterTipoMissao(missao: Missao): TipoMissao {
    if (missao.titulo.toLowerCase().includes('quest')) {
      return 'RESPONDER_QUESTOES';
    }

    if (missao.titulo.toLowerCase().includes('xp')) {
      return 'OBTER_XP';
    }

    return 'CONCLUIR_AULAS';
  }

  private aplicarConclusao(
    estado: GamificacaoEstado,
    conclusao: ConclusaoAula
  ): ResultadoGamificacao {
    const jaRegistrada = estado.conclusoes.some(
      item => item.aulaId === conclusao.aulaId
    );

    if (jaRegistrada) {
      return {
        estado,
        conclusao,
        missoesAtualizadas: estado.missoes,
        conquistasDesbloqueadas: []
      };
    }

    const metricas = this.atualizarMetricas(estado.metricas, conclusao);
    const missoesAtualizadas = this.atualizarMissoes(
      estado.missoes,
      conclusao
    );
    const recompensasMissoes = missoesAtualizadas
      .filter(missao => missao.concluida && !estado.missoes.some(
        anterior => anterior.id === missao.id && anterior.concluida
      ))
      .reduce((total, missao) => total + missao.xp, 0);
    const conquistas = this.atualizarConquistas(
      estado.conquistas,
      metricas,
      estado.estatisticas.sequenciaDias,
      estado.estatisticas
    );
    const conquistasDesbloqueadas = conquistas.filter(
      conquista => conquista.desbloqueada &&
        !estado.conquistas.some(item => item.id === conquista.id && item.desbloqueada)
    );
    const recompensasConquistas = conquistasDesbloqueadas.reduce(
      (total, conquista) => total + conquista.recompensaXp,
      0
    );
    const xpGanho = conclusao.xpTotal + recompensasMissoes + recompensasConquistas;
    const estatisticas = this.atualizarEstatisticas(estado.estatisticas, xpGanho);
    const novoRanking = this.atualizarRanking(estado.rankingUsuario, xpGanho, estatisticas);
    const novoEstado: GamificacaoEstado = {
      ...estado,
      atualizadoEm: new Date().toISOString(),
      estatisticas,
      metricas,
      missoes: missoesAtualizadas,
      conquistas,
      conclusoes: [...estado.conclusoes, conclusao],
      rankingUsuario: novoRanking,
      progressoSemanal: {
        ...estado.progressoSemanal,
        totalXp: estado.progressoSemanal.totalXp + xpGanho
      }
    };

    return {
      estado: novoEstado,
      conclusao,
      missoesAtualizadas,
      conquistasDesbloqueadas
    };
  }

  private atualizarMetricas(
    metricas: MetricasDesempenho,
    conclusao: ConclusaoAula
  ): MetricasDesempenho {
    const questoesRespondidas = metricas.questoesRespondidas + conclusao.total;
    const questoesCorretas = metricas.questoesCorretas + conclusao.acertos;

    return {
      ...metricas,
      questoesRespondidas,
      questoesCorretas,
      aulasConcluidas: metricas.aulasConcluidas + 1,
      taxaAcerto: questoesRespondidas === 0
        ? 0
        : Math.round((questoesCorretas / questoesRespondidas) * 100)
    };
  }

  private atualizarMissoes(
    missoes: MissaoEstado[],
    conclusao: ConclusaoAula
  ): MissaoEstado[] {
    return missoes.map(missao => {
      if (missao.concluida) {
        return missao;
      }

      const incremento =
        missao.tipo === 'RESPONDER_QUESTOES'
          ? conclusao.total
          : missao.tipo === 'ACERTAR_QUESTOES'
            ? conclusao.acertos
            : missao.tipo === 'OBTER_XP'
              ? conclusao.xpTotal
              : 1;
      const progressoAtual = Math.min(
        missao.meta,
        missao.progressoAtual + incremento
      );
      const concluida = progressoAtual >= missao.meta;

      return {
        ...missao,
        progressoAtual,
        concluida,
        dataConclusao: concluida ? new Date().toISOString() : undefined
      };
    });
  }

  private atualizarConquistas(
    conquistas: Conquista[],
    metricas: MetricasDesempenho,
    sequenciaDias: number,
    estatisticas: EstatisticasUsuario
  ): Conquista[] {
    return conquistas.map(conquista => {
      const progressoAtual = this.obterProgressoConquista(
        conquista,
        metricas,
        sequenciaDias,
        estatisticas
      );

      return {
        ...conquista,
        progressoAtual,
        desbloqueada: progressoAtual >= conquista.meta
      };
    });
  }

  private obterProgressoConquista(
    conquista: Conquista,
    metricas: MetricasDesempenho,
    sequenciaDias: number,
    estatisticas: EstatisticasUsuario
  ): number {
    switch (conquista.id) {
      case 1:
        return metricas.aulasConcluidas;
      case 2:
      case 4:
        return sequenciaDias;
      case 3:
        return metricas.questoesCorretas;
      case 5:
        return Math.floor(metricas.tempoTotalEstudoMinutos / 60);
      case 6:
        return estatisticas.nivel;
      default:
        return conquista.progressoAtual;
    }
  }

  private atualizarEstatisticas(
    estatisticas: EstatisticasUsuario,
    xpGanho: number
  ): EstatisticasUsuario {
    let nivel = estatisticas.nivel;
    let xpAtual = estatisticas.xpAtual + xpGanho;
    let xpProximoNivel = estatisticas.xpProximoNivel;

    while (xpAtual >= xpProximoNivel) {
      nivel += 1;
      xpProximoNivel = nivel * 250;
    }

    return {
      ...estatisticas,
      nivel,
      xpAtual,
      xpProximoNivel,
      xpHoje: estatisticas.xpHoje + xpGanho
    };
  }

  private atualizarRanking(
    ranking: AlunoRanking | null,
    xpGanho: number,
    estatisticas: EstatisticasUsuario
  ): AlunoRanking | null {
    if (!ranking) {
      return null;
    }

    return {
      ...ranking,
      nivel: estatisticas.nivel,
      xpSemana: ranking.xpSemana + xpGanho
    };
  }

  private obterDataAtual(): string {
    return new Date().toISOString().slice(0, 10);
  }

  private obterEstadoSalvo(): GamificacaoEstado | null {
    const salvo = localStorage.getItem(this.chave);

    if (!salvo) {
      return null;
    }

    try {
      return JSON.parse(salvo) as GamificacaoEstado;
    } catch {
      localStorage.removeItem(this.chave);
      return null;
    }
  }

  private persistir(estado: GamificacaoEstado): void {
    this.estadoInterno.set(estado);
    localStorage.setItem(this.chave, JSON.stringify(estado));
  }
}
