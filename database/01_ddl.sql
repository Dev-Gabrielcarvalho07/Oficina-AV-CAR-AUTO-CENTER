--
-- PostgreSQL database dump
--

\restrict SOdA8G0TwfYnnowuT4suCh4vFFLfldPPTKqbplvJVlT9a1lEFstn6Z1amXZ6cQW

-- Dumped from database version 17.9
-- Dumped by pg_dump version 17.9

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: prioridade_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.prioridade_enum AS ENUM (
    'BAIXA',
    'NORMAL',
    'ALTA',
    'URGENTE'
);


ALTER TYPE public.prioridade_enum OWNER TO postgres;

--
-- Name: status_os_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.status_os_enum AS ENUM (
    'ORCAMENTO',
    'EXECUCAO',
    'PAGAMENTO',
    'FINALIZACAO'
);


ALTER TYPE public.status_os_enum OWNER TO postgres;

--
-- Name: status_peca_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.status_peca_enum AS ENUM (
    'PEDIDO',
    'AGUARDANDO_PAGAMENTO',
    'SEPARACAO_MATERIAL',
    'ENTREGA'
);


ALTER TYPE public.status_peca_enum OWNER TO postgres;

--
-- Name: tipo_fornecedor_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_fornecedor_enum AS ENUM (
    'PECAS',
    'SERVICO',
    'AMBOS'
);


ALTER TYPE public.tipo_fornecedor_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_pessoa integer NOT NULL,
    data_cadastro date,
    ativo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- Name: colaborador; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.colaborador (
    id_pessoa integer NOT NULL,
    matricula character varying(20) NOT NULL,
    data_admissao date,
    id_funcao integer NOT NULL
);


ALTER TABLE public.colaborador OWNER TO postgres;

--
-- Name: fornecedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fornecedor (
    id_fornecedor integer NOT NULL,
    nome character varying(100) NOT NULL,
    cnpj character varying(18),
    tipo_fornecedor public.tipo_fornecedor_enum NOT NULL,
    telefone character varying(15),
    email character varying(100)
);


ALTER TABLE public.fornecedor OWNER TO postgres;

--
-- Name: fornecedor_id_fornecedor_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.fornecedor_id_fornecedor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.fornecedor_id_fornecedor_seq OWNER TO postgres;

--
-- Name: fornecedor_id_fornecedor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.fornecedor_id_fornecedor_seq OWNED BY public.fornecedor.id_fornecedor;


--
-- Name: funcao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.funcao (
    id_funcao integer NOT NULL,
    nome character varying(60) NOT NULL,
    salario_base numeric(10,2),
    CONSTRAINT funcao_salario_base_check CHECK ((salario_base >= (0)::numeric))
);


ALTER TABLE public.funcao OWNER TO postgres;

--
-- Name: funcao_id_funcao_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.funcao_id_funcao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.funcao_id_funcao_seq OWNER TO postgres;

--
-- Name: funcao_id_funcao_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.funcao_id_funcao_seq OWNED BY public.funcao.id_funcao;


--
-- Name: historico_donos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historico_donos (
    id_historico integer NOT NULL,
    id_pessoa integer NOT NULL,
    id_veiculo integer NOT NULL,
    data_inicio date NOT NULL,
    data_fim date,
    atual boolean DEFAULT true NOT NULL
);


ALTER TABLE public.historico_donos OWNER TO postgres;

--
-- Name: historico_donos_id_historico_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.historico_donos_id_historico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.historico_donos_id_historico_seq OWNER TO postgres;

--
-- Name: historico_donos_id_historico_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historico_donos_id_historico_seq OWNED BY public.historico_donos.id_historico;


--
-- Name: marca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.marca (
    id_marca integer NOT NULL,
    nome character varying(60) NOT NULL
);


ALTER TABLE public.marca OWNER TO postgres;

--
-- Name: marca_id_marca_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.marca_id_marca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.marca_id_marca_seq OWNER TO postgres;

--
-- Name: marca_id_marca_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.marca_id_marca_seq OWNED BY public.marca.id_marca;


