import { TestBed } from '@angular/core/testing';
import { AudioService } from './audio.service';


describe('AudioService', () => {
  let service: AudioService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [AudioService] });
    service = TestBed.inject(AudioService);
  });

  it('não toca feedback quando os efeitos sonoros estão desativados', () => {
    localStorage.setItem(
      'tcc_configuracoes_usuario',
      JSON.stringify({ efeitosSonoros: false })
    );

    expect(() => service.tocar('acerto')).not.toThrow();
  });
});
