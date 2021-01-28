--
-- PostgreSQL database dump
--

-- Dumped from database version 11.9 (Debian 11.9-1.pgdg90+1)
-- Dumped by pg_dump version 11.9 (Debian 11.9-1.pgdg90+1)

--
-- Data for Name: m_parts; Type: TABLE DATA; Schema: public; Owner: manufacture_user
--

COPY public.m_parts (p_id, p_type, p_desc, p_himark, p_lomark, p_name, p_planner, p_ind, p_rev, p_version) FROM stdin;
1	0	The 1st part...	1	1	Part 1	1	1	1	1
2	0	The 2nd part...	1	1	Part 2	1	1	1	1
3	0	The 3rd part...	1	1	Part 3	1	1	1	1
4	1	Assembly 1 which is build from 3 parts	3	3	Assembly 1	2	3	1	1
5	1	Assembly 2 which is build from 2 parts	3	3	Assembly 2	2	3	1	1
6	1	Assembly 3 which is build from 2 parts	3	3	Assembly 3	2	3	1	1
7	1	Assembly 4 which is build from 2 parts	3	3	Assembly 4	2	3	1	1
8	1	Assembly 5 which is build from 2 parts	3	3	Assembly 5	2	3	1	1
9	1	Assembly 6 which is build from 2 parts	3	3	Assembly 6	2	3	1	1
10	1	Assembly 7 which is build from 2 parts	3	3	Assembly 7	2	3	1	1
11	1	Assembly 8 which is build from 2 parts	3	3	Assembly 8	2	3	1	1
12	1	Assembly 9 which is build from 2 parts	3	3	Assembly 9	2	3	1	1
13	1	Assembly 10 which is build from 2 parts	3	3	Assembly 10	2	3	1	1
14	1	Assembly 11 which is build from 2 parts	3	3	Assembly 11	2	3	1	1
15	1	Assembly 12 which is build from 2 parts	3	3	Assembly 12	2	3	1	1
16	1	Assembly 13 which is build from 2 parts	3	3	Assembly 13	2	3	1	1
17	1	Assembly 14 which is build from 2 parts	3	3	Assembly 14	2	3	1	1
18	1	Assembly 15 which is build from 2 parts	3	3	Assembly 15	2	3	1	1
19	1	Assembly 16 which is build from 2 parts	3	3	Assembly 16	2	3	1	1
20	1	Assembly 17 which is build from 2 parts	3	3	Assembly 17	2	3	1	1
21	1	Assembly 18 which is build from 2 parts	3	3	Assembly 18	2	3	1	1
22	1	Assembly 19 which is build from 2 parts	3	3	Assembly 19	2	3	1	1
23	1	Assembly 20 which is build from 2 parts	3	3	Assembly 20	2	3	1	1
24	1	Assembly 21 which is build from 2 parts	3	3	Assembly 21	2	3	1	1
\.


--
-- Data for Name: m_workorder; Type: TABLE DATA; Schema: public; Owner: manufacture_user
--

COPY public.m_workorder (wo_number, wo_assembly_id, wo_comp_qty, wo_due_date, wo_location, wo_ol_id, wo_orig_qty, wo_o_id, wo_start_date, wo_status, wo_version) FROM stdin;
\.


--
-- Data for Name: u_sequences; Type: TABLE DATA; Schema: public; Owner: manufacture_user
--

--COPY public.u_sequences (s_id, s_nextnum) FROM stdin;
--WO_SEQ	0
--C_SEQ	24
--\.


--
-- Data for Name: m_bom; Type: TABLE DATA; Schema: public; Owner: manufacture_user
--

COPY public.m_bom (b_comp_id, b_line_no, b_assembly_id, b_eng_change, b_ops_desc, b_ops, b_qty, b_version) FROM stdin;
1	1	4	engChange	description	1	4	1
2	2	4	engChange	description	1	2	1
3	3	4	engChange	description	1	2	1
1	1	5	engChange	description	1	2	1
2	2	5	engChange	description	1	4	1
1	1	6	engChange	description	1	2	1
3	2	6	engChange	description	1	4	1
1	1	7	engChange	description	1	2	1
3	2	7	engChange	description	1	4	1
1	1	8	engChange	description	1	2	1
3	2	8	engChange	description	1	4	1
1	1	9	engChange	description	1	2	1
3	2	9	engChange	description	1	4	1
1	1	10	engChange	description	1	2	1
3	2	10	engChange	description	1	4	1
1	1	11	engChange	description	1	2	1
3	2	11	engChange	description	1	4	1
1	1	12	engChange	description	1	2	1
3	2	12	engChange	description	1	4	1
1	1	13	engChange	description	1	2	1
3	2	13	engChange	description	1	4	1
1	1	14	engChange	description	1	2	1
3	2	14	engChange	description	1	4	1
1	1	15	engChange	description	1	2	1
3	2	15	engChange	description	1	4	1
1	1	16	engChange	description	1	2	1
3	2	16	engChange	description	1	4	1
1	1	17	engChange	description	1	2	1
3	2	17	engChange	description	1	4	1
1	1	18	engChange	description	1	2	1
3	2	18	engChange	description	1	4	1
1	1	19	engChange	description	1	2	1
3	2	19	engChange	description	1	4	1
1	1	20	engChange	description	1	2	1
3	2	20	engChange	description	1	4	1
1	1	21	engChange	description	1	2	1
3	2	21	engChange	description	1	4	1
1	1	22	engChange	description	1	2	1
3	2	22	engChange	description	1	4	1
1	1	23	engChange	description	1	2	1
3	2	23	engChange	description	1	4	1
1	1	24	engChange	description	1	2	1
3	2	24	engChange	description	1	4	1
\.


--
-- Data for Name: m_inventory; Type: TABLE DATA; Schema: public; Owner: manufacture_user
--

COPY public.m_inventory (in_p_id, in_location, in_acc_code, in_act_date, in_ordered, in_qty, in_version) FROM stdin;
1	1	1	2019-12-02 00:00:00	10	10	1
3	1	1	2019-12-02 00:00:00	10	10	1
2	1	1	2019-12-02 00:00:00	10	10	1
5	1	1	2019-12-02 00:00:00	10	10	1
4	1	1	2019-12-02 00:00:00	10	10	1
\.






--
-- PostgreSQL database dump complete
--

