-- Create table
create table TT_PMP_SYNCHRONOUS_TMP1
(
  ID             NUMBER(38),
  EMP_CODE       VARCHAR2(30),
  BEGIN_DATE     VARCHAR2(8),
  END_DATE       VARCHAR2(8),
  BEGIN_TM1      VARCHAR2(6),
  END_TM1        VARCHAR2(6),
  BEGIN_TM_FLAG1 NUMBER(1),
  BEGIN_TM2      VARCHAR2(6),
  END_TM2        VARCHAR2(6),
  BEGIN_TM_FLAG2 NUMBER(1),
  BEGIN_TM3      VARCHAR2(6),
  END_TM3        VARCHAR2(6),
  BEGIN_TM_FLAG3 NUMBER(1),
  TMR_DAY_FLAG   VARCHAR2(1),
  OFF_DUTY_FLAG  VARCHAR2(20),
  CLASS_SYSTEM   VARCHAR2(1),
  CREATE_TM      DATE default sysdate,
  NODE_KEY       VARCHAR2(30),
  STATE_FLG      NUMBER(1) default 0,
  ERROR_INFO     VARCHAR2(10),
  EMP_POST_TYPE  VARCHAR2(1)
);
-- Add comments to the table 
comment on table TT_PMP_SYNCHRONOUS_TMP1
  is '运作排班数据同步到PMP临时表';
-- Add comments to the columns 
comment on column TT_PMP_SYNCHRONOUS_TMP1.ID
  is '运作排班数据ID';
comment on column TT_PMP_SYNCHRONOUS_TMP1.EMP_CODE
  is '员工工号';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_DATE
  is '开始日期';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_DATE
  is '结束日期';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM1
  is '开始时间1';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM1
  is '结束时间1';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG1
  is '时间段1是否有值：0无，1有';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM2
  is '开始时间2';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM2
  is '结束时间2';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG2
  is '时间段2：是否有值：0无，1有';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM3
  is '开始时间3';
comment on column TT_PMP_SYNCHRONOUS_TMP1.END_TM3
  is '结束时间3';
comment on column TT_PMP_SYNCHRONOUS_TMP1.BEGIN_TM_FLAG3
  is '时间段3：是否有值：0无，1有';
comment on column TT_PMP_SYNCHRONOUS_TMP1.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_PMP_SYNCHRONOUS_TMP1.OFF_DUTY_FLAG
  is '休息标识(休息为OFF)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.CLASS_SYSTEM
  is '排班来源(2-排班系统)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_PMP_SYNCHRONOUS_TMP1.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';
comment on column TT_PMP_SYNCHRONOUS_TMP1.ERROR_INFO
  is '同步失败的错误原因';
comment on column TT_PMP_SYNCHRONOUS_TMP1.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员[一线]、3-仓管、4-客服）';
