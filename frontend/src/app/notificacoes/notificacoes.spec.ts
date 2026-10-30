import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Notificacoes } from './notificacoes';
import { NotificacaoService } from '../services/notificacao-service/notificacao.service';

const notificacao = {
  id: 1,
  titulo: 'Aviso',
  mensagem: 'Mensagem de teste',
  tipo: 'SISTEMA' as const,
  lida: false,
  criadaEm: '2026-10-28T08:00:00'
};

describe('Notificacoes', () => {
  let component: Notificacoes;
  let fixture: ComponentFixture<Notificacoes>;
  const service = {
    obterNotificacoes: () => of([notificacao]),
    marcarComoLida: () => of([{ ...notificacao, lida: true }]),
    marcarTodasComoLidas: () => of([{ ...notificacao, lida: true }])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Notificacoes],
      providers: [
        provideRouter([]),
        { provide: NotificacaoService, useValue: service }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Notificacoes);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('carrega notificacoes e identifica nao lidas', () => {
    expect(component.notificacoes().length).toBe(1);
    expect(component.temNaoLidas()).toBe(true);
  });

  it('marca uma notificacao como lida ao abrir', () => {
    component.abrir(notificacao);
    expect(component.notificacoes()[0].lida).toBe(true);
  });
});
