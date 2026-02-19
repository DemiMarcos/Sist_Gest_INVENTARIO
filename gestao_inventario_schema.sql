--
-- PostgreSQL database dump
--

\restrict Sxnsso7BQAdLSvpElADgv6eys1MrwCLabRa9eDjgm1Qw6e8BqfqYrM1tJasdJET

-- Dumped from database version 16.11 (Ubuntu 16.11-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.11 (Ubuntu 16.11-0ubuntu0.24.04.1)

-- Started on 2026-02-11 21:46:41 WAT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 246 (class 1259 OID 58120)
-- Name: armazem; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.armazem (
    pk_armazem integer NOT NULL,
    nome character varying(100) NOT NULL,
    activo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.armazem OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 58119)
-- Name: armazem_pk_armazem_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.armazem_pk_armazem_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.armazem_pk_armazem_seq OWNER TO postgres;

--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 245
-- Name: armazem_pk_armazem_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.armazem_pk_armazem_seq OWNED BY public.armazem.pk_armazem;


--
-- TOC entry 234 (class 1259 OID 58023)
-- Name: email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email (
    pk_email integer NOT NULL,
    email character varying(120) NOT NULL
);


ALTER TABLE public.email OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 58022)
-- Name: email_pk_email_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.email_pk_email_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.email_pk_email_seq OWNER TO postgres;

--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 233
-- Name: email_pk_email_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.email_pk_email_seq OWNED BY public.email.pk_email;


--
-- TOC entry 218 (class 1259 OID 49693)
-- Name: encomenda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.encomenda (
    pk_encomenda integer NOT NULL,
    data_encomenda timestamp without time zone NOT NULL,
    estado character varying(30) NOT NULL,
    fk_fornecedor integer NOT NULL
);


ALTER TABLE public.encomenda OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 49700)
-- Name: encomenda_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.encomenda_item (
    pk_item integer NOT NULL,
    fk_encomenda integer NOT NULL,
    fk_portfolio character varying(50) NOT NULL,
    quantidade integer NOT NULL
);


ALTER TABLE public.encomenda_item OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 49699)
-- Name: encomenda_item_pk_item_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.encomenda_item_pk_item_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.encomenda_item_pk_item_seq OWNER TO postgres;

--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 219
-- Name: encomenda_item_pk_item_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.encomenda_item_pk_item_seq OWNED BY public.encomenda_item.pk_item;


--
-- TOC entry 217 (class 1259 OID 49692)
-- Name: encomenda_pk_encomenda_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.encomenda_pk_encomenda_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.encomenda_pk_encomenda_seq OWNER TO postgres;

--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 217
-- Name: encomenda_pk_encomenda_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.encomenda_pk_encomenda_seq OWNED BY public.encomenda.pk_encomenda;


--
-- TOC entry 248 (class 1259 OID 58130)
-- Name: localizacao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.localizacao (
    pk_localizacao integer NOT NULL,
    fk_armazem integer NOT NULL,
    codigo character varying(50) NOT NULL,
    descricao character varying(255),
    activo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.localizacao OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 58129)
-- Name: localizacao_pk_localizacao_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.localizacao_pk_localizacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.localizacao_pk_localizacao_seq OWNER TO postgres;

--
-- TOC entry 3603 (class 0 OID 0)
-- Dependencies: 247
-- Name: localizacao_pk_localizacao_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.localizacao_pk_localizacao_seq OWNED BY public.localizacao.pk_localizacao;


--
-- TOC entry 222 (class 1259 OID 49730)
-- Name: movimentacao_inventario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movimentacao_inventario (
    pk_movimentacao integer NOT NULL,
    fk_portfolio character varying(50) NOT NULL,
    tipo_movimento character varying(1) NOT NULL,
    quantidade integer NOT NULL,
    data_movimento timestamp without time zone NOT NULL,
    fk_armazem integer NOT NULL,
    fk_localizacao integer,
    CONSTRAINT movimentacao_inventario_tipo_movimento_check CHECK (((tipo_movimento)::bpchar = ANY (ARRAY['E'::bpchar, 'S'::bpchar])))
);


ALTER TABLE public.movimentacao_inventario OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 49729)
-- Name: movimentacao_inventario_pk_movimentacao_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.movimentacao_inventario_pk_movimentacao_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.movimentacao_inventario_pk_movimentacao_seq OWNER TO postgres;

