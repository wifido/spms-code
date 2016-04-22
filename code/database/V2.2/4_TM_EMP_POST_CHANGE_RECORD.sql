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
  is '员工岗位变更轨迹表';
-- Add comments to the columns 
comment on column TM_EMP_POST_CHANGE_RECORD.EMP_CODE
  is '员工工号';
comment on column TM_EMP_POST_CHANGE_RECORD.PREVIOUS_POST
  is '上一岗位';
comment on column TM_EMP_POST_CHANGE_RECORD.CURRENT_POST
  is '当前岗位';
comment on column TM_EMP_POST_CHANGE_RECORD.CHANGE_DATE
  is '变更时间';