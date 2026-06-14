import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { OrdemServico } from '../../core/models';
import { classeBadge, rotuloStatus } from '../../core/status.util';

const EMPTY_LIST: ApiResponse<OrdemServico[]> = { sucesso: false, dados: [] };

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  carregando = true;

  osAbertas  = 0;
  emExecucao = 0;
  naFila     = 0;
  faturado   = 0;

  recentes: OrdemServico[] = [];

  readonly classeBadge  = classeBadge;
  readonly rotuloStatus = rotuloStatus;

  ngOnInit(): void {
    forkJoin({
      ordens: this.api.get<OrdemServico[]>('/ordens-servico').pipe(
        catchError(() => of(EMPTY_LIST)),
      ),
      fila: this.api.get<OrdemServico[]>('/triagem').pipe(
        catchError(() => of(EMPTY_LIST)),
      ),
    }).pipe(
      finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }),
    ).subscribe({
      next: ({ ordens, fila }) => {
        const lista = ordens.dados ?? [];
        this.osAbertas  = lista.filter(o => o.statusOs !== 'FINALIZACAO').length;
        this.emExecucao = lista.filter(o => o.statusOs === 'EXECUCAO').length;
        this.naFila     = (fila.dados ?? []).length;
        this.faturado   = lista.reduce((s, o) => s + (o.total ?? 0), 0);
        this.recentes   = [...lista].reverse().slice(0, 5);
      },
    });
  }

  formatarMoeda(v: number): string {
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
