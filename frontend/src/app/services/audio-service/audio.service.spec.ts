import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { AudioService } from './audio.service';
import { ConfiguracaoService } from '../configuracao-service/configuracao.service';


describe('AudioService', () => {
  let service: AudioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AudioService,
        {
          provide: ConfiguracaoService,
          useValue: { configuracoes: signal({ efeitosSonoros: false }) }
        }
      ]
    });
    service = TestBed.inject(AudioService);
  });

  it('não toca feedback quando os efeitos sonoros estão desativados', () => {
    expect(() => service.tocar('acerto')).not.toThrow();
  });
});
