import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { ToastService } from '../../core/toast.service';
import {
  Funcao, Colaborador, Fornecedor, Peca, ServicoInterno,
} from '../../core/models';

export type AbaId = 'funcoes' | 'colaboradores' | 'fornecedores' | 'pecas' | 'servicos';

interface FormCadastros {
  nome:           string;
  descricao:      string;
  salarioBase:    number | null;
  matricula:      string;
  dataAdmissao:   string;
  idFuncao:       number | null;
  cnpj:           string;
  tipoFornecedor: string;
  telefone:       string;
  email:          string;
  codigoNacional: string;
  valor:          number | null;
  garantia:       string;
  valorBase:      number | null;
}

@Component({
  selector: 'app-cadastros',
  imports: [CommonModule, FormsModule],
  templateUrl: './cadastros.component.html',
  styleUrl: './cadastros.component.css',
})
export class CadastrosComponent implements OnInit {
  private readonly api   = inject(ApiService);
  private readonly cdr   = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  readonly abas: { id: AbaId; label: string; novo: string }[] = [
    { id: 'funcoes',       label: 'Funções',       novo: 'Nova Função' },
    { id: 'colaboradores', label: 'Colaboradores', novo: 'Novo Colaborador' },
    { id: 'fornecedores',  label: 'Fornecedores',  novo: 'Novo Fornecedor' },
    { id: 'pecas',         label: 'Peças',         novo: 'Nova Peça' },
    { id: 'servicos',      label: 'Serviços',      novo: 'Novo Serviço' },
  ];

  abaAtiva: AbaId = 'funcoes';
  carregando = true;

  modalAberto = false;
  salvando    = false;
  erroModal   = '';

  modoEdicao              = false;
  idEdicao: number | null = null;

  form: FormCadastros = this.formVazio();

  // Dados por aba
  funcoes:       Funcao[]         = [];
  colaboradores: Colaborador[]    = [];
  fornecedores:  Fornecedor[]     = [];
  pecas:         Peca[]           = [];
  servicos:      ServicoInterno[] = [];

  // Selects dependentes
  funcoesSelect: Funcao[] = [];

  ngOnInit(): void {
    this.carregarAba();
  }

  ativarAba(id: AbaId): void {
    if (this.abaAtiva === id) return;
    this.abaAtiva    = id;
    this.modalAberto = false;
    this.modoEdicao  = false;
    this.idEdicao    = null;
    this.form        = this.formVazio();
    this.carregando  = true;
    this.cdr.detectChanges();
    this.carregarAba();
  }

  carregarAba(): void {
    this.carregando = true;
    const done = () => { this.carregando = false; this.cdr.detectChanges(); };
    const em = <T>() => of<ApiResponse<T[]>>({ sucesso: false, dados: [] });

    switch (this.abaAtiva) {
      case 'funcoes':
        this.api.get<Funcao[]>('/funcoes').pipe(catchError(em<Funcao>), finalize(done))
          .subscribe({ next: r => { this.funcoes = r.dados ?? []; } });
        break;

      case 'colaboradores':
        forkJoin({
          colaboradores: this.api.get<Colaborador[]>('/colaboradores').pipe(catchError(em<Colaborador>)),
          funcoes:       this.api.get<Funcao[]>('/funcoes').pipe(catchError(em<Funcao>)),
        }).pipe(finalize(done)).subscribe({ next: ({ colaboradores, funcoes }) => {
          this.colaboradores = colaboradores.dados ?? [];
          this.funcoesSelect = funcoes.dados       ?? [];
        }});
        break;

      case 'fornecedores':
        this.api.get<Fornecedor[]>('/fornecedores').pipe(catchError(em<Fornecedor>), finalize(done))
          .subscribe({ next: r => { this.fornecedores = r.dados ?? []; } });
        break;

      case 'pecas':
        this.api.get<Peca[]>('/pecas').pipe(catchError(em<Peca>), finalize(done))
          .subscribe({ next: r => { this.pecas = r.dados ?? []; } });
        break;

      case 'servicos':
        this.api.get<ServicoInterno[]>('/servicos-internos').pipe(catchError(em<ServicoInterno>), finalize(done))
          .subscribe({ next: r => { this.servicos = r.dados ?? []; } });
        break;
    }
  }

  tituloNovo(): string {
    return this.abas.find(a => a.id === this.abaAtiva)?.novo ?? 'Novo';
  }

  tituloModal(): string {
    if (!this.modoEdicao) return this.tituloNovo();
    const editar: Record<AbaId, string> = {
      funcoes: 'Editar Função', colaboradores: 'Editar Colaborador',
      fornecedores: 'Editar Fornecedor', pecas: 'Editar Peça',
      servicos: 'Editar Serviço',
    };
    return editar[this.abaAtiva];
  }

  nomeFuncao(idFuncao: number): string {
    return this.funcoesSelect.find(f => f.id === idFuncao)?.nome ?? `#${idFuncao}`;
  }

  labelTipo(tipo: string): string {
    return ({ PECAS: 'Peças', SERVICO: 'Serviço', AMBOS: 'Ambos' } as Record<string, string>)[tipo] ?? tipo;
  }

