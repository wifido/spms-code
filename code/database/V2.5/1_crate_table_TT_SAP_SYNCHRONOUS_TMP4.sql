-- Create table
create table TT_SAP_SYNCHRONOUS_TMP4
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
  CREATE_TM     DATE,
  NODE_KEY      VARCHAR2(30),
  STATE_FLG     NUMBER(1),
  ERROR_INFO    VARCHAR2(10),
  EMP_POST_TYPE VARCHAR2(1)
)
tablespace SPMS
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the table 
comment on table TT_SAP_SYNCHRONOUS_TMP4
  is '排班同步到SAP信息临时';
-- Add comments to the columns 
comment on column TT_SAP_SYNCHRONOUS_TMP4.ID
  is '排班数据ID';
comment on column TT_SAP_SYNCHRONOUS_TMP4.EMP_CODE
  is '员工工号';
comment on column TT_SAP_SYNCHRONOUS_TMP4.BEGIN_DATE
  is '开始日期';
comment on column TT_SAP_SYNCHRONOUS_TMP4.END_DATE
  is '结束日期';
comment on column TT_SAP_SYNCHRONOUS_TMP4.BEGIN_TM
  is '开始时间';
comment on column TT_SAP_SYNCHRONOUS_TMP4.END_TM
  is '结束时间';
comment on column TT_SAP_SYNCHRONOUS_TMP4.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_SAP_SYNCHRONOUS_TMP4.OFF_DUTY_FLAG
  is '休息标识(休息为OFF)';
comment on column TT_SAP_SYNCHRONOUS_TMP4.CLASS_SYSTEM
  is '排班来源(2-排班系统)';
comment on column TT_SAP_SYNCHRONOUS_TMP4.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_SAP_SYNCHRONOUS_TMP4.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';
comment on column TT_SAP_SYNCHRONOUS_TMP4.ERROR_INFO
  is '同步失败的错误原因';
comment on column TT_SAP_SYNCHRONOUS_TMP4.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员[一线]、3-仓管、5-司机）';
