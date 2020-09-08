--
-- PostgreSQL database dump
--

-- Dumped from database version 11.9 (Debian 11.9-1.pgdg90+1)
-- Dumped by pg_dump version 11.9 (Debian 11.9-1.pgdg90+1)

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

SET default_with_oids = false;

--
-- Name: m_bom; Type: TABLE; Schema: public; Owner: manufacture_user
--

CREATE TABLE public.m_bom (
    b_comp_id character varying(20) NOT NULL,
    b_line_no integer NOT NULL,
    b_assembly_id character varying(20) NOT NULL,
    b_eng_change character varying(10),
    b_ops_desc character varying(100),
    b_ops integer,
    b_qty integer,
    b_version integer
);


ALTER TABLE public.m_bom OWNER TO manufacture_user;

--
-- Name: m_inventory; Type: TABLE; Schema: public; Owner: manufacture_user
--

CREATE TABLE public.m_inventory (
    in_p_id character varying(20) NOT NULL,
    in_location integer NOT NULL,
    in_acc_code integer,
    in_act_date timestamp without time zone,
    in_ordered integer,
    in_qty integer,
    in_version integer
);


ALTER TABLE public.m_inventory OWNER TO manufacture_user;

--
-- Name: m_parts; Type: TABLE; Schema: public; Owner: manufacture_user
--

CREATE TABLE public.m_parts (
    p_id character varying(20) NOT NULL,
    p_type integer,
    p_desc character varying(100),
    p_himark integer,
    p_lomark integer,
    p_name character varying(35),
    p_planner integer,
    p_ind integer,
    p_rev character varying(6),
    p_version integer
);


ALTER TABLE public.m_parts OWNER TO manufacture_user;

--
-- Name: m_workorder; Type: TABLE; Schema: public; Owner: manufacture_user
--

CREATE TABLE public.m_workorder (
    wo_number integer NOT NULL,
    wo_assembly_id character varying(20),
    wo_comp_qty integer,
    wo_due_date timestamp without time zone,
    wo_location integer NOT NULL,
    wo_ol_id integer,
    wo_orig_qty integer,
    wo_o_id integer,
    wo_start_date timestamp without time zone,
    wo_status integer,
    wo_version integer
);


ALTER TABLE public.m_workorder OWNER TO manufacture_user;

--
-- Name: u_sequences; Type: TABLE; Schema: public; Owner: manufacture_user
--

CREATE TABLE public.u_sequences (
    s_id character varying(50) NOT NULL,
    s_nextnum numeric(38,0)
);


ALTER TABLE public.u_sequences OWNER TO manufacture_user;


--
-- Name: m_bom m_bom_pkey; Type: CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_bom
    ADD CONSTRAINT m_bom_pkey PRIMARY KEY (b_comp_id, b_line_no, b_assembly_id);


--
-- Name: m_inventory m_inventory_pkey; Type: CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_inventory
    ADD CONSTRAINT m_inventory_pkey PRIMARY KEY (in_p_id, in_location);


--
-- Name: m_parts m_parts_pkey; Type: CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_parts
    ADD CONSTRAINT m_parts_pkey PRIMARY KEY (p_id);


--
-- Name: m_workorder m_workorder_pkey; Type: CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_workorder
    ADD CONSTRAINT m_workorder_pkey PRIMARY KEY (wo_number);


--
-- Name: u_sequences u_sequences_pkey; Type: CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.u_sequences
    ADD CONSTRAINT u_sequences_pkey PRIMARY KEY (s_id);


--
-- Name: m_bom fk_m_bom_b_assembly_id; Type: FK CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_bom
    ADD CONSTRAINT fk_m_bom_b_assembly_id FOREIGN KEY (b_assembly_id) REFERENCES public.m_parts(p_id);


--
-- Name: m_inventory fk_m_inventory_in_p_id; Type: FK CONSTRAINT; Schema: public; Owner: manufacture_user
--

ALTER TABLE ONLY public.m_inventory
    ADD CONSTRAINT fk_m_inventory_in_p_id FOREIGN KEY (in_p_id) REFERENCES public.m_parts(p_id);

--
-- PostgreSQL database dump complete
--

