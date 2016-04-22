-----------------------------------------------
-- Export file for user PUSHPN               --
-- Created by sfit0505 on 2014/9/15, 9:30:35 --
-----------------------------------------------

spool Initializes sequence.log

prompt
prompt Creating sequence SEQ_DEMO
prompt ==========================
prompt
create sequence SEQ_DEMO
minvalue 1
maxvalue 999999999999999999
start with 10001
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_OSS_BASE
prompt ==============================
prompt
create sequence SEQ_OSS_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 900741
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_OSS_INTERFACE
prompt ===================================
prompt
create sequence SEQ_OSS_INTERFACE
minvalue 1
maxvalue 999999999999999999999999999
start with 835621
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_OSS_TL
prompt ============================
prompt
create sequence SEQ_OSS_TL
minvalue 1
maxvalue 999999999999999999999999999
start with 961
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_OUT_ENPLOYEE_BASE
prompt =======================================
prompt
create sequence SEQ_OUT_ENPLOYEE_BASE
minvalue 100000000
maxvalue 999999999999999999999999999
start with 100004306
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_PX_BASE
prompt =============================
prompt
create sequence SEQ_PX_BASE
minvalue 1
maxvalue 999999999999999999999999999
start with 121021
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_SCHEDULE_DATLY_S
prompt ======================================
prompt
create sequence SEQ_SCHEDULE_DATLY_S
minvalue 1
maxvalue 999999999999999999
start with 9581
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_SYSMGMT
prompt =============================
prompt
create sequence SEQ_SYSMGMT
minvalue 1
maxvalue 999999999999999999
start with 10341
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TT_PB_OVERTIME
prompt ====================================
prompt
create sequence SEQ_TT_PB_OVERTIME
minvalue 1
maxvalue 999999999999999999
start with 41
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TT_SAP_SYNCHRONOUS
prompt ========================================
prompt
create sequence SEQ_TT_SAP_SYNCHRONOUS
minvalue 1
maxvalue 999999999999999999
start with 2301
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_USER_LOG
prompt ==============================
prompt
create sequence SEQ_USER_LOG
minvalue 1
maxvalue 999999999999999999
start with 16005
increment by 1
cache 20;

prompt
prompt Creating sequence ZCJ_TEST
prompt ==========================
prompt
create sequence ZCJ_TEST
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_PROCESS_BY_MONTH_LOG
prompt ==========================
prompt
create sequence SEQ_PROCESS_BY_MONTH_LOG
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_SHEDULE_BY_MONTH_LOG
prompt ==========================
prompt
create sequence SEQ_SHEDULE_BY_MONTH_LOG
minvalue 1
maxvalue 999999999999999999
start with 1
increment by 1
cache 20;

spool off