--
-- Name: modelo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.modelo (
    id_modelo integer NOT NULL,
    nome character varying(60) NOT NULL,
    id_marca integer NOT NULL
);


ALTER TABLE public.modelo OWNER TO postgres;

--
-- Name: modelo_id_modelo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.modelo_id_modelo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.modelo_id_modelo_seq OWNER TO postgres;

--
-- Name: modelo_id_modelo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.modelo_id_modelo_seq OWNED BY public.modelo.id_modelo;


--
-- Name: ordem_servico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ordem_servico (
    id_os integer NOT NULL,
    numero_os integer NOT NULL,
    data_entrada date NOT NULL,
    data_saida date,
    status_os public.status_os_enum NOT NULL,
    prioridade public.prioridade_enum DEFAULT 'NORMAL'::public.prioridade_enum NOT NULL,
    valor_mao_obra numeric(10,2),
    valor_pecas numeric(10,2),
    deslocamento numeric(10,2),
    servico_guincho numeric(10,2),
    outros numeric(10,2),
    total numeric(10,2),
    id_pessoa integer NOT NULL,
    id_veiculo integer NOT NULL,
    CONSTRAINT ordem_servico_deslocamento_check CHECK ((deslocamento >= (0)::numeric)),
    CONSTRAINT ordem_servico_servico_guincho_check CHECK ((servico_guincho >= (0)::numeric)),
    CONSTRAINT ordem_servico_total_check CHECK ((total >= (0)::numeric)),
    CONSTRAINT ordem_servico_valor_mao_obra_check CHECK ((valor_mao_obra >= (0)::numeric)),
    CONSTRAINT ordem_servico_valor_pecas_check CHECK ((valor_pecas >= (0)::numeric))
);


ALTER TABLE public.ordem_servico OWNER TO postgres;

--
-- Name: ordem_servico_id_os_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ordem_servico_id_os_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ordem_servico_id_os_seq OWNER TO postgres;

--
-- Name: ordem_servico_id_os_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ordem_servico_id_os_seq OWNED BY public.ordem_servico.id_os;


--
-- Name: os_peca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.os_peca (
    id_os_peca integer NOT NULL,
    id_os integer NOT NULL,
    id_peca integer NOT NULL,
    id_fornecedor integer NOT NULL,
    qtd integer NOT NULL,
    valor_unitario numeric(10,2) NOT NULL,
    valor_total numeric(10,2) NOT NULL,
    status_peca public.status_peca_enum NOT NULL,
    CONSTRAINT os_peca_qtd_check CHECK ((qtd > 0)),
    CONSTRAINT os_peca_valor_total_check CHECK ((valor_total >= (0)::numeric)),
    CONSTRAINT os_peca_valor_unitario_check CHECK ((valor_unitario >= (0)::numeric))
);


ALTER TABLE public.os_peca OWNER TO postgres;

--
-- Name: os_peca_id_os_peca_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.os_peca_id_os_peca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.os_peca_id_os_peca_seq OWNER TO postgres;

--
-- Name: os_peca_id_os_peca_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.os_peca_id_os_peca_seq OWNED BY public.os_peca.id_os_peca;


--
-- Name: os_servico_externo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.os_servico_externo (
    id_os_servico_externo integer NOT NULL,
    id_os integer NOT NULL,
    id_servico_externo integer NOT NULL,
    id_fornecedor integer NOT NULL,
    valor numeric(10,2) NOT NULL,
    CONSTRAINT os_servico_externo_valor_check CHECK ((valor >= (0)::numeric))
);


ALTER TABLE public.os_servico_externo OWNER TO postgres;

--
-- Name: os_servico_externo_id_os_servico_externo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.os_servico_externo_id_os_servico_externo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.os_servico_externo_id_os_servico_externo_seq OWNER TO postgres;

--
-- Name: os_servico_externo_id_os_servico_externo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.os_servico_externo_id_os_servico_externo_seq OWNED BY public.os_servico_externo.id_os_servico_externo;


