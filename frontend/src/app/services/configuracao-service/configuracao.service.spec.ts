import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { ConfiguracaoService } from './configuracao.service';
import { ConfiguracoesUsuario } from '../../models/models';

const configuracoes: ConfiguracoesUsuario = {
  matricula: '123',
  escola: 'Escola Teste',
  turma: '3A',
  emailResponsaveis: 'responsavel@teste.com',
  tema: 'escuro',
  tamanhoFonte: 'grande',
  altoContraste: true,
  efeitosSonoros: false,
  animacoesGamificacao: false,
  notificarTarefas: true,
  notificarProvas: true,
  resumoSemanalPais: false
};

describe('ConfiguracaoService', () => {
  let service: ConfiguracaoService;
  let http: HttpTestingController;

  beforeEach(() => {
    document.documentElement.className = '';
    TestBed.configureTestingModule({
      providers: [
        ConfiguracaoService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ConfiguracaoService);
    http = TestBed.inject(HttpTestingController);
  });

  it('aplica tema, contraste, fonte e movimento no documento', () => {
    service.aplicarConfiguracoes(configuracoes);

    expect(document.documentElement.classList.contains('dark')).toBe(true);
    expect(document.documentElement.classList.contains('high-contrast')).toBe(true);
    expect(document.documentElement.classList.contains('font-large')).toBe(true);
    expect(document.documentElement.classList.contains('reduce-motion')).toBe(true);
  });

  it('salva configuracoes pela API e aplica seus efeitos', () => {
    service.salvarConfiguracoes(configuracoes).subscribe(resultado => {
      expect(resultado).toEqual(configuracoes);
      expect(document.documentElement.classList.contains('dark')).toBe(true);
    });
    const request = http.expectOne('/api/v1/configuracoes');
    expect(request.request.method).toBe('PUT');
    request.flush(configuracoes);
  });
});
