import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { firstValueFrom, forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

import { ApiService, ApiResponse } from '../../core/api.service';
import { Veiculo, Modelo, Marca } from '../../core/models';
import { ToastService } from '../../core/toast.service';

interface FormVeiculo {
  placa:         string;
  marca:         string;
  modelo:        string;
  anoFabricacao: number | null;
  cor:           string;
  quilometragem: number | null;
}

@Component({
  selector: 'app-veiculos',
  imports: [CommonModule, FormsModule],
  templateUrl: './veiculos.component.html',
  styleUrl: './veiculos.component.css',
})
export class VeiculosComponent implements OnInit {
  private readonly api   = inject(ApiService);
  private readonly cdr   = inject(ChangeDetectorRef);
  private readonly toast = inject(ToastService);

  veiculos:  Veiculo[] = [];
  modelos:   Modelo[]  = [];
  marcas:    Marca[]   = [];
  carregando = true;

  modalAberto = false;
  salvando    = false;
  erroModal   = '';

  modoEdicao              = false;
  idEdicao: number | null = null;

  form: FormVeiculo = this.formVazio();

  ngOnInit(): void {
    this.carregar();
  }

  private formVazio(): FormVeiculo {
    return { placa: '', marca: '', modelo: '', anoFabricacao: null, cor: '', quilometragem: null };
  }

  carregar(): void {
    this.carregando = true;
    forkJoin({
      veiculos: this.api.get<Veiculo[]>('/veiculos').pipe(
        catchError(() => of<ApiResponse<Veiculo[]>>({ sucesso: false, dados: [] })),
      ),
      modelos: this.api.get<Modelo[]>('/modelos').pipe(
        catchError(() => of<ApiResponse<Modelo[]>>({ sucesso: false, dados: [] })),
      ),
      marcas: this.api.get<Marca[]>('/marcas').pipe(
        catchError(() => of<ApiResponse<Marca[]>>({ sucesso: false, dados: [] })),
      ),
    }).pipe(
      finalize(() => { this.carregando = false; this.cdr.detectChanges(); }),
    ).subscribe({
      next: ({ veiculos, modelos, marcas }) => {
        this.veiculos = veiculos.dados ?? [];
        this.modelos  = modelos.dados  ?? [];
        this.marcas   = marcas.dados   ?? [];
      },
    });
  }

  nomeModelo(idModelo: number): string {
    return this.modelos.find(m => m.id === idModelo)?.nome ?? `#${idModelo}`;
  }

  nomeMarcaDoVeiculo(idModelo: number): string {
    const modelo = this.modelos.find(m => m.id === idModelo);
    if (!modelo) return '—';
    return this.marcas.find(m => m.id === modelo.idMarca)?.nome ?? '—';
  }

  abrirModal(): void {
    this.modoEdicao  = false;
    this.idEdicao    = null;
    this.form        = this.formVazio();
    this.erroModal   = '';
    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  abrirModalEdicao(v: Veiculo): void {
    this.modoEdicao = true;
    this.idEdicao   = v.id;
    this.erroModal  = '';
    const modelo = this.modelos.find(m => m.id === v.idModelo);
    const marca  = modelo ? this.marcas.find(m => m.id === modelo.idMarca) : undefined;
    this.form = {
      placa:         v.placa,
      marca:         marca?.nome  ?? '',
      modelo:        modelo?.nome ?? '',
      anoFabricacao: v.anoFabricacao,
      cor:           v.cor,
      quilometragem: v.quilometragem ?? null,
    };
    this.modalAberto = true;
    this.cdr.detectChanges();
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.salvando    = false;
    this.cdr.detectChanges();
  }

  private async resolverMarca(nome: string): Promise<number> {
    const existente = this.marcas.find(m => m.nome.toLowerCase() === nome.toLowerCase());
    if (existente) return existente.id;

    const r = await firstValueFrom(this.api.post<Marca>('/marcas', { nome }));
    if (!r.sucesso || !r.dados) throw new Error(r.mensagem ?? 'Erro ao criar marca.');
    this.marcas = [...this.marcas, r.dados];
    return r.dados.id;
  }

  private async resolverModelo(nome: string, idMarca: number): Promise<number> {
    const existente = this.modelos.find(
      m => m.nome.toLowerCase() === nome.toLowerCase() && m.idMarca === idMarca,
    );
    if (existente) return existente.id;

    const r = await firstValueFrom(this.api.post<Modelo>('/modelos', { nome, idMarca }));
    if (!r.sucesso || !r.dados) throw new Error(r.mensagem ?? 'Erro ao criar modelo.');
    this.modelos = [...this.modelos, r.dados];
    return r.dados.id;
  }

  async salvar(): Promise<void> {
    if (!this.form.placa.trim())  { this.erroModal = 'Placa é obrigatória.';  return; }
    if (!this.form.marca.trim())  { this.erroModal = 'Informe a marca.';      return; }
    if (!this.form.modelo.trim()) { this.erroModal = 'Informe o modelo.';     return; }
    if (!this.form.anoFabricacao) { this.erroModal = 'Ano é obrigatório.';    return; }
    if (!this.form.cor.trim())    { this.erroModal = 'Cor é obrigatória.';    return; }

    this.salvando  = true;
    this.erroModal = '';
    this.cdr.detectChanges();

    try {
      const idMarca  = await this.resolverMarca(this.form.marca.trim());
      const idModelo = await this.resolverModelo(this.form.modelo.trim(), idMarca);

      const payload: Record<string, unknown> = {
        placa:         this.form.placa.trim().toUpperCase(),
        idModelo,
        anoFabricacao: Number(this.form.anoFabricacao),
        cor:           this.form.cor.trim(),
        quilometragem: this.form.quilometragem != null ? Number(this.form.quilometragem) : undefined,
      };

      console.log('[VeiculosComponent] payload:', payload);

      const req = this.modoEdicao && this.idEdicao
        ? this.api.put<Veiculo>(`/veiculos/${this.idEdicao}`, payload)
        : this.api.post<Veiculo>('/veiculos', payload);

      const r = await firstValueFrom(req);
      if (r.sucesso) {
        this.toast.sucesso(
          this.modoEdicao ? 'Veículo atualizado com sucesso.' : 'Veículo cadastrado com sucesso.',
        );
        this.fecharModal();
        this.carregar();
      } else {
        this.erroModal = r.mensagem ?? 'Erro ao salvar veículo.';
        this.salvando  = false;
        this.cdr.detectChanges();
      }
    } catch (e: unknown) {
      const err = e as { error?: { mensagem?: string }; message?: string };
      this.erroModal = err?.error?.mensagem ?? err?.message ?? 'Erro de comunicação com o servidor.';
      this.salvando  = false;
      this.cdr.detectChanges();
    }
  }
}
