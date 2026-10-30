import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth-service/auth.service';

@Component({
  selector: 'app-sidebar',
  imports: [MatIconModule, RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);

  itensMenu = [
    {
      texto: 'Início',
      icone: 'home',
      rota: '/home'
    },
    {
      texto: 'Aulas',
      icone: 'school',
      rota: '/aulas'
    },
    {
      texto: 'Desafios',
      icone: 'emoji_events',
      rota: '/desafios'
    },
    {
      texto: 'Ranking',
      icone: 'leaderboard',
      rota: '/ranking'
    },
    {
      texto: 'Perfil',
      icone: 'person',
      rota: '/usuario'
    },
    {
      texto: 'Configurações',
      icone: 'settings',
      rota: '/configuracoes'
    },
  ];

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
