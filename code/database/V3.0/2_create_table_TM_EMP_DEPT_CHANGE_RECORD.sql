-- Create table
create table TM_EMP_DEPT_CHANGE_RECORD
(
  ID          NUMBER,
  EMP_CODE    VARCHAR2(38),
  LAST_ZNO    VARCHAR2(18),
  CURRENT_ZNO VARCHAR2(18),
  CHANGE_DATE DATE
);
-- Add comments to the table 
comment on table TM_EMP_DEPT_CHANGE_RECORD
  is '员工网点变更轨迹表';
-- Add comments to the columns 
comment on column TM_EMP_DEPT_CHANGE_RECORD.EMP_CODE
  is '员工工号';
comment on column TM_EMP_DEPT_CHANGE_RECORD.LAST_ZNO
  is '上一网点';
comment on column TM_EMP_DEPT_CHANGE_RECORD.CURRENT_ZNO
  is '当前网点';
comment on column TM_EMP_DEPT_CHANGE_RECORD.CHANGE_DATE
  is '变更时间';