--
-- TOC entry 3604 (class 0 OID 0)
-- Dependencies: 221
-- Name: movimentacao_inventario_pk_movimentacao_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.movimentacao_inventario_pk_movimentacao_seq OWNED BY public.movimentacao_inventario.pk_movimentacao;


--
-- TOC entry 229 (class 1259 OID 57991)
-- Name: pessoa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa (
    pk_pessoa integer NOT NULL,
    nome character varying(120) NOT NULL,
    activo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.pessoa OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 58041)
-- Name: pessoa_email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_email (
    pk_pessoa_email integer NOT NULL,
    fk_pessoa integer NOT NULL,
    fk_email integer NOT NULL
);


ALTER TABLE public.pessoa_email OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 58040)
-- Name: pessoa_email_pk_pessoa_email_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pessoa_email_pk_pessoa_email_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_email_pk_pessoa_email_seq OWNER TO postgres;

--
-- TOC entry 3605 (class 0 OID 0)
-- Dependencies: 237
-- Name: pessoa_email_pk_pessoa_email_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pessoa_email_pk_pessoa_email_seq OWNED BY public.pessoa_email.pk_pessoa_email;


--
-- TOC entry 228 (class 1259 OID 57990)
-- Name: pessoa_pk_pessoa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pessoa_pk_pessoa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_pk_pessoa_seq OWNER TO postgres;

--
-- TOC entry 3606 (class 0 OID 0)
-- Dependencies: 228
-- Name: pessoa_pk_pessoa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pessoa_pk_pessoa_seq OWNED BY public.pessoa.pk_pessoa;


--
-- TOC entry 240 (class 1259 OID 58060)
-- Name: pessoa_telefone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_telefone (
    pk_pessoa_telefone integer NOT NULL,
    fk_pessoa integer NOT NULL,
    fk_telefone integer NOT NULL
);


ALTER TABLE public.pessoa_telefone OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 58059)
-- Name: pessoa_telefone_pk_pessoa_telefone_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pessoa_telefone_pk_pessoa_telefone_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_telefone_pk_pessoa_telefone_seq OWNER TO postgres;

--
-- TOC entry 3607 (class 0 OID 0)
-- Dependencies: 239
-- Name: pessoa_telefone_pk_pessoa_telefone_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pessoa_telefone_pk_pessoa_telefone_seq OWNED BY public.pessoa_telefone.pk_pessoa_telefone;


--
-- TOC entry 230 (class 1259 OID 57998)
-- Name: pessoa_tipo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_tipo (
    pk_tipo character varying(20) NOT NULL
);


ALTER TABLE public.pessoa_tipo OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 58004)
-- Name: pessoa_tipo_map; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pessoa_tipo_map (
    pk_map integer NOT NULL,
    fk_pessoa integer NOT NULL,
    fk_tipo character varying(20) NOT NULL
);


ALTER TABLE public.pessoa_tipo_map OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 58003)
-- Name: pessoa_tipo_map_pk_map_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pessoa_tipo_map_pk_map_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_tipo_map_pk_map_seq OWNER TO postgres;

--
-- TOC entry 3608 (class 0 OID 0)
-- Dependencies: 231
-- Name: pessoa_tipo_map_pk_map_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pessoa_tipo_map_pk_map_seq OWNED BY public.pessoa_tipo_map.pk_map;


--
-- TOC entry 215 (class 1259 OID 49629)
-- Name: portfolio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.portfolio (
    pk_portfolio character varying(50) NOT NULL,
    descricao character varying(255) NOT NULL,
    fk_portfolio_pai character varying(50)
);


ALTER TABLE public.portfolio OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 49756)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto (
    fk_portfolio character varying(50) NOT NULL,
    quantidade_critica integer NOT NULL,
    quantidade_maxima integer NOT NULL,
    quantidade_exata integer DEFAULT 0 NOT NULL,
    CONSTRAINT ck_produto_max_maior_ou_igual_critica CHECK ((quantidade_maxima >= quantidade_critica)),
    CONSTRAINT ck_produto_quantidade_critica CHECK ((quantidade_critica >= 0)),
    CONSTRAINT ck_produto_quantidade_maxima CHECK ((quantidade_maxima >= 0))
);


