import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  mensagem: string;
  tipo: 'sucesso' | 'erro';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  readonly toasts = signal<Toast[]>([]);
  private nextId = 0;

  sucesso(mensagem: string): void { this._add(mensagem, 'sucesso'); }
  erro(mensagem: string): void    { this._add(mensagem, 'erro'); }

  remover(id: number): void {
    this.toasts.update(ts => ts.filter(t => t.id !== id));
  }

  private _add(mensagem: string, tipo: 'sucesso' | 'erro'): void {
    const id = ++this.nextId;
    this.toasts.update(ts => [...ts, { id, mensagem, tipo }]);
    setTimeout(() => this.remover(id), 3000);
  }
}
