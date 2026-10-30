import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap, timeout } from 'rxjs';
import { PerfilUsuario, Usuario } from '../../models/models';

interface LoginRequest {
  email: string;
  senha: string;
}

interface LoginResponse {
  token: string;
  usuario: Usuario;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/auth';
  private readonly mockUsuarioUrl = 'assets/data/mock_usuario.json';

  login(dados: LoginRequest): Observable<LoginResponse> {
    if (this.ehCredencialDemo(dados)) {
      return this.http
        .get<PerfilUsuario>(this.mockUsuarioUrl)
        .pipe(
          map(perfil => ({
            token: 'token-demo-local',
            usuario: perfil.usuario
          })),
          tap(resposta => this.persistirSessao(resposta))
        );
    }

    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, dados)
      .pipe(
        timeout(5000),
        tap(resposta => this.persistirSessao(resposta))
      );
  }

  private ehCredencialDemo(dados: LoginRequest): boolean {
    return dados.email === 'joao.silva@exemplo.com' && dados.senha === '123456';
  }

  private persistirSessao(resposta: LoginResponse): void {
    localStorage.setItem('tcc_token', resposta.token);
    localStorage.setItem('tcc_usuario', JSON.stringify(resposta.usuario));
  }

  logout(): void {
    localStorage.removeItem('tcc_token');
    localStorage.removeItem('tcc_usuario');
  }

  estaAutenticado(): boolean {
    return !!localStorage.getItem('tcc_token');
  }

  obterToken(): string | null {
    return localStorage.getItem('tcc_token');
  }

  obterUsuario(): Usuario | null {
    const usuario = localStorage.getItem('tcc_usuario');
    return usuario ? JSON.parse(usuario) : null;
  }
}