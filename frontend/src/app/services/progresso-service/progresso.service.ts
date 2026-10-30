import { Injectable } from '@angular/core';
import {
  DadosHome,
  Materia,
  PerfilUsuario,
  StatusItemEstudo
} from '../../models/models';

export interface ConclusaoAula {
  aulaId: number;
  acertos: number;
  total: number;
  xpTotal: number;
  dataConclusao: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProgressoService {
  private readonly chave = 'tcc_conclusoes_aula';

  registrarConclusao(
    aulaId: number,
    resultado: Omit<ConclusaoAula, 'aulaId' | 'dataConclusao'>
  ): ConclusaoAula {
    const conclusao: ConclusaoAula = {
      aulaId,
      ...resultado,
      dataConclusao: new Date().toISOString()
    };

    const conclusoes = this.obterConclusoes();
    const indiceExistente = conclusoes.findIndex(item => item.aulaId === aulaId);

    if (indiceExistente >= 0) {
      conclusoes[indiceExistente] = conclusao;
    } else {
      conclusoes.push(conclusao);
    }

    localStorage.setItem(this.chave, JSON.stringify(conclusoes));
    return conclusao;
  }

  obterConclusoes(): ConclusaoAula[] {
    const dados = localStorage.getItem(this.chave);

    if (!dados) {
      return [];
    }

    try {
      return JSON.parse(dados) as ConclusaoAula[];
    } catch {
      return [];
    }
  }

  obterConclusao(aulaId: number): ConclusaoAula | undefined {
    return this.obterConclusoes().find(item => item.aulaId === aulaId);
  }

  aplicarNaHome(dados: DadosHome): DadosHome {
    const conclusoes = this.obterConclusoes();
    const xpGanho = this.obterXp(conclusoes);

    if (conclusoes.length === 0) {
      return dados;
    }

    const continuar = dados.continuarEstudando;
    const conclusaoAtual = continuar
      ? conclusoes.find(item => item.aulaId === continuar.aulaId)
      : undefined;

    return {
      ...dados,
      estatisticas: {
        ...dados.estatisticas,
        xpAtual: dados.estatisticas.xpAtual + xpGanho,
        xpHoje: dados.estatisticas.xpHoje + xpGanho
      },
      continuarEstudando: conclusaoAtual && continuar
        ? {
            ...continuar,
            progresso: 100,
            ultimaDataEstudo: conclusaoAtual.dataConclusao
          }
        : continuar,
      progressoSemanal: {
        ...dados.progressoSemanal,
        totalXp: dados.progressoSemanal.totalXp + xpGanho,
        diasEstudados: dados.progressoSemanal.diasEstudados + 1
      }
    };
  }

  aplicarNoPerfil(perfil: PerfilUsuario): PerfilUsuario {
    const conclusoes = this.obterConclusoes();
    const xpGanho = this.obterXp(conclusoes);
    const questoesRespondidas = conclusoes.reduce(
      (total, conclusao) => total + conclusao.total,
      0
    );
    const questoesCorretas = conclusoes.reduce(
      (total, conclusao) => total + conclusao.acertos,
      0
    );

    if (conclusoes.length === 0) {
      return perfil;
    }

    const metricas = {
      ...perfil.metricas,
      questoesRespondidas: perfil.metricas.questoesRespondidas + questoesRespondidas,
      questoesCorretas: perfil.metricas.questoesCorretas + questoesCorretas,
      aulasConcluidas: perfil.metricas.aulasConcluidas + conclusoes.length,
      taxaAcerto: this.calcularTaxaAcerto(
        perfil.metricas.questoesRespondidas + questoesRespondidas,
        perfil.metricas.questoesCorretas + questoesCorretas
      )
    };

    return {
      ...perfil,
      estatisticas: {
        ...perfil.estatisticas,
        xpAtual: perfil.estatisticas.xpAtual + xpGanho,
        xpHoje: perfil.estatisticas.xpHoje + xpGanho
      },
      metricas
    };
  }

  aplicarNasMaterias(materias: Materia[]): Materia[] {
    const idsConcluidos = new Set(
      this.obterConclusoes().map(conclusao => conclusao.aulaId)
    );

    if (idsConcluidos.size === 0) {
      return materias;
    }

    return materias.map(materia => {
      const modulos = materia.modulos.map(modulo => {
        const aulas = modulo.aulas.map(aula =>
          idsConcluidos.has(aula.id)
            ? { ...aula, status: 'CONCLUIDA' as StatusItemEstudo }
            : aula
        );
        const aulasConcluidas = aulas.filter(aula => aula.status === 'CONCLUIDA').length;

        return {
          ...modulo,
          aulas,
          progressoPorcentagem: aulas.length
            ? Math.round((aulasConcluidas / aulas.length) * 100)
            : modulo.progressoPorcentagem,
          status: aulas.length > 0 && aulasConcluidas === aulas.length
            ? 'CONCLUIDA' as StatusItemEstudo
            : modulo.status
        };
      });
      const aulas = modulos.flatMap(modulo => modulo.aulas);
      const aulasConcluidas = aulas.filter(aula => aula.status === 'CONCLUIDA').length;

      return {
        ...materia,
        modulos,
        aulasConcluidas,
        progressoPorcentagem: aulas.length
          ? Math.round((aulasConcluidas / aulas.length) * 100)
          : materia.progressoPorcentagem
      };
    });
  }

  private obterXp(conclusoes: ConclusaoAula[]): number {
    return conclusoes.reduce((total, conclusao) => total + conclusao.xpTotal, 0);
  }

  private calcularTaxaAcerto(respondidas: number, corretas: number): number {
    return respondidas === 0 ? 0 : Math.round((corretas / respondidas) * 100);
  }
}
