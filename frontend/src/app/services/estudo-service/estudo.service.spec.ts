import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { EstudoService } from './estudo.service';

describe('EstudoService', () => {
  let service: EstudoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EstudoService, provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(EstudoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('inicia uma sessão vinculada à aula', () => {
    service.iniciar(10).subscribe(sessao => expect(sessao.id).toBe(4));
    const request = http.expectOne('/api/v1/sessoes-estudo');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({ aulaId: 10 });
    request.flush({ id: 4, aulaId: 10, inicio: '2026-11-07T17:00:00', concluida: false });
  });

  it('finaliza a sessão pela API', () => {
    service.finalizar(4).subscribe(sessao => expect(sessao.concluida).toBe(true));
    const request = http.expectOne('/api/v1/sessoes-estudo/4/finalizar');
    expect(request.request.method).toBe('POST');
    request.flush({
      id: 4,
      aulaId: 10,
      inicio: '2026-11-07T17:00:00',
      fim: '2026-11-07T17:12:00',
      duracaoMinutos: 12,
      concluida: true
    });
  });
});
