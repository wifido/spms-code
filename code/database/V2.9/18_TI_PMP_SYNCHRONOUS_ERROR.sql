-- Create table
create table TI_PMP_SYNCHRONOUS_ERROR
(
  ID            NUMBER(38) not null,
  EMP_CODE      VARCHAR2(30),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  THEME_NAME    VARCHAR2(100),
  ERROR_INFO    VARCHAR2(300),
  LASTUPDATE    DATE default sysdate,
  EXTRAINFO1    VARCHAR2(100),
  EXTRAINFO2    VARCHAR2(300),
  EXTRAINFO3    VARCHAR2(1000),
  DEAL_FLAG     NUMBER(1) default 0
);
-- Add comments to the table 
comment on table TI_PMP_SYNCHRONOUS_ERROR
  is 'SAP同步到排班错误信息表接口表';
-- Add comments to the columns 
comment on column TI_PMP_SYNCHRONOUS_ERROR.ID
  is '主键,序号';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EMP_CODE
  is '员工工号';
comment on column TI_PMP_SYNCHRONOUS_ERROR.BEGIN_DATE
  is '开始日期';
comment on column TI_PMP_SYNCHRONOUS_ERROR.END_DATE
  is '结束日期';
comment on column TI_PMP_SYNCHRONOUS_ERROR.BEGIN_TM
  is '开始时间';
comment on column TI_PMP_SYNCHRONOUS_ERROR.END_TM
  is '结束时间';
comment on column TI_PMP_SYNCHRONOUS_ERROR.TMR_DAY_FLAG
  is '前一天标识';
comment on column TI_PMP_SYNCHRONOUS_ERROR.OFF_DUTY_FLAG
  is '休息标识(ON为休息，默认为OFF)';
comment on column TI_PMP_SYNCHRONOUS_ERROR.CLASS_SYSTEM
  is '排班来源(2-调度系统)';
comment on column TI_PMP_SYNCHRONOUS_ERROR.THEME_NAME
  is '主题名称';
comment on column TI_PMP_SYNCHRONOUS_ERROR.ERROR_INFO
  is '同步失败的错误原因';
comment on column TI_PMP_SYNCHRONOUS_ERROR.LASTUPDATE
  is '同步时间';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO1
  is '扩展字段1';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO2
  is '扩展字段2';
comment on column TI_PMP_SYNCHRONOUS_ERROR.EXTRAINFO3
  is '扩展字段3';
comment on column TI_PMP_SYNCHRONOUS_ERROR.DEAL_FLAG
  is '1:已处理0:未处理2：同步错误';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_PMP_SYNCHRONOUS_ERROR
  add primary key (ID);
-- Create/Recreate indexes 
create index IDX_PMP_SYNCHRONOUS_ERROR1 on TI_PMP_SYNCHRONOUS_ERROR (DEAL_FLAG, ERROR_INFO, BEGIN_DATE);
-- Grant/Revoke object privileges 
grant select, insert, update, delete on TI_PMP_SYNCHRONOUS_ERROR to PMPETL;
