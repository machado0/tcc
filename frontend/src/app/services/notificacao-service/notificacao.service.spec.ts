import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { NotificacaoService } from './notificacao.service';

const notificacoes = [
  {
    id: 1,
    titulo: 'Missão',
    mensagem: 'Complete uma atividade.',
    tipo: 'MISSAO' as const,
    lida: false,
    criadaEm: '2026-10-28T08:00:00'
  }
];

describe('NotificacaoService', () => {
  let service: NotificacaoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        NotificacaoService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(NotificacaoService);
    http = TestBed.inject(HttpTestingController);
  });

  it('carrega notificacoes pela API', () => {
    service.obterNotificacoes().subscribe(resultado => {
      expect(resultado).toEqual(notificacoes);
    });
    http.expectOne('/api/v1/notificacoes').flush(notificacoes);
  });

  it('marca uma notificacao como lida', () => {
    service.marcarComoLida(1).subscribe(resultado => {
      expect(resultado[0].lida).toBe(true);
      expect(service).toBeTruthy();
    });
    http.expectOne(request => request.url === '/api/v1/notificacoes/1/lida' && request.method === 'PATCH')
      .flush([{ ...notificacoes[0], lida: true }]);
  });
});
