-- Create table
create table TT_PB_TRAINING_INFO
(
  ID                NUMBER(38) not null,
  DEPARTMENT_CODE   VARCHAR2(30),
  TRAINING_CODE     VARCHAR2(30),
  EMPLOYEE_CODE     VARCHAR2(30),
  YEARS_MONTH       VARCHAR2(30),
  DAY_OF_MONTH      VARCHAR2(30),
  POST_TYPE         NUMBER(1),
  CREATE_TM         DATE default sysdate,
  MODIFIED_TM       DATE default sysdate,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  DESCRIPTION       VARCHAR2(200)
);
-- Add comments to the table 
comment on table TT_PB_TRAINING_INFO
  is '培训信息表';
-- Add comments to the columns 
comment on column TT_PB_TRAINING_INFO.ID
  is '主键ID';
comment on column TT_PB_TRAINING_INFO.DEPARTMENT_CODE
  is '网点代码';
comment on column TT_PB_TRAINING_INFO.TRAINING_CODE
  is '培训代码';
comment on column TT_PB_TRAINING_INFO.EMPLOYEE_CODE
  is '员工';
comment on column TT_PB_TRAINING_INFO.YEARS_MONTH
  is '培训年月';
comment on column TT_PB_TRAINING_INFO.DAY_OF_MONTH
  is '培训日期（年月日）';
comment on column TT_PB_TRAINING_INFO.POST_TYPE
  is '岗位类型（1代表运作，2代表仓管）';
comment on column TT_PB_TRAINING_INFO.CREATE_TM
  is '创建时间';
comment on column TT_PB_TRAINING_INFO.MODIFIED_TM
  is '修改时间';
comment on column TT_PB_TRAINING_INFO.CREATE_EMP_CODE
  is '创建者工号';
comment on column TT_PB_TRAINING_INFO.MODIFIED_EMP_CODE
  is '修改者工号';
comment on column TT_PB_TRAINING_INFO.DESCRIPTION
  is '描述信息';