--
-- Name: os_servico_interno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.os_servico_interno (
    id_os_servico_interno integer NOT NULL,
    id_os integer NOT NULL,
    id_servico_interno integer NOT NULL,
    id_pessoa integer NOT NULL,
    qtd integer NOT NULL,
    valor numeric(10,2) NOT NULL,
    inicio time without time zone,
    fim time without time zone,
    CONSTRAINT os_servico_interno_qtd_check CHECK ((qtd > 0)),
    CONSTRAINT os_servico_interno_valor_check CHECK ((valor >= (0)::numeric))
);


ALTER TABLE public.os_servico_interno OWNER TO postgres;

--
-- Name: os_servico_interno_id_os_servico_interno_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.os_servico_interno_id_os_servico_interno_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.os_servico_interno_id_os_servico_interno_seq OWNER TO postgres;

--
-- Name: os_servico_interno_id_os_servico_interno_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.os_servico_interno_id_os_servico_interno_seq OWNED BY public.os_servico_interno.id_os_servico_interno;


--
-- Name: peca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.peca (
    id_peca integer NOT NULL,
    codigo_nacional character varying(30) NOT NULL,
    nome character varying(100) NOT NULL,
    valor numeric(10,2) NOT NULL,
    garantia integer,
    CONSTRAINT peca_garantia_check CHECK ((garantia >= 0)),
    CONSTRAINT peca_valor_check CHECK ((valor >= (0)::numeric))
);


ALTER TABLE public.peca OWNER TO postgres;

--
-- Name: peca_id_peca_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.peca_id_peca_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.peca_id_peca_seq OWNER TO postgres;

--
-- Name: peca_id_peca_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.peca_id_peca_seq OWNED BY public.peca.id_peca;


--
-- Name: pessoa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa (
    id_pessoa integer NOT NULL,
    nome character varying(100) NOT NULL,
    email character varying(100),
    cep character varying(9) NOT NULL,
    logradouro character varying(100) NOT NULL,
    numero character varying(10),
    complemento character varying(50)
);


ALTER TABLE public.pessoa OWNER TO postgres;

--
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_fisica (
    id_pessoa integer NOT NULL,
    cpf character varying(14) NOT NULL,
    rg character varying(12)
);


ALTER TABLE public.pessoa_fisica OWNER TO postgres;

--
-- Name: pessoa_id_pessoa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pessoa_id_pessoa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_id_pessoa_seq OWNER TO postgres;

--
-- Name: pessoa_id_pessoa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pessoa_id_pessoa_seq OWNED BY public.pessoa.id_pessoa;


--
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_juridica (
    id_pessoa integer NOT NULL,
    cnpj character varying(18) NOT NULL,
    inscricao_estadual character varying(20),
    nome_fantasia character varying(100),
    nome_contato character varying(100)
);


ALTER TABLE public.pessoa_juridica OWNER TO postgres;

--
-- Name: servico_externo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.servico_externo (
    id_servico_externo integer NOT NULL,
    descricao character varying(255) NOT NULL,
    valor_base numeric(10,2) NOT NULL,
    garantia integer,
    CONSTRAINT servico_externo_garantia_check CHECK ((garantia >= 0)),
    CONSTRAINT servico_externo_valor_base_check CHECK ((valor_base >= (0)::numeric))
);


ALTER TABLE public.servico_externo OWNER TO postgres;

--
-- Name: servico_externo_id_servico_externo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.servico_externo_id_servico_externo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.servico_externo_id_servico_externo_seq OWNER TO postgres;

--
-- Name: servico_externo_id_servico_externo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.servico_externo_id_servico_externo_seq OWNED BY public.servico_externo.id_servico_externo;


--
-- Name: servico_interno; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.servico_interno (
    id_servico_interno integer NOT NULL,
    descricao character varying(255) NOT NULL,
    valor_base numeric(10,2) NOT NULL,
    garantia integer,
    CONSTRAINT servico_interno_garantia_check CHECK ((garantia >= 0)),
    CONSTRAINT servico_interno_valor_base_check CHECK ((valor_base >= (0)::numeric))
);