  formatarMoeda(v?: number): string {
    if (v == null) return '—';
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  private formVazio(): FormCadastros {
    return {
      nome: '', descricao: '',
      salarioBase: null,
      matricula: '', dataAdmissao: '', idFuncao: null,
      cnpj: '', tipoFornecedor: 'PECAS', telefone: '', email: '',
      codigoNacional: '', valor: null, garantia: '', valorBase: null,
    };
  }

  abrirModal(): void {
    this.modoEdicao  = false;
    this.idEdicao    = null;
    this.form        = this.formVazio();
    this.erroModal   = '';
    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  abrirModalEdicao(item: unknown): void {
    const obj = item as Record<string, unknown>;
    this.modoEdicao  = true;
    this.idEdicao    = obj['id'] as number;
    this.erroModal   = '';
    this.form        = this.formVazio();

    switch (this.abaAtiva) {
      case 'funcoes':
        this.form.nome        = String(obj['nome'] ?? '');
        this.form.salarioBase = (obj['salarioBase'] as number) ?? null;
        break;
      case 'colaboradores':
        this.form.nome         = String(obj['nome'] ?? '');
        this.form.matricula    = String(obj['matricula'] ?? '');
        this.form.dataAdmissao = String(obj['dataAdmissao'] ?? '');
        this.form.idFuncao     = (obj['idFuncao'] as number) ?? null;
        break;
      case 'fornecedores':
        this.form.nome           = String(obj['nome'] ?? '');
        this.form.cnpj           = String(obj['cnpj'] ?? '');
        this.form.tipoFornecedor = String(obj['tipoFornecedor'] ?? 'PECAS');
        this.form.telefone       = String(obj['telefone'] ?? '');
        this.form.email          = String(obj['email'] ?? '');
        break;
      case 'pecas':
        this.form.codigoNacional = String(obj['codigoNacional'] ?? '');
        this.form.nome           = String(obj['nome'] ?? '');
        this.form.valor          = (obj['valor'] as number) ?? null;
        this.form.garantia       = String(obj['garantia'] ?? '');
        break;
      case 'servicos':
        this.form.descricao = String(obj['descricao'] ?? '');
        this.form.valorBase = (obj['valorBase'] as number) ?? null;
        this.form.garantia  = String(obj['garantia'] ?? '');
        break;
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
    let endpoint: string;
    let payload: Record<string, unknown>;

    switch (this.abaAtiva) {
      case 'funcoes':
        if (!this.form.nome.trim()) { this.erroModal = 'Nome é obrigatório.'; return; }
        endpoint = '/funcoes';
        payload  = { nome: this.form.nome.trim(), salarioBase: this.form.salarioBase ?? undefined };
        break;

      case 'colaboradores':
        if (!this.form.nome.trim())      { this.erroModal = 'Nome é obrigatório.'; return; }
        if (!this.form.matricula.trim()) { this.erroModal = 'Matrícula é obrigatória.'; return; }
        if (!this.form.dataAdmissao)     { this.erroModal = 'Data de admissão é obrigatória.'; return; }
        if (!this.form.idFuncao)         { this.erroModal = 'Selecione a função.'; return; }
        endpoint = '/colaboradores';
        payload  = {
          nome:         this.form.nome.trim(),
          matricula:    this.form.matricula.trim(),
          dataAdmissao: this.form.dataAdmissao,
          idFuncao:     this.form.idFuncao,
        };
        break;

      case 'fornecedores':
        if (!this.form.nome.trim()) { this.erroModal = 'Nome é obrigatório.'; return; }
        if (!this.form.cnpj.trim()) { this.erroModal = 'CNPJ é obrigatório.'; return; }
        endpoint = '/fornecedores';
        payload  = {
          nome:           this.form.nome.trim(),
          cnpj:           this.form.cnpj.trim(),
          tipoFornecedor: this.form.tipoFornecedor,
          telefone:       this.form.telefone.trim() || undefined,
          email:          this.form.email.trim()    || undefined,
        };
        break;

      case 'pecas':
        if (!this.form.codigoNacional.trim()) { this.erroModal = 'Código Nacional é obrigatório.'; return; }
        if (!this.form.nome.trim())           { this.erroModal = 'Nome é obrigatório.'; return; }
        if (this.form.valor === null)          { this.erroModal = 'Valor é obrigatório.'; return; }
        endpoint = '/pecas';
        payload  = {
          codigoNacional: this.form.codigoNacional.trim(),
          nome:           this.form.nome.trim(),
          valor:          this.form.valor,
          garantia:       this.form.garantia.trim() || undefined,
        };
        break;

      case 'servicos':
        if (!this.form.descricao.trim()) { this.erroModal = 'Descrição é obrigatória.'; return; }
        if (this.form.valorBase === null) { this.erroModal = 'Valor Base é obrigatório.'; return; }
        endpoint = '/servicos-internos';
        payload  = {
          descricao: this.form.descricao.trim(),
          valorBase: this.form.valorBase,
          garantia:  this.form.garantia.trim() || undefined,
        };
        break;

      default:
        return;
    }

    this.salvando  = true;
    this.erroModal = '';

    const url = this.modoEdicao && this.idEdicao ? `${endpoint}/${this.idEdicao}` : endpoint;
    const req = this.modoEdicao && this.idEdicao
      ? this.api.put<unknown>(url, payload)
      : this.api.post<unknown>(endpoint, payload);

    req.subscribe({
      next: r => {
        if (r.sucesso) {
          this.toast.sucesso(this.modoEdicao ? 'Registro atualizado.' : 'Registro salvo.');
          this.fecharModal();
          this.carregarAba();
        } else {
          this.erroModal = r.mensagem ?? 'Erro ao salvar.';
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
}
