import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of, switchMap } from 'rxjs';
import { PerfilUsuario, Usuario } from '../../models/models';
import { GamificacaoService } from '../gamificacao-service/gamificacao.service';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly gamificacaoService = inject(GamificacaoService);
  private readonly chaveUsuario = 'tcc_usuario_perfil';

  obterPerfil(): Observable<PerfilUsuario> {
    return this.http.get<PerfilUsuario>('assets/data/mock_usuario.json').pipe(
      map(perfil => this.aplicarUsuarioSalvo(perfil)),
      switchMap(perfil => this.gamificacaoService.carregar().pipe(
        map(estado => ({
          ...perfil,
          estatisticas: estado.estatisticas,
          metricas: estado.metricas,
          conquistas: estado.conquistas
        }))
      ))
    );
  }

  atualizarPerfil(usuario: Usuario): Observable<Usuario> {
    localStorage.setItem(this.chaveUsuario, JSON.stringify(usuario));
    localStorage.setItem('tcc_usuario', JSON.stringify(usuario));
    return of(usuario);
  }

  private aplicarUsuarioSalvo(perfil: PerfilUsuario): PerfilUsuario {
    const salvo = localStorage.getItem(this.chaveUsuario);

    if (!salvo) {
      return perfil;
    }

    try {
      return {
        ...perfil,
        usuario: {
          ...perfil.usuario,
          ...(JSON.parse(salvo) as Usuario)
        }
      };
    } catch {
      localStorage.removeItem(this.chaveUsuario);
      return perfil;
    }
  }
}