ALTER TABLE public.servico_interno OWNER TO postgres;

--
-- Name: servico_interno_id_servico_interno_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.servico_interno_id_servico_interno_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.servico_interno_id_servico_interno_seq OWNER TO postgres;

--
-- Name: servico_interno_id_servico_interno_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.servico_interno_id_servico_interno_seq OWNED BY public.servico_interno.id_servico_interno;


--
-- Name: telefone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telefone (
    id_pessoa integer NOT NULL,
    numero character varying(15) NOT NULL
);


ALTER TABLE public.telefone OWNER TO postgres;

--
-- Name: veiculo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.veiculo (
    id_veiculo integer NOT NULL,
    placa character varying(8) NOT NULL,
    chassi character varying(17),
    cor character varying(30),
    ano_fabricacao integer NOT NULL,
    quilometragem integer,
    id_modelo integer NOT NULL,
    CONSTRAINT veiculo_ano_fabricacao_check CHECK (((ano_fabricacao >= 1900) AND ((ano_fabricacao)::numeric <= (EXTRACT(year FROM CURRENT_DATE) + (1)::numeric)))),
    CONSTRAINT veiculo_quilometragem_check CHECK ((quilometragem >= 0))
);


ALTER TABLE public.veiculo OWNER TO postgres;

--
-- Name: veiculo_id_veiculo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.veiculo_id_veiculo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.veiculo_id_veiculo_seq OWNER TO postgres;

--
-- Name: veiculo_id_veiculo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.veiculo_id_veiculo_seq OWNED BY public.veiculo.id_veiculo;


--
-- Name: fornecedor id_fornecedor; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor ALTER COLUMN id_fornecedor SET DEFAULT nextval('public.fornecedor_id_fornecedor_seq'::regclass);


--
-- Name: funcao id_funcao; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcao ALTER COLUMN id_funcao SET DEFAULT nextval('public.funcao_id_funcao_seq'::regclass);


--
-- Name: historico_donos id_historico; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historico_donos ALTER COLUMN id_historico SET DEFAULT nextval('public.historico_donos_id_historico_seq'::regclass);


--
-- Name: marca id_marca; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marca ALTER COLUMN id_marca SET DEFAULT nextval('public.marca_id_marca_seq'::regclass);


--
-- Name: modelo id_modelo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modelo ALTER COLUMN id_modelo SET DEFAULT nextval('public.modelo_id_modelo_seq'::regclass);


--
-- Name: ordem_servico id_os; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordem_servico ALTER COLUMN id_os SET DEFAULT nextval('public.ordem_servico_id_os_seq'::regclass);


--
-- Name: os_peca id_os_peca; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_peca ALTER COLUMN id_os_peca SET DEFAULT nextval('public.os_peca_id_os_peca_seq'::regclass);


--
-- Name: os_servico_externo id_os_servico_externo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_externo ALTER COLUMN id_os_servico_externo SET DEFAULT nextval('public.os_servico_externo_id_os_servico_externo_seq'::regclass);


--
-- Name: os_servico_interno id_os_servico_interno; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_interno ALTER COLUMN id_os_servico_interno SET DEFAULT nextval('public.os_servico_interno_id_os_servico_interno_seq'::regclass);


--
-- Name: peca id_peca; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peca ALTER COLUMN id_peca SET DEFAULT nextval('public.peca_id_peca_seq'::regclass);


--
-- Name: pessoa id_pessoa; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa ALTER COLUMN id_pessoa SET DEFAULT nextval('public.pessoa_id_pessoa_seq'::regclass);


--
-- Name: servico_externo id_servico_externo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servico_externo ALTER COLUMN id_servico_externo SET DEFAULT nextval('public.servico_externo_id_servico_externo_seq'::regclass);


--
-- Name: servico_interno id_servico_interno; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servico_interno ALTER COLUMN id_servico_interno SET DEFAULT nextval('public.servico_interno_id_servico_interno_seq'::regclass);


