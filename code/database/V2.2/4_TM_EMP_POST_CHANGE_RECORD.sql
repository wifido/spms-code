-- Create table
create table TM_EMP_POST_CHANGE_RECORD
(
  ID            NUMBER,
  EMP_CODE      VARCHAR2(38),
  PREVIOUS_POST VARCHAR2(2),
  CURRENT_POST  VARCHAR2(2),
  CHANGE_DATE   DATE
);
-- Add comments to the table 
comment on table TM_EMP_POST_CHANGE_RECORD
  is 'Ա����λ����켣��';
-- Add comments to the columns 
comment on column TM_EMP_POST_CHANGE_RECORD.EMP_CODE
  is 'Ա������';
comment on column TM_EMP_POST_CHANGE_RECORD.PREVIOUS_POST
  is '��һ��λ';
comment on column TM_EMP_POST_CHANGE_RECORD.CURRENT_POST
  is '��ǰ��λ';
comment on column TM_EMP_POST_CHANGE_RECORD.CHANGE_DATE
  is '���ʱ��';