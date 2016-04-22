-- Create table
create table TT_PMP_SYNCHRONOUS_TMP
(
  ID            NUMBER(38),
  EMP_CODE      VARCHAR2(30),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  CREATE_TM     DATE default sysdate,
  NODE_KEY      VARCHAR2(30),
  STATE_FLG     NUMBER(1) default 0,
  ERROR_INFO    VARCHAR2(10),
  EMP_POST_TYPE VARCHAR2(1)
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS_TMP
  is '排班同步到PMP临时表';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS_TMP.ID
  is '排班数据ID';
comment on column TT_PMP_SYNCHRONOUS_TMP.EMP_CODE
  is '员工工号';
comment on column TT_PMP_SYNCHRONOUS_TMP.BEGIN_DATE
  is '开始日期';
comment on column TT_PMP_SYNCHRONOUS_TMP.END_DATE
  is '结束日期';
comment on column TT_PMP_SYNCHRONOUS_TMP.BEGIN_TM
  is '开始时间';
comment on column TT_PMP_SYNCHRONOUS_TMP.END_TM
  is '结束时间';
comment on column TT_PMP_SYNCHRONOUS_TMP.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_PMP_SYNCHRONOUS_TMP.OFF_DUTY_FLAG
  is '休息标识(休息为OFF)';
comment on column TT_PMP_SYNCHRONOUS_TMP.CLASS_SYSTEM
  is '排班来源(2-排班系统)';
comment on column TT_PMP_SYNCHRONOUS_TMP.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_PMP_SYNCHRONOUS_TMP.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';
comment on column TT_PMP_SYNCHRONOUS_TMP.ERROR_INFO
  is '同步失败的错误原因';
comment on column TT_PMP_SYNCHRONOUS_TMP.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员[一线]、3-仓管、4-客服）';
