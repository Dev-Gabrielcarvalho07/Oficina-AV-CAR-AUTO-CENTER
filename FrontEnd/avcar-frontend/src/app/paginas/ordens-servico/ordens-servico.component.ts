import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { OrdemServico, Cliente, Veiculo } from '../../core/models';
import { classeBadge, rotuloStatus } from '../../core/status.util';
import { ToastService } from '../../core/toast.service';

interface FormNovaOS {
  idPessoa:       number | null;
  idVeiculo:      number | null;
  valorMaoObra:   number | null;
  valorPecas:     number | null;
  deslocamento:   number | null;
  servicoGuincho: number | null;
  outros:         number | null;
}

@Component({
  selector: 'app-ordens-servico',
  imports: [CommonModule, FormsModule],
  templateUrl: './ordens-servico.component.html',
  styleUrl: './ordens-servico.component.css',
})
export class OrdensServicoComponent implements OnInit {
  private readonly api   = inject(ApiService);
  private readonly cdr   = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  ordens:    OrdemServico[] = [];
  clientes:  Cliente[]      = [];
  veiculos:  Veiculo[]      = [];
  carregando                = true;
  erroBusca                 = '';
  termoBusca                = '';
  avancando: Set<number>    = new Set();

  modalAberto = false;
  salvando    = false;
  erroModal   = '';

  form: FormNovaOS = this.formVazio();

  readonly classeBadge  = classeBadge;
  readonly rotuloStatus = rotuloStatus;

  ngOnInit(): void {
    this.carregar();
  }

  private formVazio(): FormNovaOS {
    return {
      idPessoa: null, idVeiculo: null,
      valorMaoObra: null, valorPecas: null,
      deslocamento: null, servicoGuincho: null, outros: null,
    };
  }

  carregar(): void {
    this.carregando = true;
    const em = <T>() => of<ApiResponse<T[]>>({ sucesso: false, dados: [] });
    forkJoin({
      ordens:   this.api.get<OrdemServico[]>('/ordens-servico').pipe(catchError(em<OrdemServico>)),
      clientes: this.api.get<Cliente[]>('/clientes').pipe(catchError(em<Cliente>)),
      veiculos: this.api.get<Veiculo[]>('/veiculos').pipe(catchError(em<Veiculo>)),
    }).pipe(
      finalize(() => { this.carregando = false; this.cdr.detectChanges(); }),
    ).subscribe({
      next: ({ ordens, clientes, veiculos }) => {
        this.ordens   = ordens.dados   ?? [];
        this.clientes = clientes.dados ?? [];
        this.veiculos = veiculos.dados ?? [];
      },
    });
  }

  nomeCliente(idPessoa: number): string {
    return this.clientes.find(c => c.id === idPessoa)?.nome ?? '—';
  }

  placaVeiculo(idVeiculo: number): string {
    return this.veiculos.find(v => v.id === idVeiculo)?.placa ?? '—';
  }

  buscarPorNumero(event: KeyboardEvent): void {
    if (event.key !== 'Enter') return;
    const num = parseInt(this.termoBusca, 10);
    if (isNaN(num)) { this.carregar(); return; }

    this.carregando = true;
    this.erroBusca  = '';

    this.api.get<OrdemServico>(`/ordens-servico/buscar/${num}`).pipe(
      catchError(() => {
        this.erroBusca = `OS #${num} não encontrada.`;
        this.ordens    = [];
        return of({ sucesso: false, dados: null as unknown as OrdemServico });
      }),
      finalize(() => { this.carregando = false; this.cdr.detectChanges(); }),
    ).subscribe({
      next: r => {
        if (r.sucesso && r.dados) {
          this.ordens = [r.dados];
        } else if (r.sucesso) {
          this.ordens    = [];
          this.erroBusca = `OS #${num} não encontrada.`;
        }
      },
    });
  }

  limparBusca(): void {
    if (!this.termoBusca) return;
    this.termoBusca = '';
    this.erroBusca  = '';
    this.carregar();
  }

  avancar(os: OrdemServico): void {
    if (os.statusOs === 'FINALIZACAO') return;
    this.avancando.add(os.id);
    this.cdr.detectChanges();

    this.api.patch<OrdemServico>(`/ordens-servico/${os.id}/avancar`).subscribe({
      next: r => {
        const atualizado = r.dados;
        const idx = this.ordens.findIndex(o => o.id === os.id);
        if (idx !== -1 && atualizado) this.ordens[idx] = atualizado;
        this.avancando.delete(os.id);
        this.toast.sucesso('Status avançado com sucesso.');
        this.cdr.detectChanges();
      },
      error: e => {
        this.avancando.delete(os.id);
        this.toast.erro(e?.error?.mensagem ?? 'Erro ao avançar status.');
        this.cdr.detectChanges();
      },
    });
  }

  abrirModal(): void {
    this.form        = this.formVazio();
    this.erroModal   = '';
    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.salvando    = false;
    this.cdr.detectChanges();
  }

  salvar(): void {
    if (!this.form.idPessoa)  { this.erroModal = 'Selecione o cliente.'; return; }
    if (!this.form.idVeiculo) { this.erroModal = 'Selecione o veículo.'; return; }

    this.salvando  = true;
    this.erroModal = '';

    const payload: Partial<OrdemServico> = {
      idPessoa:       this.form.idPessoa,
      idVeiculo:      this.form.idVeiculo,
      valorMaoObra:   this.form.valorMaoObra   ?? undefined,
      valorPecas:     this.form.valorPecas     ?? undefined,
      deslocamento:   this.form.deslocamento   ?? undefined,
      servicoGuincho: this.form.servicoGuincho ?? undefined,
      outros:         this.form.outros         ?? undefined,
    };

    this.api.post<OrdemServico>('/ordens-servico', payload).subscribe({
      next: r => {
        if (r.sucesso) {
          this.toast.sucesso('OS aberta com sucesso.');
          this.fecharModal();
          this.carregar();
        } else {
          this.erroModal = r.mensagem ?? 'Erro ao abrir OS.';
          this.salvando  = false;
          this.cdr.detectChanges();
        }
      },
      error: e => {
        this.erroModal = e?.error?.mensagem ?? 'Erro de comunicação com o servidor.';
        this.salvando  = false;
        this.cdr.detectChanges();
      },
    });
  }

  estaAvancando(id: number): boolean {
    return this.avancando.has(id);
  }

  formatarMoeda(v?: number): string {
    if (v == null) return '—';
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
