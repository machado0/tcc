import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Materia } from '../../models/models';
import { ProgressoService } from '../progresso-service/progresso.service';

@Injectable({
  providedIn: 'root'
})
export class MateriaService {
  private readonly http = inject(HttpClient);
  private readonly progressoService = inject(ProgressoService);

  obterMaterias(): Observable<Materia[]> {
    return this.http
      .get<Materia[]>('assets/data/mock_materias.json')
      .pipe(map(materias => this.progressoService.aplicarNasMaterias(materias)));
  }

  obterMateriaPorId(id: number): Observable<Materia | undefined> {
    return this.obterMaterias().pipe(
      map((materias) => materias.find((m) => m.id === id))
    );
  }
}

