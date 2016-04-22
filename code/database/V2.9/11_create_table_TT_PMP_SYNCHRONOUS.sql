-- Create table
create table TT_PMP_SYNCHRONOUS
(
  ID                NUMBER(38) not null,
  EMP_CODE          VARCHAR2(30),
  BEGIN_DATE        VARCHAR2(8),
  END_DATE          VARCHAR2(8),
  BEGIN_TM          VARCHAR2(6),
  END_TM            VARCHAR2(6),
  TMR_DAY_FLAG      VARCHAR2(1),
  OFF_DUTY_FLAG     VARCHAR2(20),
  CLASS_SYSTEM      VARCHAR2(1),
  CREATE_TM         DATE default sysdate,
  NODE_KEY          VARCHAR2(30),
  STATE_FLG         NUMBER(1) default 0,
  ERROR_INFO        VARCHAR2(1000),
  SCHEDULE_DAILY_ID NUMBER(38),
  EMP_POST_TYPE     VARCHAR2(1),
  SYNC_TM           DATE
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS
  is '排班同步到PMP信息表';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS.ID
  is '主键,序号';
comment on column TT_PMP_SYNCHRONOUS.EMP_CODE
  is '员工工号';
comment on column TT_PMP_SYNCHRONOUS.BEGIN_DATE
  is '开始日期';
comment on column TT_PMP_SYNCHRONOUS.END_DATE
  is '结束日期';
comment on column TT_PMP_SYNCHRONOUS.BEGIN_TM
  is '开始时间';
comment on column TT_PMP_SYNCHRONOUS.END_TM
  is '结束时间';
comment on column TT_PMP_SYNCHRONOUS.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_PMP_SYNCHRONOUS.OFF_DUTY_FLAG
  is '休息标识(休息OFF)';
comment on column TT_PMP_SYNCHRONOUS.CLASS_SYSTEM
  is '排班来源(2-排班系统)';
comment on column TT_PMP_SYNCHRONOUS.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_PMP_SYNCHRONOUS.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';
comment on column TT_PMP_SYNCHRONOUS.ERROR_INFO
  is '同步失败的错误原因';
comment on column TT_PMP_SYNCHRONOUS.SCHEDULE_DAILY_ID
  is '排班ID';
comment on column TT_PMP_SYNCHRONOUS.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员[一线]、3-仓管、4-客服）';
comment on column TT_PMP_SYNCHRONOUS.SYNC_TM
  is '推送时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_PMP_SYNCHRONOUS
  add primary key (ID);
-- Create/Recreate indexes 
create index IDX_PMP_SYNCHRONOUS_1 on TT_PMP_SYNCHRONOUS (BEGIN_DATE);
create index IDX_PMP_SYNCHRONOUS_2 on TT_PMP_SYNCHRONOUS (BEGIN_DATE, EMP_CODE, EMP_POST_TYPE);
create index IDX_PMP_SYNCHRONOUS_3 on TT_PMP_SYNCHRONOUS (STATE_FLG);
create index IDX_PMP_SYNCHRONOUS_4 on TT_PMP_SYNCHRONOUS (CREATE_TM);
grant select on TT_PMP_SYNCHRONOUS  to PMPETL;