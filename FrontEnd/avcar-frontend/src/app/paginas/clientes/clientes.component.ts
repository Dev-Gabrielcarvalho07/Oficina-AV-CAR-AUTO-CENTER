import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { Cliente } from '../../core/models';
import { ToastService } from '../../core/toast.service';

type TipoForm = 'PF' | 'PJ';

interface FormCliente {
  tipo: TipoForm;
  nome: string;
  cpf: string;
  rg: string;
  cnpj: string;
  inscricaoEstadual: string;
  nomeFantasia: string;
  nomeContato: string;
  cep: string;
  logradouro: string;
  numero: string;
  telefone: string;
}

const EMPTY_CLIENTES: ApiResponse<Cliente[]> = { sucesso: false, dados: [] };

@Component({
  selector: 'app-clientes',
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.css',
})
export class ClientesComponent implements OnInit {
  private readonly api   = inject(ApiService);
  private readonly cdr   = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  clientes: Cliente[] = [];
  carregando = true;

  modalAberto = false;
  salvando    = false;
  erroModal   = '';

  modoEdicao              = false;
  idEdicao: number | null = null;

  inativando = new Set<number>();

  form: FormCliente = this.formVazio();

  ngOnInit(): void {
    this.carregar();
  }

  private formVazio(): FormCliente {
    return {
      tipo: 'PF',
      nome: '', cpf: '', rg: '',
      cnpj: '', inscricaoEstadual: '', nomeFantasia: '', nomeContato: '',
      cep: '', logradouro: '', numero: '', telefone: '',
    };
  }

  carregar(): void {
    this.carregando = true;
    this.api.get<Cliente[]>('/clientes').pipe(
      catchError(() => of(EMPTY_CLIENTES)),
      finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }),
    ).subscribe({
      next: r => { this.clientes = r.dados ?? []; },
    });
  }

  isPF(c: Cliente): boolean {
    return !!(c as unknown as Record<string, unknown>)['cpf'];
  }

  documento(c: Cliente): string {
    const x = c as unknown as Record<string, unknown>;
    return (x['cpf'] as string) ?? (x['cnpj'] as string) ?? '—';
  }

  primeiroTelefone(c: Cliente): string {
    return c.telefones?.[0]?.numero ?? '—';
  }

  abrirModal(): void {
    this.modoEdicao  = false;
    this.idEdicao    = null;
    this.form        = this.formVazio();
    this.erroModal   = '';
    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  abrirModalEdicao(c: Cliente): void {
    this.modoEdicao = true;
    this.idEdicao   = c.id;
    this.erroModal  = '';
    const x = c as unknown as Record<string, unknown>;

    if (this.isPF(c)) {
      this.form = {
        tipo: 'PF',
        nome: c.nome,
        cpf:  (x['cpf'] as string) ?? '',
        rg:   (x['rg'] as string) ?? '',
        cnpj: '', inscricaoEstadual: '', nomeFantasia: '', nomeContato: '',
        cep:        c.cep ?? '',
        logradouro: c.logradouro ?? '',
        numero:     c.numero ?? '',
        telefone:   c.telefones?.[0]?.numero ?? '',
      };
    } else {
      this.form = {
        tipo: 'PJ',
        nome: c.nome,
        cpf: '', rg: '',
        cnpj:              (x['cnpj'] as string) ?? '',
        inscricaoEstadual: (x['inscricaoEstadual'] as string) ?? '',
        nomeFantasia:      (x['nomeFantasia'] as string) ?? '',
        nomeContato:       (x['nomeContato'] as string) ?? '',
        cep:        c.cep ?? '',
        logradouro: c.logradouro ?? '',
        numero:     c.numero ?? '',
        telefone:   c.telefones?.[0]?.numero ?? '',
      };
    }

    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.salvando    = false;
    this.cdr.detectChanges();
  }

  salvar(): void {
    if (!this.form.nome.trim()) { this.erroModal = 'Nome é obrigatório.'; return; }

    const telefones = this.form.telefone.trim()
      ? [{ numero: this.form.telefone.trim() }]
      : undefined;

    const base = {
      nome:       this.form.nome.trim(),
      cep:        this.form.cep.trim()        || undefined,
      logradouro: this.form.logradouro.trim() || undefined,
      numero:     this.form.numero.trim()     || undefined,
      telefones,
    };

    let endpoint: string;
    let payload:  Record<string, unknown>;

    if (this.form.tipo === 'PF') {
      if (!this.form.cpf.trim()) { this.erroModal = 'CPF é obrigatório.'; return; }
      endpoint = this.modoEdicao && this.idEdicao
        ? `/clientes/pessoa-fisica/${this.idEdicao}`
        : '/clientes/pessoa-fisica';
      payload  = { ...base, cpf: this.form.cpf.trim(), rg: this.form.rg.trim() || undefined };
    } else {
      if (!this.form.cnpj.trim()) { this.erroModal = 'CNPJ é obrigatório.'; return; }
      endpoint = this.modoEdicao && this.idEdicao
        ? `/clientes/pessoa-juridica/${this.idEdicao}`
        : '/clientes/pessoa-juridica';
      payload  = {
        ...base,
        cnpj:              this.form.cnpj.trim(),
        inscricaoEstadual: this.form.inscricaoEstadual.trim() || undefined,
        nomeFantasia:      this.form.nomeFantasia.trim()      || undefined,
        nomeContato:       this.form.nomeContato.trim()       || undefined,
      };
    }

    this.salvando  = true;
    this.erroModal = '';

    const req = this.modoEdicao && this.idEdicao
      ? this.api.put<Cliente>(endpoint, payload)
      : this.api.post<Cliente>(endpoint, payload);

    req.subscribe({
      next: r => {
        if (r.sucesso) {
          this.toast.sucesso(this.modoEdicao ? 'Cliente atualizado.' : 'Cliente cadastrado.');
          this.fecharModal();
          this.carregar();
        } else {
          this.erroModal = r.mensagem ?? 'Erro ao salvar cliente.';
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

  inativar(c: Cliente): void {
    if (!confirm(`Deseja inativar o cliente "${c.nome}"?`)) return;
    this.inativando.add(c.id);
    this.cdr.detectChanges();

    this.api.patch<Cliente>(`/clientes/${c.id}/inativar`).subscribe({
      next: () => {
        const idx = this.clientes.findIndex(x => x.id === c.id);
        if (idx !== -1) this.clientes[idx] = { ...this.clientes[idx], ativo: false };
        this.inativando.delete(c.id);
        this.toast.sucesso(`Cliente "${c.nome}" inativado.`);
        this.cdr.detectChanges();
      },
      error: () => {
        this.inativando.delete(c.id);
        this.toast.erro('Erro ao inativar cliente.');
        this.cdr.detectChanges();
      },
    });
  }

  estaInativando(id: number): boolean {
    return this.inativando.has(id);
  }
}
