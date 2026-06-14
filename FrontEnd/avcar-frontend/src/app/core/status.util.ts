import { StatusOS } from './models';

export function classeBadge(status: StatusOS): string {
  const mapa: Record<StatusOS, string> = {
    ORCAMENTO:   'badge badge-orcamento',
    EXECUCAO:    'badge badge-execucao',
    PAGAMENTO:   'badge badge-pagamento',
    FINALIZACAO: 'badge badge-finalizacao',
  };
  return mapa[status] ?? 'badge';
}

export function rotuloStatus(status: StatusOS): string {
  const mapa: Record<StatusOS, string> = {
    ORCAMENTO:   'Orçamento',
    EXECUCAO:    'Execução',
    PAGAMENTO:   'Pagamento',
    FINALIZACAO: 'Finalização',
  };
  return mapa[status] ?? status;
}
