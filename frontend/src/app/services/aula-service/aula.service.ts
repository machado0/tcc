import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DetalheAula } from '../../models/models';

@Injectable({ providedIn: 'root' })
export class AulaService {
  private readonly http = inject(HttpClient);

  obterDetalheAula(aulaId: number): Observable<DetalheAula> {
    return this.http.get<BackendTrilha>('/api/v1/trilhas/matematica').pipe(
      map(trilha => {
        for (const modulo of trilha.modulos ?? []) {
          const aula = (modulo.aulas ?? []).find(item => item.id === aulaId);
          if (aula) {
            return {
              id: aula.id,
              moduloId: modulo.id,
              materiaNome: trilha.nome,
              moduloNome: modulo.nome,
              titulo: aula.nome,
              descricao: aula.descricao ?? '',
              duracaoMinutos: aula.duracaoMinutos ?? 0,
              xpTotal: (aula.questoes ?? []).reduce((total, questao) => total + (questao.xpRecompensa ?? 0), 0),
              conteudoTeorico: { resumo: '', pontosChave: [], dicaProfessor: '', exemploPratico: '' },
              questoes: (aula.questoes ?? []).map(questao => ({
                id: questao.id,
                ordem: questao.ordem ?? 0,
                enunciado: questao.cabecalho ?? '',
                opcoes: (questao.alternativas ?? []).map((opcao, index) => ({
                  id: opcao.id,
                  letra: ['A', 'B', 'C', 'D'][index] as 'A' | 'B' | 'C' | 'D',
                  texto: opcao.texto
                })),
                xpRecompensa: questao.xpRecompensa ?? 0
              }))
            };
          }
        }
        return null;
      }),
      map(aula => aula ?? (() => { throw new Error(`Aula ${aulaId} não encontrada.`); })())
    );
  }
}

interface BackendTrilha {
  nome: string;
  modulos: Array<{
    id: number;
    nome: string;
    aulas: Array<{
      id: number;
      nome: string;
      descricao?: string;
      duracaoMinutos?: number;
      questoes?: Array<{
        id: number;
        cabecalho?: string;
        ordem?: number;
        xpRecompensa?: number;
        alternativas?: Array<{ id: number; texto: string }>;
      }>;
    }>;
  }>;
}
