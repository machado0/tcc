import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Usuario } from './usuario';
import { UsuarioService } from '../services/usuario-service/usuario.service';
import { PerfilUsuario } from '../models/models';

const perfil: PerfilUsuario = {
  usuario: {
    id: 1,
    nome: 'Aluno',
    email: 'aluno@teste.com',
    username: '@aluno'
  },
  estatisticas: {
    nivel: 1,
    xpAtual: 0,
    xpProximoNivel: 250,
    xpHoje: 0,
    sequenciaDias: 1,
    melhorSequenciaDias: 1,
    moedas: 0,
    vidas: 3
  },
  metricas: {
    questoesRespondidas: 0,
    questoesCorretas: 0,
    aulasConcluidas: 0,
    tempoTotalEstudoMinutos: 0,
    taxaAcerto: 0
  },
  conquistas: []
};

describe('Usuario', () => {
  let component: Usuario;
  let fixture: ComponentFixture<Usuario>;
  const service = {
    obterPerfil: () => of(perfil),
    atualizarPerfil: (usuario: PerfilUsuario['usuario']) => of(usuario)
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Usuario],
      providers: [
        provideRouter([]),
        { provide: UsuarioService, useValue: service }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Usuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('carrega o perfil do usuario', () => {
    expect(component.perfil()?.usuario.nome).toBe('Aluno');
  });

  it('salva a edicao do perfil', () => {
    component.iniciarEdicao();
    component.nomeEditado = 'Aluno Atualizado';
    component.salvarPerfil();

    expect(component.perfil()?.usuario.nome).toBe('Aluno Atualizado');
    expect(component.editando()).toBe(false);
  });
});
