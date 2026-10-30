import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    data: { layout: 'auth' },
    loadComponent: () =>
      import('./login/login').then(m => m.Login)
  },
  {
    path: 'erro-conexao',
    data: { layout: 'auth' },
    loadComponent: () =>
      import('./erros/erro-conexao').then(m => m.ErroConexao)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },
  {
    path: 'home',
    loadComponent: () =>
      import('./home/home').then(m => m.Home),
    canActivate: [authGuard]
  },
  {
    path: 'aulas',
    loadComponent: () =>
      import('./materias/materias').then(m => m.Materias),
    canActivate: [authGuard]
  },
  {
    path: 'aula/:id',
    loadComponent: () =>
      import('./aula/aula').then(m => m.Aula),
    canActivate: [authGuard]
  },
  {
    path: 'desafios',
    loadComponent: () =>
      import('./desafios/desafios').then(m => m.Desafios),
    canActivate: [authGuard]
  },
  {
    path: 'ranking',
    loadComponent: () =>
      import('./ranking/ranking').then(m => m.Ranking),
    canActivate: [authGuard]
  },
  {
    path: 'notificacoes',
    loadComponent: () =>
      import('./notificacoes/notificacoes').then(m => m.Notificacoes),
    canActivate: [authGuard]
  },
  {
    path: 'usuario',
    loadComponent: () =>
      import('./usuario/usuario').then(m => m.Usuario),
    canActivate: [authGuard]
  },
  {
    path: 'configuracoes',
    loadComponent: () =>
      import('./configuracoes/configuracoes').then(m => m.Configuracoes),
    canActivate: [authGuard]
  },
  {
    path: '**',
    loadComponent: () =>
      import('./erros/pagina-nao-encontrada').then(m => m.PaginaNaoEncontrada)
  }
];