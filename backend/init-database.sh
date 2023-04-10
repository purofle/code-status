#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SEQUENCE public.account_id_seq
        AS integer
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1;

    ALTER TABLE public.account_id_seq OWNER TO codestatus;

    CREATE TABLE public.account (
        id integer DEFAULT nextval('public.account_id_seq'::regclass) NOT NULL,
        avatar_url character varying(255) NOT NULL,
        email character varying(255) NOT NULL,
        name character varying(255),
        login character varying(255) NOT NULL,
        node_id text NOT NULL
    );

    ALTER TABLE public.account OWNER TO codestatus;

    CREATE SEQUENCE public.commit_id_seq
        AS integer
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1;

    ALTER TABLE public.commit_id_seq OWNER TO codestatus;

    CREATE TABLE public.commits (
        user_id integer NOT NULL,
        commit_id character varying(40) NOT NULL,
        "timestamp" timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
        message character varying(255) NOT NULL,
        url character varying(255) NOT NULL,
        added_files integer,
        removed_files integer,
        modified_files integer,
        repository_id character varying(255) NOT NULL,
        id integer DEFAULT nextval('public.commit_id_seq'::regclass) NOT NULL,
        node_id text
    );

    ALTER TABLE public.commits OWNER TO codestatus;

    CREATE TABLE public.repository (
        id integer DEFAULT nextval('public.commit_id_seq'::regclass) NOT NULL,
        full_name character varying(255),
        private boolean DEFAULT false NOT NULL,
        owner_id integer NOT NULL,
        node_id text NOT NULL
    );
    ALTER TABLE public.repository OWNER TO codestatus;

    CREATE SEQUENCE public.repository_id_seq
        AS integer
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1;

    ALTER TABLE public.repository_id_seq OWNER TO codestatus;

    ALTER TABLE ONLY public.account
        ADD CONSTRAINT account_pkey PRIMARY KEY (id);

    ALTER TABLE ONLY public.commits
        ADD CONSTRAINT commits_pk UNIQUE (commit_id);

    ALTER TABLE ONLY public.commits
        ADD CONSTRAINT commits_pk2 PRIMARY KEY (id);

    ALTER TABLE ONLY public.repository
        ADD CONSTRAINT repository_pk PRIMARY KEY (id);

    CREATE UNIQUE INDEX repository_id_uindex ON public.repository USING btree (id);

    ALTER TABLE ONLY public.commits
        ADD CONSTRAINT commits_account_id_fk FOREIGN KEY (user_id) REFERENCES public.account(id) ON DELETE RESTRICT;

    ALTER TABLE ONLY public.repository
        ADD CONSTRAINT repository_account_id_fk FOREIGN KEY (owner_id) REFERENCES public.account(id);
EOSQL
