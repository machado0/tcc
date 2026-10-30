import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
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

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        NotificacaoService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(NotificacaoService);
  });

  it('persiste e carrega notificacoes locais', () => {
    localStorage.setItem('tcc_notificacoes', JSON.stringify(notificacoes));

    service.obterNotificacoes().subscribe(resultado => {
      expect(resultado).toEqual(notificacoes);
    });
  });

  it('marca uma notificacao como lida', () => {
    localStorage.setItem('tcc_notificacoes', JSON.stringify(notificacoes));

    service.marcarComoLida(1).subscribe(resultado => {
      expect(resultado[0].lida).toBe(true);
      expect(service).toBeTruthy();
    });
  });
});
