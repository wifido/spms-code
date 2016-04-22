-- Create table
create table OPERATION_COUNT_SHEDULING
(
  MONTH_ID           VARCHAR2(28),
  AREA_CODE          VARCHAR2(38),
  DEPT_CODE          VARCHAR2(10),
  EMP_CODE           VARCHAR2(50),
  EMP_NAME           VARCHAR2(50),
  PERSK_TXT          VARCHAR2(50),
  SF_DATE            DATE,
  EMP_STATUS         NUMBER(1),
  SHEDULE_NUM        NUMBER(10),
  GROUP_NUM          NUMBER(10),
  PROCESS_NUM        NUMBER(10),
  LENGTH_TIME_OF_DAY NUMBER(10,2),
  REST_DAYS          NUMBER(10),
  TOTAL_ATTENDANCE   NUMBER(10),
  DEPT_ID            VARCHAR2(38)
);
-- Add comments to the table 
comment on table OPERATION_COUNT_SHEDULING
  is '排班表';
-- Add comments to the columns 
comment on column OPERATION_COUNT_SHEDULING.MONTH_ID
  is '月份';
comment on column OPERATION_COUNT_SHEDULING.AREA_CODE
  is '地区代码';
comment on column OPERATION_COUNT_SHEDULING.DEPT_CODE
  is '网点代码';
comment on column OPERATION_COUNT_SHEDULING.EMP_CODE
  is '员工工号';
comment on column OPERATION_COUNT_SHEDULING.EMP_NAME
  is '员工姓名';
comment on column OPERATION_COUNT_SHEDULING.PERSK_TXT
  is '人员类型';
comment on column OPERATION_COUNT_SHEDULING.SF_DATE
  is '入职时间';
comment on column OPERATION_COUNT_SHEDULING.EMP_STATUS
  is '在职状态（1在职0离职）';
comment on column OPERATION_COUNT_SHEDULING.SHEDULE_NUM
  is '班别数量';
comment on column OPERATION_COUNT_SHEDULING.GROUP_NUM
  is '小组数量';
comment on column OPERATION_COUNT_SHEDULING.PROCESS_NUM
  is '工序数量';
comment on column OPERATION_COUNT_SHEDULING.LENGTH_TIME_OF_DAY
  is '日均时长';
comment on column OPERATION_COUNT_SHEDULING.REST_DAYS
  is '休息天数';
comment on column OPERATION_COUNT_SHEDULING.TOTAL_ATTENDANCE
  is '出勤时长';
comment on column OPERATION_COUNT_SHEDULING.DEPT_ID
  is '网点ID';
-- Create/Recreate indexes 
create index INDEX_MONTH_ID on OPERATION_COUNT_SHEDULING (MONTH_ID);
