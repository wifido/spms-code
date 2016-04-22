-- Create table
create table TM_SPMS2CDP_BY_OPERATION_INFO
(
  SCHEDULE_CODE VARCHAR2(9),
  SCHEDULE_DT   DATE,
  DEPT_CODE     VARCHAR2(38),
  START1_TIME   DATE,
  END1_TIME     DATE,
  START2_TIME   DATE,
  END2_TIME     DATE,
  START3_TIME   DATE,
  END3_TIME     DATE,
  YM            VARCHAR2(25)
);
-- Add comments to the table 
comment on table TM_SPMS2CDP_BY_OPERATION_INFO
  is '班别基础信息表';
-- Add comments to the columns 
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.SCHEDULE_CODE
  is '班别代码';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.SCHEDULE_DT
  is '排班日期';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.DEPT_CODE
  is '网点代码';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START1_TIME
  is '开始时间一';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END1_TIME
  is '结束时间一';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START2_TIME
  is '开始时间二';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END2_TIME
  is '结束时间二';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.START3_TIME
  is '开始时间三';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.END3_TIME
  is '结束时间三';
comment on column TM_SPMS2CDP_BY_OPERATION_INFO.YM
  is '年月';
-- Create/Recreate indexes 
create index IDX_TM_SPMS2CDP_INFO1 on TM_SPMS2CDP_BY_OPERATION_INFO (DEPT_CODE, SCHEDULE_CODE, SCHEDULE_DT, YM);
