import { Component, inject, signal, OnInit } from '@angular/core';
import { MateriaService } from '../services/materia-service/materia.service';
import { Materia } from '../models/models';
import { SeletorMaterias } from './seletor-materias/seletor-materias';
import { CardModulo } from './card-modulo/card-modulo';
import { ResumoMateria } from './resumo-materia/resumo-materia';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-materias',
  imports: [
    SeletorMaterias,
    CardModulo,
    ResumoMateria,
    MatIconModule
  ],
  templateUrl: './materias.html',
  styleUrl: './materias.css',
})
export class Materias implements OnInit {
  private readonly materiaService = inject(MateriaService);

  materias = signal<Materia[]>([]);
  materiaAtiva = signal<Materia | null>(null);
  carregando = signal(true);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregarMaterias();
  }

  carregarMaterias(): void {
    this.carregando.set(true);
    this.erro.set(null);

    this.materiaService.obterMaterias().subscribe({
      next: (lista) => {
        this.materias.set(lista);
        const ativa = lista.find((m) => m.disponivel) ?? lista[0] ?? null;
        this.materiaAtiva.set(ativa);
        this.carregando.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar matérias:', err);
        this.erro.set('Não foi possível carregar as matérias do mapa.');
        this.carregando.set(false);
      }
    });
  }

  aoTrocarMateria(materia: Materia): void {
    this.materiaAtiva.set(materia);
  }
}

