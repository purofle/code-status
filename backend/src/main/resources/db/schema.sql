--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

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

DROP DATABASE IF EXISTS codestatus;
--
-- Name: codestatus; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE codestatus;


ALTER DATABASE codestatus OWNER TO postgres;

\connect codestatus

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

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: codestatus
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO codestatus;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: codestatus
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: codestatus
--

CREATE SEQUENCE public.account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_id_seq OWNER TO codestatus;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account; Type: TABLE; Schema: public; Owner: codestatus
--

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
-- Name: commit_id_seq; Type: SEQUENCE; Schema: public; Owner: codestatus
--

CREATE SEQUENCE public.commit_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.commit_id_seq OWNER TO codestatus;

--
-- Name: commits; Type: TABLE; Schema: public; Owner: codestatus
--

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

--
-- Name: repository; Type: TABLE; Schema: public; Owner: codestatus
--

CREATE TABLE public.repository (
    id integer DEFAULT nextval('public.commit_id_seq'::regclass) NOT NULL,
    full_name character varying(255),
    private boolean DEFAULT false NOT NULL,
    owner_id integer NOT NULL,
    node_id text NOT NULL
);


ALTER TABLE public.repository OWNER TO codestatus;

--
-- Name: repository_id_seq; Type: SEQUENCE; Schema: public; Owner: codestatus
--

CREATE SEQUENCE public.repository_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.repository_id_seq OWNER TO codestatus;

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


--
-- Name: repository repository_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: codestatus
--

ALTER TABLE ONLY public.repository
    ADD CONSTRAINT repository_account_id_fk FOREIGN KEY (owner_id) REFERENCES public.account(id);


--
-- PostgreSQL database dump complete
--

