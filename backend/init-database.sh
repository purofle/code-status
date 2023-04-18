#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
CREATE TABLE public.account (
    id integer DEFAULT nextval('public.account_id_seq'::regclass) NOT NULL,
    avatar_url character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255),
    login character varying(255) NOT NULL,
    node_id text NOT NULL
);


ALTER TABLE public.account OWNER TO codestatus;

--
-- Name: commits; Type: TABLE; Schema: public; Owner: codestatus
--

CREATE TABLE public.commits (
    user_id integer NOT NULL,
    commit_id character varying(40) NOT NULL,
    "timestamp" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    message text NOT NULL,
    url character varying(255) NOT NULL,
    added_files integer,
    removed_files integer,
    modified_files integer,
    repository_id character varying(255) NOT NULL,
    id integer DEFAULT nextval('public.commit_id_seq'::regclass) NOT NULL,
    node_id text
);


ALTER TABLE public.commits OWNER TO codestatus;

--
-- Name: repository; Type: TABLE; Schema: public; Owner: codestatus
--

CREATE TABLE public.repository (
    id integer DEFAULT nextval('public.commit_id_seq'::regclass) NOT NULL,
    full_name character varying(255),
    private boolean DEFAULT false NOT NULL,
    node_id text NOT NULL
);


ALTER TABLE public.repository OWNER TO codestatus;

--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: commits commits_pk; Type: CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.commits
    ADD CONSTRAINT commits_pk UNIQUE (commit_id);


--
-- Name: commits commits_pk2; Type: CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.commits
    ADD CONSTRAINT commits_pk2 PRIMARY KEY (id);


--
-- Name: repository repository_pk; Type: CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.repository
    ADD CONSTRAINT repository_pk PRIMARY KEY (id);


--
-- Name: repository_id_uindex; Type: INDEX; Schema: public; Owner: codestatus
--

CREATE UNIQUE INDEX repository_id_uindex ON public.repository USING btree (id);


--
-- Name: commits commits_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.commits
    ADD CONSTRAINT commits_account_id_fk FOREIGN KEY (user_id) REFERENCES public.account(id) ON DELETE RESTRICT;

EOSQL
