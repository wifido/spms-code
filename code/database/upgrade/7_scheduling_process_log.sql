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
  is '排班月度记录表';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.ID
  is '主键ID';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.YM
  is '年月';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DEPT_ID
  is '网点ID';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.EMP_CODE
  is '被排班人工号';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY1
  is '第1天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY2
  is '第2天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY3
  is '第3天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY4
  is '第4天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY5
  is '第5天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY6
  is '第6天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY7
  is '第7天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY8
  is '第8天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY9
  is '第9天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY10
  is '第10天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY11
  is '第11天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY12
  is '第12天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY13
  is '第13天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY14
  is '第14天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY15
  is '第15天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY16
  is '第16天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY17
  is '第17天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY18
  is '第18天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY19
  is '第19天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY20
  is '第20天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY21
  is '第21天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY22
  is '第22天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY23
  is '第23天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY24
  is '第24天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY25
  is '第25天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY26
  is '第26天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY27
  is '第27天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY28
  is '第28天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY29
  is '第29天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY30
  is '第30天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.DAY31
  is '第31天';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.CREATE_TM
  is '创建日期';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.MODIFIED_TM
  is '修改日期';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.CREATE_EMP_CODE
  is '创建人工号';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TT_PB_SHEDULE_BY_MONTH_LOG.VERSION
  is '版本';
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
