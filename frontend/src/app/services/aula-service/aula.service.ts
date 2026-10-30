import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { DetalheAula } from '../../models/models';

@Injectable({
  providedIn: 'root'
})
export class AulaService {
  private readonly http = inject(HttpClient);

  obterDetalheAula(aulaId: number): Observable<DetalheAula> {
    return this.http
      .get<DetalheAula>('assets/data/mock_aula_detalhe.json')
      .pipe(
        map(aula => {
          if (aula.id !== aulaId) {
            throw new Error(`Aula ${aulaId} não encontrada.`);
          }

          return aula;
        })
      );
  }
}

