import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth-service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class Login {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  email = '';
  senha = '';
  carregando = false;
  erro = '';

  entrar(): void {
    this.erro = '';

    if (!this.email || !this.senha) {
      this.erro = 'Informe o e-mail e a senha.';
      return;
    }

    this.carregando = true;

    this.authService.login({
      email: this.email,
      senha: this.senha
    }).subscribe({
      next: () => this.router.navigate(['/home']),
      error: () => {
        this.erro = 'Não foi possível realizar o login.';
        this.carregando = false;
      }
    });
  }
}