ALTER TABLE public.produto OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 49784)
-- Name: produto_preco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto_preco (
    pk_preco integer NOT NULL,
    fk_portfolio character varying(50) NOT NULL,
    preco double precision NOT NULL,
    data_inicio timestamp without time zone DEFAULT now() NOT NULL,
    data_fim timestamp without time zone,
    CONSTRAINT ck_preco_positivo CHECK ((preco > (0)::double precision))
);


ALTER TABLE public.produto_preco OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 49783)
-- Name: produto_preco_pk_preco_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.produto_preco_pk_preco_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.produto_preco_pk_preco_seq OWNER TO postgres;

--
-- TOC entry 3609 (class 0 OID 0)
-- Dependencies: 226
-- Name: produto_preco_pk_preco_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.produto_preco_pk_preco_seq OWNED BY public.produto_preco.pk_preco;


--
-- TOC entry 249 (class 1259 OID 58155)
-- Name: produto_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produto_stock (
    fk_portfolio character varying(50) NOT NULL,
    fk_armazem integer NOT NULL,
    quantidade_exata integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.produto_stock OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 58032)
-- Name: telefone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telefone (
    pk_telefone integer NOT NULL,
    numero character varying(40) NOT NULL
);


ALTER TABLE public.telefone OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 58031)
-- Name: telefone_pk_telefone_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.telefone_pk_telefone_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.telefone_pk_telefone_seq OWNER TO postgres;

--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 235
-- Name: telefone_pk_telefone_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.telefone_pk_telefone_seq OWNED BY public.telefone.pk_telefone;


--
-- TOC entry 224 (class 1259 OID 49743)
-- Name: utilizador; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilizador (
    pk_utilizador integer NOT NULL,
    username character varying(50) NOT NULL,
    activo boolean NOT NULL
);


ALTER TABLE public.utilizador OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 49742)
-- Name: utilizador_pk_utilizador_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilizador_pk_utilizador_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilizador_pk_utilizador_seq OWNER TO postgres;

--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 223
-- Name: utilizador_pk_utilizador_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilizador_pk_utilizador_seq OWNED BY public.utilizador.pk_utilizador;


--
-- TOC entry 242 (class 1259 OID 58085)
-- Name: venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda (
    pk_venda integer NOT NULL,
    data_venda timestamp without time zone DEFAULT now() NOT NULL,
    estado character varying(30) NOT NULL,
    fk_cliente integer NOT NULL
);


ALTER TABLE public.venda OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 58099)
-- Name: venda_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venda_item (
    pk_item integer NOT NULL,
    fk_venda integer NOT NULL,
    fk_portfolio character varying(50) NOT NULL,
    quantidade integer NOT NULL,
    preco_unitario double precision NOT NULL,
    CONSTRAINT ck_vi_preco CHECK ((preco_unitario > (0)::double precision)),
    CONSTRAINT ck_vi_quantidade CHECK ((quantidade > 0))
);


ALTER TABLE public.venda_item OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 58098)
-- Name: venda_item_pk_item_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venda_item_pk_item_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venda_item_pk_item_seq OWNER TO postgres;

--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 243
-- Name: venda_item_pk_item_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venda_item_pk_item_seq OWNED BY public.venda_item.pk_item;


--
-- TOC entry 241 (class 1259 OID 58084)
-- Name: venda_pk_venda_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venda_pk_venda_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venda_pk_venda_seq OWNER TO postgres;

--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 241
-- Name: venda_pk_venda_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venda_pk_venda_seq OWNED BY public.venda.pk_venda;


--
-- TOC entry 216 (class 1259 OID 49639)
-- Name: versao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.versao (
    pk_versao character varying(50) NOT NULL,
    data timestamp without time zone NOT NULL
);


ALTER TABLE public.versao OWNER TO postgres;

--
-- TOC entry 3354 (class 2604 OID 58123)
-- Name: armazem pk_armazem; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.armazem ALTER COLUMN pk_armazem SET DEFAULT nextval('public.armazem_pk_armazem_seq'::regclass);


--
-- TOC entry 3347 (class 2604 OID 58026)
-- Name: email pk_email; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email ALTER COLUMN pk_email SET DEFAULT nextval('public.email_pk_email_seq'::regclass);


