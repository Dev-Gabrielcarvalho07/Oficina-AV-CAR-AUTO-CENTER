-- Migração: torna cep e logradouro nullable em pessoa.
-- Motivo: Colaborador herda pessoa (class-table) mas não possui endereço (MER).
-- A obrigatoriedade para Cliente é mantida por validação de aplicação (ClienteService).

ALTER TABLE public.pessoa ALTER COLUMN cep DROP NOT NULL;
ALTER TABLE public.pessoa ALTER COLUMN logradouro DROP NOT NULL;