--
-- Name: veiculo id_veiculo; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.veiculo ALTER COLUMN id_veiculo SET DEFAULT nextval('public.veiculo_id_veiculo_seq'::regclass);


--
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_pessoa);


--
-- Name: colaborador colaborador_matricula_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colaborador
    ADD CONSTRAINT colaborador_matricula_key UNIQUE (matricula);


--
-- Name: colaborador colaborador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colaborador
    ADD CONSTRAINT colaborador_pkey PRIMARY KEY (id_pessoa);


--
-- Name: fornecedor fornecedor_cnpj_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor
    ADD CONSTRAINT fornecedor_cnpj_key UNIQUE (cnpj);


--
-- Name: fornecedor fornecedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fornecedor
    ADD CONSTRAINT fornecedor_pkey PRIMARY KEY (id_fornecedor);


--
-- Name: funcao funcao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.funcao
    ADD CONSTRAINT funcao_pkey PRIMARY KEY (id_funcao);


--
-- Name: historico_donos historico_donos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historico_donos
    ADD CONSTRAINT historico_donos_pkey PRIMARY KEY (id_historico);


--
-- Name: marca marca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marca
    ADD CONSTRAINT marca_pkey PRIMARY KEY (id_marca);


--
-- Name: modelo modelo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modelo
    ADD CONSTRAINT modelo_pkey PRIMARY KEY (id_modelo);


--
-- Name: ordem_servico ordem_servico_numero_os_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordem_servico
    ADD CONSTRAINT ordem_servico_numero_os_key UNIQUE (numero_os);


--
-- Name: ordem_servico ordem_servico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordem_servico
    ADD CONSTRAINT ordem_servico_pkey PRIMARY KEY (id_os);


--
-- Name: os_peca os_peca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_peca
    ADD CONSTRAINT os_peca_pkey PRIMARY KEY (id_os_peca);


--
-- Name: os_servico_externo os_servico_externo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_externo
    ADD CONSTRAINT os_servico_externo_pkey PRIMARY KEY (id_os_servico_externo);


--
-- Name: os_servico_interno os_servico_interno_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_interno
    ADD CONSTRAINT os_servico_interno_pkey PRIMARY KEY (id_os_servico_interno);


--
-- Name: peca peca_codigo_nacional_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peca
    ADD CONSTRAINT peca_codigo_nacional_key UNIQUE (codigo_nacional);


--
-- Name: peca peca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peca
    ADD CONSTRAINT peca_pkey PRIMARY KEY (id_peca);


--
-- Name: pessoa_fisica pessoa_fisica_cpf_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_cpf_key UNIQUE (cpf);


--
-- Name: pessoa_fisica pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id_pessoa);


--
-- Name: pessoa_juridica pessoa_juridica_cnpj_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_cnpj_key UNIQUE (cnpj);


--
-- Name: pessoa_juridica pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id_pessoa);


--
-- Name: pessoa pessoa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT pessoa_pkey PRIMARY KEY (id_pessoa);


--
-- Name: telefone pk_telefone; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telefone
    ADD CONSTRAINT pk_telefone PRIMARY KEY (id_pessoa, numero);


--
-- Name: servico_externo servico_externo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servico_externo
    ADD CONSTRAINT servico_externo_pkey PRIMARY KEY (id_servico_externo);


--
-- Name: servico_interno servico_interno_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.servico_interno
    ADD CONSTRAINT servico_interno_pkey PRIMARY KEY (id_servico_interno);


--
-- Name: veiculo veiculo_chassi_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.veiculo
    ADD CONSTRAINT veiculo_chassi_key UNIQUE (chassi);


--
-- Name: veiculo veiculo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.veiculo
    ADD CONSTRAINT veiculo_pkey PRIMARY KEY (id_veiculo);


--
-- Name: veiculo veiculo_placa_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.veiculo
    ADD CONSTRAINT veiculo_placa_key UNIQUE (placa);


--
-- Name: cliente fk_cliente_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT fk_cliente_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);