--
-- TOC entry 3337 (class 2604 OID 49696)
-- Name: encomenda pk_encomenda; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda ALTER COLUMN pk_encomenda SET DEFAULT nextval('public.encomenda_pk_encomenda_seq'::regclass);


--
-- TOC entry 3338 (class 2604 OID 49703)
-- Name: encomenda_item pk_item; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda_item ALTER COLUMN pk_item SET DEFAULT nextval('public.encomenda_item_pk_item_seq'::regclass);


--
-- TOC entry 3356 (class 2604 OID 58133)
-- Name: localizacao pk_localizacao; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.localizacao ALTER COLUMN pk_localizacao SET DEFAULT nextval('public.localizacao_pk_localizacao_seq'::regclass);


--
-- TOC entry 3339 (class 2604 OID 49733)
-- Name: movimentacao_inventario pk_movimentacao; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacao_inventario ALTER COLUMN pk_movimentacao SET DEFAULT nextval('public.movimentacao_inventario_pk_movimentacao_seq'::regclass);


--
-- TOC entry 3344 (class 2604 OID 57994)
-- Name: pessoa pk_pessoa; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa ALTER COLUMN pk_pessoa SET DEFAULT nextval('public.pessoa_pk_pessoa_seq'::regclass);


--
-- TOC entry 3349 (class 2604 OID 58044)
-- Name: pessoa_email pk_pessoa_email; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_email ALTER COLUMN pk_pessoa_email SET DEFAULT nextval('public.pessoa_email_pk_pessoa_email_seq'::regclass);


--
-- TOC entry 3350 (class 2604 OID 58063)
-- Name: pessoa_telefone pk_pessoa_telefone; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_telefone ALTER COLUMN pk_pessoa_telefone SET DEFAULT nextval('public.pessoa_telefone_pk_pessoa_telefone_seq'::regclass);


--
-- TOC entry 3346 (class 2604 OID 58007)
-- Name: pessoa_tipo_map pk_map; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo_map ALTER COLUMN pk_map SET DEFAULT nextval('public.pessoa_tipo_map_pk_map_seq'::regclass);


--
-- TOC entry 3342 (class 2604 OID 49787)
-- Name: produto_preco pk_preco; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_preco ALTER COLUMN pk_preco SET DEFAULT nextval('public.produto_preco_pk_preco_seq'::regclass);


--
-- TOC entry 3348 (class 2604 OID 58035)
-- Name: telefone pk_telefone; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telefone ALTER COLUMN pk_telefone SET DEFAULT nextval('public.telefone_pk_telefone_seq'::regclass);


--
-- TOC entry 3340 (class 2604 OID 49746)
-- Name: utilizador pk_utilizador; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador ALTER COLUMN pk_utilizador SET DEFAULT nextval('public.utilizador_pk_utilizador_seq'::regclass);


--
-- TOC entry 3351 (class 2604 OID 58088)
-- Name: venda pk_venda; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda ALTER COLUMN pk_venda SET DEFAULT nextval('public.venda_pk_venda_seq'::regclass);


--
-- TOC entry 3353 (class 2604 OID 58102)
-- Name: venda_item pk_item; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_item ALTER COLUMN pk_item SET DEFAULT nextval('public.venda_item_pk_item_seq'::regclass);


--
-- TOC entry 3420 (class 2606 OID 58128)
-- Name: armazem armazem_nome_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.armazem
    ADD CONSTRAINT armazem_nome_key UNIQUE (nome);


--
-- TOC entry 3422 (class 2606 OID 58126)
-- Name: armazem armazem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.armazem
    ADD CONSTRAINT armazem_pkey PRIMARY KEY (pk_armazem);


--
-- TOC entry 3397 (class 2606 OID 58028)
-- Name: email email_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email
    ADD CONSTRAINT email_pkey PRIMARY KEY (pk_email);


--
-- TOC entry 3374 (class 2606 OID 49705)
-- Name: encomenda_item encomenda_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda_item
    ADD CONSTRAINT encomenda_item_pkey PRIMARY KEY (pk_item);


--
-- TOC entry 3371 (class 2606 OID 49698)
-- Name: encomenda encomenda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda
    ADD CONSTRAINT encomenda_pkey PRIMARY KEY (pk_encomenda);


