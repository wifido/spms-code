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
  is 'Ա���������켣��';
-- Add comments to the columns 
comment on column TM_EMP_DEPT_CHANGE_RECORD.EMP_CODE
  is 'Ա������';
comment on column TM_EMP_DEPT_CHANGE_RECORD.LAST_ZNO
  is '��һ����';
comment on column TM_EMP_DEPT_CHANGE_RECORD.CURRENT_ZNO
  is '��ǰ����';
comment on column TM_EMP_DEPT_CHANGE_RECORD.CHANGE_DATE
  is '���ʱ��';
