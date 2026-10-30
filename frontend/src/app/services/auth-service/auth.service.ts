import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
import { Usuario } from '../../models/models';

interface LoginRequest {
  email: string;
  senha: string;
}

interface LoginResponse {
  token: string;
  tipo: string;
  nome: string;
  email: string;
  perfil: string;
  usuarioId: number;
  usuario: Usuario;
}
type RawLoginResponse = Omit<LoginResponse, 'usuario'>;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/auth';

  login(dados: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<RawLoginResponse>(`${this.apiUrl}/login`, dados)
      .pipe(
        map(resposta => ({
          ...resposta,
          usuario: this.mapearUsuario(resposta)
        })),
        tap(resposta => this.persistirSessao(resposta))
      );
  }

  private mapearUsuario(resposta: RawLoginResponse): Usuario {
    return {
      id: resposta.usuarioId,
      nome: resposta.nome,
      email: resposta.email,
      username: resposta.email,
      tituloHonra: resposta.perfil
    };
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