--
-- TOC entry 3424 (class 2606 OID 58136)
-- Name: localizacao localizacao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.localizacao
    ADD CONSTRAINT localizacao_pkey PRIMARY KEY (pk_localizacao);


--
-- TOC entry 3376 (class 2606 OID 49736)
-- Name: movimentacao_inventario movimentacao_inventario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacao_inventario
    ADD CONSTRAINT movimentacao_inventario_pkey PRIMARY KEY (pk_movimentacao);


--
-- TOC entry 3405 (class 2606 OID 58046)
-- Name: pessoa_email pessoa_email_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_email
    ADD CONSTRAINT pessoa_email_pkey PRIMARY KEY (pk_pessoa_email);


--
-- TOC entry 3389 (class 2606 OID 57997)
-- Name: pessoa pessoa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT pessoa_pkey PRIMARY KEY (pk_pessoa);


--
-- TOC entry 3409 (class 2606 OID 58065)
-- Name: pessoa_telefone pessoa_telefone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_telefone
    ADD CONSTRAINT pessoa_telefone_pkey PRIMARY KEY (pk_pessoa_telefone);


--
-- TOC entry 3393 (class 2606 OID 58009)
-- Name: pessoa_tipo_map pessoa_tipo_map_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo_map
    ADD CONSTRAINT pessoa_tipo_map_pkey PRIMARY KEY (pk_map);


--
-- TOC entry 3391 (class 2606 OID 58002)
-- Name: pessoa_tipo pessoa_tipo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo
    ADD CONSTRAINT pessoa_tipo_pkey PRIMARY KEY (pk_tipo);


--
-- TOC entry 3429 (class 2606 OID 58160)
-- Name: produto_stock pk_produto_stock; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_stock
    ADD CONSTRAINT pk_produto_stock PRIMARY KEY (fk_portfolio, fk_armazem);


--
-- TOC entry 3367 (class 2606 OID 49633)
-- Name: portfolio portfolio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.portfolio
    ADD CONSTRAINT portfolio_pkey PRIMARY KEY (pk_portfolio);


--
-- TOC entry 3384 (class 2606 OID 49763)
-- Name: produto produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (fk_portfolio);


--
-- TOC entry 3386 (class 2606 OID 49791)
-- Name: produto_preco produto_preco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_preco
    ADD CONSTRAINT produto_preco_pkey PRIMARY KEY (pk_preco);


--
-- TOC entry 3401 (class 2606 OID 58037)
-- Name: telefone telefone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telefone
    ADD CONSTRAINT telefone_pkey PRIMARY KEY (pk_telefone);


--
-- TOC entry 3426 (class 2606 OID 58138)
-- Name: localizacao uk_localizacao; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.localizacao
    ADD CONSTRAINT uk_localizacao UNIQUE (fk_armazem, codigo);


--
-- TOC entry 3378 (class 2606 OID 49748)
-- Name: utilizador utilizador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador
    ADD CONSTRAINT utilizador_pkey PRIMARY KEY (pk_utilizador);


--
-- TOC entry 3380 (class 2606 OID 49750)
-- Name: utilizador utilizador_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizador
    ADD CONSTRAINT utilizador_username_key UNIQUE (username);


--
-- TOC entry 3399 (class 2606 OID 58030)
-- Name: email ux_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email
    ADD CONSTRAINT ux_email UNIQUE (email);


--
-- TOC entry 3407 (class 2606 OID 58048)
-- Name: pessoa_email ux_pe; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_email
    ADD CONSTRAINT ux_pe UNIQUE (fk_pessoa, fk_email);


--
-- TOC entry 3411 (class 2606 OID 58067)
-- Name: pessoa_telefone ux_pt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_telefone
    ADD CONSTRAINT ux_pt UNIQUE (fk_pessoa, fk_telefone);


--
-- TOC entry 3395 (class 2606 OID 58011)
-- Name: pessoa_tipo_map ux_ptm; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo_map
    ADD CONSTRAINT ux_ptm UNIQUE (fk_pessoa, fk_tipo);


--
-- TOC entry 3403 (class 2606 OID 58039)
-- Name: telefone ux_telefone; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telefone
    ADD CONSTRAINT ux_telefone UNIQUE (numero);


