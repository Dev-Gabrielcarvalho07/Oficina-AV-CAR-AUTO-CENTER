import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  {
    path: 'dashboard',
    loadComponent: () => import('./paginas/dashboard/dashboard.component')
      .then(m => m.DashboardComponent)
  },
  {
    path: 'clientes',
    loadComponent: () => import('./paginas/clientes/clientes.component')
      .then(m => m.ClientesComponent)
  },
  {
    path: 'veiculos',
    loadComponent: () => import('./paginas/veiculos/veiculos.component')
      .then(m => m.VeiculosComponent)
  },
  {
    path: 'ordens-servico',
    loadComponent: () => import('./paginas/ordens-servico/ordens-servico.component')
      .then(m => m.OrdensServicoComponent)
  },
  {
    path: 'triagem',
    loadComponent: () => import('./paginas/triagem/triagem.component')
      .then(m => m.TriagemComponent)
  },
  {
    path: 'cadastros',
    loadComponent: () => import('./paginas/cadastros/cadastros.component')
      .then(m => m.CadastrosComponent)
  },
];
