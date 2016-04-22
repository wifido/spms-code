-- Create table
create table TI_SCH_EMPLOYEECLASS_PLAIN
(
  ID         NUMBER(20) not null,
  EMPLOYEEID VARCHAR2(20),
  DUTYDATE   VARCHAR2(20),
  CREATETIME DATE,
  SYNC_TM    DATE default SYSDATE
);
-- Add comments to the table 
comment on table TI_SCH_EMPLOYEECLASS_PLAIN
  is 'SCH_CDH收派员值班信息接口表';
-- Add comments to the columns 
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.ID
  is '逻辑主键';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.EMPLOYEEID
  is '收派员工号';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.DUTYDATE
  is '值班日期';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.CREATETIME
  is 'SCH_CDH创建时间';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.SYNC_TM
  is '同步时间';
-- GrantRevoke object privileges 
grant all on TI_SCH_EMPLOYEECLASS_PLAIN to SPMSETL;

-- Create table
create table TT_CDP_EMPNUM_SCHEDULINGNUM
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
comment on table TT_CDP_EMPNUM_SCHEDULINGNUM
  is 'CDP统计数据接口表';
-- Add comments to the columns 
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DAY_OF_MONTH
  is '日期';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.HQ_CODE
  is '经营本部';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.AREA_CODE
  is '地区';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DEPT_CODE
  is '网点';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.DIVISION_CODE
  is '分部';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.FULL_TIME_NUM
  is '全日制在职人数';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.N_FULL_TIME_NUM
  is '非全在职人数';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.TOTAL_PAYROLLS
  is '在职人数合计';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.FULL_TIME_SCHEDUL_NUM
  is '全日制排班人数';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.N_FULL_TIME_SCHEDUL_NUM
  is '非全排班人数';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.SCHEDUL_NUM_TOTAL
  is '排班人数合计';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.POST_TYPE
  is '岗位类型
';
comment on column TT_CDP_EMPNUM_SCHEDULINGNUM.CREATE_DATE
  is '创建时间';