--
-- TOC entry 3418 (class 2606 OID 58106)
-- Name: venda_item venda_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_item
    ADD CONSTRAINT venda_item_pkey PRIMARY KEY (pk_item);


--
-- TOC entry 3414 (class 2606 OID 58091)
-- Name: venda venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT venda_pkey PRIMARY KEY (pk_venda);


--
-- TOC entry 3369 (class 2606 OID 49643)
-- Name: versao versao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.versao
    ADD CONSTRAINT versao_pkey PRIMARY KEY (pk_versao);


--
-- TOC entry 3381 (class 1259 OID 49769)
-- Name: idx_produto_quantidade_critica; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_produto_quantidade_critica ON public.produto USING btree (quantidade_critica);


--
-- TOC entry 3382 (class 1259 OID 49770)
-- Name: idx_produto_quantidade_maxima; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_produto_quantidade_maxima ON public.produto USING btree (quantidade_maxima);


--
-- TOC entry 3372 (class 1259 OID 58078)
-- Name: ix_encomenda_fornecedor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ix_encomenda_fornecedor ON public.encomenda USING btree (fk_fornecedor);


--
-- TOC entry 3427 (class 1259 OID 58171)
-- Name: ix_produto_stock_armazem; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ix_produto_stock_armazem ON public.produto_stock USING btree (fk_armazem);


--
-- TOC entry 3412 (class 1259 OID 58097)
-- Name: ix_venda_cliente; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ix_venda_cliente ON public.venda USING btree (fk_cliente);


--
-- TOC entry 3415 (class 1259 OID 58118)
-- Name: ix_venda_item_produto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ix_venda_item_produto ON public.venda_item USING btree (fk_portfolio);


--
-- TOC entry 3416 (class 1259 OID 58117)
-- Name: ix_venda_item_venda; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ix_venda_item_venda ON public.venda_item USING btree (fk_venda);


--
-- TOC entry 3387 (class 1259 OID 49798)
-- Name: ux_produto_preco_atual; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX ux_produto_preco_atual ON public.produto_preco USING btree (fk_portfolio) WHERE (data_fim IS NULL);


--
-- TOC entry 3431 (class 2606 OID 58079)
-- Name: encomenda fk_encomenda_fornecedor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda
    ADD CONSTRAINT fk_encomenda_fornecedor FOREIGN KEY (fk_fornecedor) REFERENCES public.pessoa(pk_pessoa);


--
-- TOC entry 3432 (class 2606 OID 49706)
-- Name: encomenda_item fk_item_encomenda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda_item
    ADD CONSTRAINT fk_item_encomenda FOREIGN KEY (fk_encomenda) REFERENCES public.encomenda(pk_encomenda);


--
-- TOC entry 3433 (class 2606 OID 49711)
-- Name: encomenda_item fk_item_portfolio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.encomenda_item
    ADD CONSTRAINT fk_item_portfolio FOREIGN KEY (fk_portfolio) REFERENCES public.portfolio(pk_portfolio);


--
-- TOC entry 3434 (class 2606 OID 58144)
-- Name: movimentacao_inventario fk_mov_armazem; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacao_inventario
    ADD CONSTRAINT fk_mov_armazem FOREIGN KEY (fk_armazem) REFERENCES public.armazem(pk_armazem);


--
-- TOC entry 3435 (class 2606 OID 58149)
-- Name: movimentacao_inventario fk_mov_localizacao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacao_inventario
    ADD CONSTRAINT fk_mov_localizacao FOREIGN KEY (fk_localizacao) REFERENCES public.localizacao(pk_localizacao);


--
-- TOC entry 3436 (class 2606 OID 49737)
-- Name: movimentacao_inventario fk_mov_portfolio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movimentacao_inventario
    ADD CONSTRAINT fk_mov_portfolio FOREIGN KEY (fk_portfolio) REFERENCES public.portfolio(pk_portfolio);


--
-- TOC entry 3441 (class 2606 OID 58054)
-- Name: pessoa_email fk_pe_email; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_email
    ADD CONSTRAINT fk_pe_email FOREIGN KEY (fk_email) REFERENCES public.email(pk_email);


