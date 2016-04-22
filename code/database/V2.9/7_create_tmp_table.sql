-- Create table
create table TT_SAPSYNCHRONOUS_HKTMP
(
  ID                NUMBER(38),
  EMP_CODE          VARCHAR2(30),
  BEGIN_DATE        VARCHAR2(8),
  END_DATE          VARCHAR2(8),
  BEGIN_TM          VARCHAR2(6),
  END_TM            VARCHAR2(6),
  TMR_DAY_FLAG      VARCHAR2(1),
  OFF_DUTY_FLAG     VARCHAR2(20),
  CLASS_SYSTEM      VARCHAR2(1),
  CREATE_TM         DATE,
  NODE_KEY          VARCHAR2(30),
  STATE_FLG         NUMBER(1),
  ERROR_INFO        VARCHAR2(1000),
  SCHEDULE_DAILY_ID NUMBER(38),
  EMP_POST_TYPE     VARCHAR2(1),
  SYNC_TM           DATE
);
-- Add comments to the table 
comment on table TT_SAPSYNCHRONOUS_HKTMP
  is '排班同步到SAP信息临时';
-- Add comments to the columns 
comment on column TT_SAPSYNCHRONOUS_HKTMP.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_SAPSYNCHRONOUS_HKTMP.OFF_DUTY_FLAG
  is '休息标识(休息为OFF)';
comment on column TT_SAPSYNCHRONOUS_HKTMP.CLASS_SYSTEM
  is '排班来源(2-排班系统)';
comment on column TT_SAPSYNCHRONOUS_HKTMP.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';
comment on column TT_SAPSYNCHRONOUS_HKTMP.ERROR_INFO
  is '同步失败的错误原因';
comment on column TT_SAPSYNCHRONOUS_HKTMP.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员[一线]、3-仓管、4-客服）';
comment on column TT_SAPSYNCHRONOUS_HKTMP.SYNC_TM
  is '推送时间';
