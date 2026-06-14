import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { OrdemServico } from '../../core/models';
import { classeBadge, rotuloStatus } from '../../core/status.util';
import { ToastService } from '../../core/toast.service';

const EMPTY_OS: ApiResponse<OrdemServico[]> = { sucesso: false, dados: [] };

@Component({
  selector: 'app-triagem',
  imports: [CommonModule, FormsModule],
  templateUrl: './triagem.component.html',
  styleUrl: './triagem.component.css',
})
export class TriagemComponent implements OnInit {
  private readonly api   = inject(ApiService);
  private readonly cdr   = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  fila: OrdemServico[]           = [];
  emAtendimento: OrdemServico | null = null;
  carregando   = true;
  chamando     = false;
  enfileirando = false;
  saindoId: number | null = null;
  inputOs  = '';
  erroFila = '';

  readonly classeBadge  = classeBadge;
  readonly rotuloStatus = rotuloStatus;

  ngOnInit(): void {
    this.carregarFila();
  }

  carregarFila(): void {
    this.carregando = true;
    this.api.get<OrdemServico[]>('/triagem').pipe(
      catchError(() => of(EMPTY_OS)),
      finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }),
    ).subscribe({
      next: r => { this.fila = r.dados ?? []; },
    });
  }

  ordinal(i: number): string {
    return `${i + 1}º`;
  }

  chamarProximo(): void {
    if (this.fila.length === 0 || this.chamando) return;

    this.chamando = true;
    this.saindoId = this.fila[0].id;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.api.post<OrdemServico>('/triagem/chamar-proximo', {}).subscribe({
        next: r => {
          this.emAtendimento = r.dados ?? null;
          this.fila          = this.fila.slice(1);
          this.saindoId      = null;
          this.chamando      = false;
          if (r.dados) this.toast.sucesso(`OS #${r.dados.numeroOs} chamada para atendimento.`);
          this.cdr.detectChanges();
        },
        error: e => {
          this.erroFila = e?.error?.mensagem ?? 'Fila vazia ou erro ao chamar.';
          this.saindoId = null;
          this.chamando = false;
          this.cdr.detectChanges();
        },
      });
    }, 320);
  }

  enfileirar(): void {
    const idOs = parseInt(this.inputOs, 10);
    if (isNaN(idOs) || idOs < 1) {
      this.erroFila = 'Informe um número de OS válido.';
      this.cdr.detectChanges();
      return;
    }

    this.enfileirando = true;
    this.erroFila     = '';

    this.api.post<OrdemServico>('/triagem/enfileirar', { idOs }).subscribe({
      next: r => {
        this.enfileirando = false;
        if (r.sucesso) {
          this.inputOs = '';
          this.toast.sucesso('OS adicionada à fila de triagem.');
          this.carregarFila();
        } else {
          this.erroFila = r.mensagem ?? 'Erro ao enfileirar OS.';
          this.cdr.detectChanges();
        }
      },
      error: e => {
        this.erroFila     = e?.error?.mensagem ?? 'Erro de comunicação.';
        this.enfileirando = false;
        this.cdr.detectChanges();
      },
    });
  }
}