--
-- TOC entry 3442 (class 2606 OID 58049)
-- Name: pessoa_email fk_pe_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_email
    ADD CONSTRAINT fk_pe_pessoa FOREIGN KEY (fk_pessoa) REFERENCES public.pessoa(pk_pessoa);


--
-- TOC entry 3437 (class 2606 OID 49764)
-- Name: produto fk_produto_portfolio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT fk_produto_portfolio FOREIGN KEY (fk_portfolio) REFERENCES public.portfolio(pk_portfolio);


--
-- TOC entry 3438 (class 2606 OID 49792)
-- Name: produto_preco fk_produto_preco_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_preco
    ADD CONSTRAINT fk_produto_preco_produto FOREIGN KEY (fk_portfolio) REFERENCES public.produto(fk_portfolio) ON DELETE CASCADE;


--
-- TOC entry 3449 (class 2606 OID 58166)
-- Name: produto_stock fk_produto_stock_armazem; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_stock
    ADD CONSTRAINT fk_produto_stock_armazem FOREIGN KEY (fk_armazem) REFERENCES public.armazem(pk_armazem);


--
-- TOC entry 3450 (class 2606 OID 58161)
-- Name: produto_stock fk_produto_stock_portfolio; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produto_stock
    ADD CONSTRAINT fk_produto_stock_portfolio FOREIGN KEY (fk_portfolio) REFERENCES public.portfolio(pk_portfolio);


--
-- TOC entry 3443 (class 2606 OID 58068)
-- Name: pessoa_telefone fk_pt_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_telefone
    ADD CONSTRAINT fk_pt_pessoa FOREIGN KEY (fk_pessoa) REFERENCES public.pessoa(pk_pessoa);


--
-- TOC entry 3444 (class 2606 OID 58073)
-- Name: pessoa_telefone fk_pt_telefone; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_telefone
    ADD CONSTRAINT fk_pt_telefone FOREIGN KEY (fk_telefone) REFERENCES public.telefone(pk_telefone);


--
-- TOC entry 3439 (class 2606 OID 58012)
-- Name: pessoa_tipo_map fk_ptm_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo_map
    ADD CONSTRAINT fk_ptm_pessoa FOREIGN KEY (fk_pessoa) REFERENCES public.pessoa(pk_pessoa);


--
-- TOC entry 3440 (class 2606 OID 58017)
-- Name: pessoa_tipo_map fk_ptm_tipo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pessoa_tipo_map
    ADD CONSTRAINT fk_ptm_tipo FOREIGN KEY (fk_tipo) REFERENCES public.pessoa_tipo(pk_tipo);


--
-- TOC entry 3445 (class 2606 OID 58092)
-- Name: venda fk_venda_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda
    ADD CONSTRAINT fk_venda_cliente FOREIGN KEY (fk_cliente) REFERENCES public.pessoa(pk_pessoa);


--
-- TOC entry 3446 (class 2606 OID 58112)
-- Name: venda_item fk_vi_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_item
    ADD CONSTRAINT fk_vi_produto FOREIGN KEY (fk_portfolio) REFERENCES public.produto(fk_portfolio);


--
-- TOC entry 3447 (class 2606 OID 58107)
-- Name: venda_item fk_vi_venda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venda_item
    ADD CONSTRAINT fk_vi_venda FOREIGN KEY (fk_venda) REFERENCES public.venda(pk_venda) ON DELETE CASCADE;


--
-- TOC entry 3448 (class 2606 OID 58139)
-- Name: localizacao localizacao_fk_armazem_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.localizacao
    ADD CONSTRAINT localizacao_fk_armazem_fkey FOREIGN KEY (fk_armazem) REFERENCES public.armazem(pk_armazem);


--
-- TOC entry 3430 (class 2606 OID 49634)
-- Name: portfolio portfolio_pai_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.portfolio
    ADD CONSTRAINT portfolio_pai_fk FOREIGN KEY (fk_portfolio_pai) REFERENCES public.portfolio(pk_portfolio) NOT VALID;


-- Completed on 2026-02-11 21:46:41 WAT

--
-- PostgreSQL database dump complete
--

\unrestrict Sxnsso7BQAdLSvpElADgv6eys1MrwCLabRa9eDjgm1Qw6e8BqfqYrM1tJasdJET

