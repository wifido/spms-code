-- Create table
create table TT_CDP_EMPNUM_SCHEDUL_HIS
(
  DAY_OF_MONTH            VARCHAR2(10),
  HQ_CODE                 VARCHAR2(30),
  AREA_CODE               VARCHAR2(30),
  DEPT_CODE               VARCHAR2(30),
  DIVISION_CODE           VARCHAR2(30),
  FULL_TIME_NUM           NUMBER(10),
  N_FULL_TIME_NUM         NUMBER(10),
  TOTAL_PAYROLLS          NUMBER(10,2),
  FULL_TIME_SCHEDUL_NUM   NUMBER(10),
  N_FULL_TIME_SCHEDUL_NUM NUMBER(10),
  SCHEDUL_NUM_TOTAL       NUMBER(10,2),
  POST_TYPE               VARCHAR2(10),
  CREATE_DATE             DATE
);
-- Add comments to the table 
comment on table TT_CDP_EMPNUM_SCHEDUL_HIS
  is 'CDP统计数据接口表';
-- Add comments to the columns 
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DAY_OF_MONTH
  is '日期';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.HQ_CODE
  is '经营本部';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.AREA_CODE
  is '地区';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DEPT_CODE
  is '网点';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.DIVISION_CODE
  is '分部';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.FULL_TIME_NUM
  is '全日制在职人数';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.N_FULL_TIME_NUM
  is '非全在职人数';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.TOTAL_PAYROLLS
  is '在职人数合计';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.FULL_TIME_SCHEDUL_NUM
  is '全日制排班人数';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.N_FULL_TIME_SCHEDUL_NUM
  is '非全排班人数';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.SCHEDUL_NUM_TOTAL
  is '排班人数合计';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.POST_TYPE
  is '岗位类型';
comment on column TT_CDP_EMPNUM_SCHEDUL_HIS.CREATE_DATE
  is '创建时间';
