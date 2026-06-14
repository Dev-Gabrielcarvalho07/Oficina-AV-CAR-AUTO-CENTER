export type StatusOS   = 'ORCAMENTO' | 'EXECUCAO' | 'PAGAMENTO' | 'FINALIZACAO';
export type Prioridade = 'BAIXA' | 'NORMAL' | 'ALTA' | 'URGENTE';

export interface Marca {
  id: number;
  nome: string;
}

export interface Modelo {
  id: number;
  nome: string;
  idMarca: number;
}

export interface Veiculo {
  id: number;
  placa: string;
  anoFabricacao: number;
  cor: string;
  idModelo: number;
  quilometragem?: number;
}

export interface Funcao {
  id: number;
  nome: string;
  salarioBase?: number;
}

export interface Colaborador {
  id: number;
  nome: string;
  matricula: string;
  dataAdmissao: string;
  idFuncao: number;
}

export interface Fornecedor {
  id: number;
  nome: string;
  cnpj: string;
  tipoFornecedor: string;
  telefone?: string;
  email?: string;
}

export interface Peca {
  id: number;
  codigoNacional: string;
  nome: string;
  valor: number;
  garantia?: string;
}

export interface ServicoInterno {
  id: number;
  descricao: string;
  valorBase: number;
  garantia?: string;
}

export interface Cliente {
  id: number;
  nome: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  cidade?: string;
  uf?: string;
  ativo: boolean;
  dataCadastro?: string;
  telefones?: { numero: string }[];
}

export interface PessoaFisica extends Cliente {
  cpf: string;
  rg?: string;
}

export interface PessoaJuridica extends Cliente {
  cnpj: string;
  inscricaoEstadual?: string;
  nomeFantasia?: string;
  nomeContato?: string;
}

export interface OrdemServico {
  id: number;
  numeroOs: number;
  dataEntrada: string;
  dataSaida?: string;
  statusOs: StatusOS;
  prioridade: Prioridade;
  valorMaoObra?: number;
  valorPecas?: number;
  deslocamento?: number;
  servicoGuincho?: number;
  outros?: number;
  total?: number;
  idPessoa: number;
  idVeiculo: number;
}
