import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subject, of } from 'rxjs';
import { Aula } from './aula';
import { AulaService } from '../services/aula-service/aula.service';
import { EstudoService } from '../services/estudo-service/estudo.service';
import { DetalheAula, SessaoEstudo } from '../models/models';

const detalhe: DetalheAula = {
  id: 10,
  moduloId: 1,
  materiaNome: 'Matemática',
  moduloNome: 'Números',
  titulo: 'Aula de teste',
  descricao: 'Descrição',
  duracaoMinutos: 20,
  xpTotal: 10,
  conteudoTeorico: {
    resumo: 'Resumo',
    pontosChave: [],
    dicaProfessor: 'Dica',
    exemploPratico: 'Exemplo'
  },
  questoes: []
};

describe('Aula', () => {
  let fixture: ComponentFixture<Aula>;
  let component: Aula;
  let inicio$: Subject<SessaoEstudo>;
  let finalizar: ReturnType<typeof vi.fn>;

  beforeEach(async () => {
    inicio$ = new Subject<SessaoEstudo>();
    finalizar = vi.fn(() => of({
      id: 9,
      aulaId: 10,
      inicio: '2026-11-07T17:00:00',
      fim: '2026-11-07T17:01:00',
      duracaoMinutos: 1,
      concluida: true
    }));

    await TestBed.configureTestingModule({
      imports: [Aula],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '10' } } }
        },
        {
          provide: AulaService,
          useValue: { obterDetalheAula: () => of(detalhe) }
        },
        {
          provide: EstudoService,
          useValue: {
            iniciar: (): Observable<SessaoEstudo> => inicio$,
            finalizar
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Aula);
    component = fixture.componentInstance;
  });

  it('finaliza a sessão quando o componente é destruído antes da resposta de início', () => {
    component.ngOnInit();
    component.ngOnDestroy();

    inicio$.next({
      id: 9,
      aulaId: 10,
      inicio: '2026-11-07T17:00:00',
      concluida: false
    });

    expect(finalizar).toHaveBeenCalledTimes(1);
    expect(finalizar).toHaveBeenCalledWith(9);
  });
});
