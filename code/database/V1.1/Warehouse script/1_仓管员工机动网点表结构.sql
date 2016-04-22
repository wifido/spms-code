-------------------------------------------------
-- Export file for user PUSHPN                 --
-- Created by sfit0505 on 2014/10/15, 10:09:29 --
-------------------------------------------------

spool tt_warehouse_emp_dept_r.log

prompt
prompt Creating table TT_WAREHOUSE_EMP_DEPT_R
prompt ======================================
prompt
create table TT_WAREHOUSE_EMP_DEPT_R
(
  EMP_CODE VARCHAR2(20) not null,
  DEPT_ID  NUMBER(38) not null
)
;
comment on column TT_WAREHOUSE_EMP_DEPT_R.EMP_CODE
  is '员工工号';
comment on column TT_WAREHOUSE_EMP_DEPT_R.DEPT_ID
  is '网点ID';
alter table TT_WAREHOUSE_EMP_DEPT_R
  add constraint EMP_CODE_DEPT_CODE_PRIMARYKEY primary key (EMP_CODE, DEPT_ID);


spool off
