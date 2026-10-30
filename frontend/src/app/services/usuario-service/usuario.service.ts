import { Injectable, inject } from '@angular/core';
import { Observable, forkJoin, map } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { PerfilUsuario, Usuario } from '../../models/models';
import { GamificacaoService } from '../gamificacao-service/gamificacao.service';
import { AuthService } from '../auth-service/auth.service';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly gamificacaoService = inject(GamificacaoService);
  private readonly authService = inject(AuthService);
  private readonly http = inject(HttpClient);

  obterPerfil(): Observable<PerfilUsuario> {
    return forkJoin({
      usuario: this.http.get<Usuario>('/api/v1/perfil'),
      estado: this.gamificacaoService.carregar()
    }).pipe(
      map(({ usuario, estado }) => ({
        usuario,
        estatisticas: estado.estatisticas,
        metricas: estado.metricas,
        conquistas: estado.conquistas
      }))
    );
  }

  atualizarPerfil(usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>('/api/v1/perfil', {
      id: usuario.id,
      nome: usuario.nome,
      username: usuario.username ?? usuario.email,
      email: usuario.email,
      tituloHonra: usuario.tituloHonra
    }).pipe(
      map(atualizado => {
        localStorage.setItem('tcc_usuario', JSON.stringify(atualizado));
        return atualizado;
      })
    );
  }
}