--
-- Name: colaborador fk_colab_funcao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colaborador
    ADD CONSTRAINT fk_colab_funcao FOREIGN KEY (id_funcao) REFERENCES public.funcao(id_funcao);


--
-- Name: colaborador fk_colab_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.colaborador
    ADD CONSTRAINT fk_colab_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);


--
-- Name: historico_donos fk_hist_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historico_donos
    ADD CONSTRAINT fk_hist_cliente FOREIGN KEY (id_pessoa) REFERENCES public.cliente(id_pessoa);


--
-- Name: historico_donos fk_hist_veiculo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historico_donos
    ADD CONSTRAINT fk_hist_veiculo FOREIGN KEY (id_veiculo) REFERENCES public.veiculo(id_veiculo);


--
-- Name: modelo fk_modelo_marca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.modelo
    ADD CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca) REFERENCES public.marca(id_marca);


--
-- Name: ordem_servico fk_os_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordem_servico
    ADD CONSTRAINT fk_os_cliente FOREIGN KEY (id_pessoa) REFERENCES public.cliente(id_pessoa);


--
-- Name: ordem_servico fk_os_veiculo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ordem_servico
    ADD CONSTRAINT fk_os_veiculo FOREIGN KEY (id_veiculo) REFERENCES public.veiculo(id_veiculo);


--
-- Name: os_servico_externo fk_ose_fornecedor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_externo
    ADD CONSTRAINT fk_ose_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES public.fornecedor(id_fornecedor);


--
-- Name: os_servico_externo fk_ose_os; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_externo
    ADD CONSTRAINT fk_ose_os FOREIGN KEY (id_os) REFERENCES public.ordem_servico(id_os);


--
-- Name: os_servico_externo fk_ose_servico; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_externo
    ADD CONSTRAINT fk_ose_servico FOREIGN KEY (id_servico_externo) REFERENCES public.servico_externo(id_servico_externo);


--
-- Name: os_servico_interno fk_osi_colab; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_interno
    ADD CONSTRAINT fk_osi_colab FOREIGN KEY (id_pessoa) REFERENCES public.colaborador(id_pessoa);


--
-- Name: os_servico_interno fk_osi_os; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_interno
    ADD CONSTRAINT fk_osi_os FOREIGN KEY (id_os) REFERENCES public.ordem_servico(id_os);


--
-- Name: os_servico_interno fk_osi_servico; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_servico_interno
    ADD CONSTRAINT fk_osi_servico FOREIGN KEY (id_servico_interno) REFERENCES public.servico_interno(id_servico_interno);


--
-- Name: os_peca fk_osp_fornecedor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_peca
    ADD CONSTRAINT fk_osp_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES public.fornecedor(id_fornecedor);


--
-- Name: os_peca fk_osp_os; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_peca
    ADD CONSTRAINT fk_osp_os FOREIGN KEY (id_os) REFERENCES public.ordem_servico(id_os);


--
-- Name: os_peca fk_osp_peca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.os_peca
    ADD CONSTRAINT fk_osp_peca FOREIGN KEY (id_peca) REFERENCES public.peca(id_peca);


--
-- Name: pessoa_fisica fk_pf_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT fk_pf_cliente FOREIGN KEY (id_pessoa) REFERENCES public.cliente(id_pessoa);


--
-- Name: pessoa_juridica fk_pj_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT fk_pj_cliente FOREIGN KEY (id_pessoa) REFERENCES public.cliente(id_pessoa);


--
-- Name: telefone fk_telefone_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telefone
    ADD CONSTRAINT fk_telefone_pessoa FOREIGN KEY (id_pessoa) REFERENCES public.pessoa(id_pessoa);


--
-- Name: veiculo fk_veiculo_modelo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.veiculo
    ADD CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES public.modelo(id_modelo);


--
-- PostgreSQL database dump complete
--

\unrestrict SOdA8G0TwfYnnowuT4suCh4vFFLfldPPTKqbplvJVlT9a1lEFstn6Z1amXZ6cQW

