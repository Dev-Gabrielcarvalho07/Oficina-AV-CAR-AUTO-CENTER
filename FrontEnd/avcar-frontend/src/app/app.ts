import { Component, signal, inject } from '@angular/core';
import { Router, NavigationEnd, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { ToastComponent } from './shared/toast/toast.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly pageTitle = signal('Dashboard');

  private readonly routeTitles: Record<string, string> = {
    '/dashboard':      'Dashboard',
    '/clientes':       'Clientes',
    '/veiculos':       'Veículos',
    '/ordens-servico': 'Ordens de Serviço',
    '/triagem':        'Fila de Triagem',
    '/cadastros':      'Cadastros',
  };

  constructor() {
    const router = inject(Router);
    router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(e => {
      const url = (e as NavigationEnd).urlAfterRedirects;
      this.pageTitle.set(this.routeTitles[url] ?? 'AV CAR');
    });
  }
}
