import { Component, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';
import { Header } from './components/header/header';
import { Sidebar } from './components/sidebar/sidebar';
import { ConfiguracaoService } from './services/configuracao-service/configuracao.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Sidebar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly router = inject(Router);
  private readonly configuracaoService = inject(ConfiguracaoService);
  protected readonly title = signal('tcc');
  protected readonly isAuthLayout = toSignal(
    this.router.events.pipe(
      filter(
        (event): event is NavigationEnd => event instanceof NavigationEnd
      ),
      map(() => this.router.routerState.root.firstChild?.snapshot.data['layout'] === 'auth')
    ),
    {
      initialValue: this.router.routerState.root.firstChild?.snapshot.data['layout'] === 'auth'
    }
  );

  ngOnInit(): void {
    this.configuracaoService.aplicarConfiguracaoSalva();
  }
}
