import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Materia, StatusItemEstudo } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class MateriaService {
  private readonly http = inject(HttpClient);

  obterMaterias(): Observable<Materia[]> {
    return this.http.get<BackendTrilha>('/api/v1/trilhas/matematica').pipe(
      map(trilha => this.mapearTrilha(trilha))
    );
  }

  obterMateriaPorId(id: number): Observable<Materia | undefined> {
    return this.obterMaterias().pipe(
      map((materias) => materias.find((m) => m.id === id))
    );
  }

  private mapearTrilha(trilha: BackendTrilha): Materia[] {
    const modulos = (trilha.modulos ?? []).map(modulo => ({
      id: modulo.id,
      ordem: modulo.ordem,
      titulo: modulo.nome,
      descricao: modulo.descricao ?? '',
      status: this.mapearStatus(modulo.status),
      progressoPorcentagem: modulo.progressoPercentual ?? 0,
      aulasConcluidas: (modulo.aulas ?? []).filter(aula => aula.status === 'CONCLUIDA').length,
      aulas: (modulo.aulas ?? []).map(aula => ({
        id: aula.id,
        ordem: aula.ordem,
        titulo: aula.nome,
        descricao: aula.descricao,
        duracaoMinutos: aula.duracaoMinutos ?? 0,
        xpRecompensa: (aula.questoes ?? []).reduce((total, questao) => total + (questao.xpRecompensa ?? 0), 0),
        status: this.mapearStatus(aula.status)
      }))
    }));
    const materia: Materia = {
      id: trilha.id,
      nome: trilha.nome,
      descricao: trilha.descricao ?? '',
      icone: 'calculate',
      cor: '#4f46e5',
      progressoPorcentagem: modulos.length
        ?         Math.round(modulos.reduce((total, modulo) => total + modulo.progressoPorcentagem, 0) / modulos.length)
        : 0,
      totalAulas: modulos.reduce((total, modulo) => total + modulo.aulas.length, 0),
      aulasConcluidas: modulos.reduce((total, modulo) => total + modulo.aulasConcluidas, 0),
      disponivel: true,
      modulos
    };
    return [materia];
  }

  private mapearStatus(status?: string): StatusItemEstudo {
    if (status === 'CONCLUIDA') return 'CONCLUIDA';
    if (status === 'BLOQUEADA') return 'BLOQUEADA';
    return 'DISPONIVEL';
  }
}

interface BackendTrilha {
  id: number;
  nome: string;
  descricao?: string;
  modulos: Array<{
    id: number;
    nome: string;
    descricao?: string;
    ordem: number;
    status?: string;
    progressoPercentual?: number;
    aulas: Array<{
      id: number;
      nome: string;
      descricao?: string;
      ordem: number;
      duracaoMinutos?: number;
      status?: string;
      progressoPercentual?: number;
      questoes?: Array<{ xpRecompensa?: number }>;
    }>;
  }>;
}
