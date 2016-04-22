------------------------------------------------
-- Export file for user PUSHPN                --
-- Created by sfit0509 on 2014/9/23, 14:37:37 --
------------------------------------------------

spool shuju.log

prompt
prompt Creating table TT_PB_PROCESS_BY_MONTH_LOG
prompt =========================================
prompt
create table TT_PB_PROCESS_BY_MONTH_LOG
(
  ID                NUMBER(38) not null,
  YM                VARCHAR2(50),
  DEPT_ID           NUMBER(19),
  EMP_CODE          VARCHAR2(50),
  DAY1              VARCHAR2(50),
  DAY2              VARCHAR2(50),
  DAY3              VARCHAR2(50),
  DAY4              VARCHAR2(50),
  DAY5              VARCHAR2(50),
  DAY6              VARCHAR2(50),
  DAY7              VARCHAR2(50),
  DAY8              VARCHAR2(50),
  DAY9              VARCHAR2(50),
  DAY10             VARCHAR2(50),
  DAY11             VARCHAR2(50),
  DAY12             VARCHAR2(50),
  DAY13             VARCHAR2(50),
  DAY14             VARCHAR2(50),
  DAY15             VARCHAR2(50),
  DAY16             VARCHAR2(50),
  DAY17             VARCHAR2(50),
  DAY18             VARCHAR2(50),
  DAY19             VARCHAR2(50),
  DAY20             VARCHAR2(50),
  DAY21             VARCHAR2(50),
  DAY22             VARCHAR2(50),
  DAY23             VARCHAR2(50),
  DAY24             VARCHAR2(50),
  DAY25             VARCHAR2(50),
  DAY26             VARCHAR2(50),
  DAY27             VARCHAR2(50),
  DAY28             VARCHAR2(50),
  DAY29             VARCHAR2(50),
  DAY30             VARCHAR2(50),
  DAY31             VARCHAR2(50),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  VERSION           NUMBER(5),
  COMMIT_STATUS     NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table TT_PB_SHEDULE_BY_MONTH_LOG
prompt =========================================
prompt
create table TT_PB_SHEDULE_BY_MONTH_LOG
(
  ID                NUMBER(38) not null,
  YM                VARCHAR2(50),
  DEPT_ID           NUMBER(19),
  EMP_CODE          VARCHAR2(50),
  DAY1              VARCHAR2(50),
  DAY2              VARCHAR2(50),
  DAY3              VARCHAR2(50),
  DAY4              VARCHAR2(50),
  DAY5              VARCHAR2(50),
  DAY6              VARCHAR2(50),
  DAY7              VARCHAR2(50),
  DAY8              VARCHAR2(50),
  DAY9              VARCHAR2(50),
  DAY10             VARCHAR2(50),
  DAY11             VARCHAR2(50),
  DAY12             VARCHAR2(50),
  DAY13             VARCHAR2(50),
  DAY14             VARCHAR2(50),
  DAY15             VARCHAR2(50),
  DAY16             VARCHAR2(50),
  DAY17             VARCHAR2(50),
  DAY18             VARCHAR2(50),
  DAY19             VARCHAR2(50),
  DAY20             VARCHAR2(50),
  DAY21             VARCHAR2(50),
  DAY22             VARCHAR2(50),
  DAY23             VARCHAR2(50),
  DAY24             VARCHAR2(50),
  DAY25             VARCHAR2(50),
  DAY26             VARCHAR2(50),
  DAY27             VARCHAR2(50),
  DAY28             VARCHAR2(50),
  DAY29             VARCHAR2(50),
  DAY30             VARCHAR2(50),
  DAY31             VARCHAR2(50),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  VERSION           NUMBER(5)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on table TT_PB_SHEDULE_BY_MONTH_LOG
  is '�Ű��¶ȼ�¼��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.ID
  is '����ID';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.YM
  is '����';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DEPT_ID
  is '����ID';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.EMP_CODE
  is '���Ű��˹���';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY1
  is '��1��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY2
  is '��2��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY3
  is '��3��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY4
  is '��4��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY5
  is '��5��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY6
  is '��6��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY7
  is '��7��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY8
  is '��8��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY9
  is '��9��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY10
  is '��10��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY11
  is '��11��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY12
  is '��12��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY13
  is '��13��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY14
  is '��14��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY15
  is '��15��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY16
  is '��16��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY17
  is '��17��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY18
  is '��18��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY19
  is '��19��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY20
  is '��20��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY21
  is '��21��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY22
  is '��22��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY23
  is '��23��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY24
  is '��24��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY25
  is '��25��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY26
  is '��26��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY27
  is '��27��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY28
  is '��28��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY29
  is '��29��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY30
  is '��30��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY31
  is '��31��';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.CREATE_TM
  is '��������';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.MODIFIED_TM
  is '�޸�����';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.CREATE_EMP_CODE
  is '�����˹���';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.MODIFIED_EMP_CODE
  is '�޸��˹���';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.VERSION
  is '�汾';
alter table TT_PB_SHEDULE_BY_MONTH_LOG
  add constraint PK_TT_PB_SHEDULE_BY_MONTH_LOG primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


spool off
