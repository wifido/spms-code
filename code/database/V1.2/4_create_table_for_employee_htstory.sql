-- Create table
create table TM_OSS_EMPLOYEE_HISTORY
(
  EMP_ID             NUMBER(38) not null,
  EMP_CODE           VARCHAR2(20),
  EMP_NAME           VARCHAR2(500),
  EMP_DUTY_NAME      VARCHAR2(100),
  DEPT_ID            NUMBER(19),
  GROUP_ID           NUMBER(38),
  CREATE_TM          DATE,
  MODIFIED_TM        DATE,
  CREATE_EMP_CODE    VARCHAR2(20),
  MODIFIED_EMP_CODE  VARCHAR2(20),
  WORK_TYPE          NUMBER(1),
  EMAIL              VARCHAR2(100),
  DIMISSION_DT       DATE,
  SF_DATE            DATE,
  EMP_POST_TYPE      VARCHAR2(1),
  IS_HAVE_COMMISSION VARCHAR2(1),
  POSITION_ATTR      VARCHAR2(20),
  DUTY_SERIAL        VARCHAR2(20),
  VERSION_NUMBER     NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table TM_OSS_EMPLOYEE_HISTORY
  is '人员信息历史表';
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_ID
  is '主键ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_CODE
  is '工号（系统自动生成，从100000000开始;内部人员使用人资工号)';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_NAME
  is '姓名';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_DUTY_NAME
  is '职位';
comment on column TM_OSS_EMPLOYEE_HISTORY.DEPT_ID
  is '网点ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.GROUP_ID
  is '小组ID';
comment on column TM_OSS_EMPLOYEE_HISTORY.CREATE_TM
  is '创建时间';
comment on column TM_OSS_EMPLOYEE_HISTORY.MODIFIED_TM
  is '修改时间';
comment on column TM_OSS_EMPLOYEE_HISTORY.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_OSS_EMPLOYEE_HISTORY.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TM_OSS_EMPLOYEE_HISTORY.WORK_TYPE
  is '用工类型(1-非全日制工、2-基地见习生、3-劳务派遣、4-全日制员工、5-实习生、6-外包)';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMAIL
  is '电子邮箱';
comment on column TM_OSS_EMPLOYEE_HISTORY.DIMISSION_DT
  is '离职日期';
comment on column TM_OSS_EMPLOYEE_HISTORY.SF_DATE
  is '入职日期';
comment on column TM_OSS_EMPLOYEE_HISTORY.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员、3-仓管、4-客服）';
comment on column TM_OSS_EMPLOYEE_HISTORY.IS_HAVE_COMMISSION
  is '仓管人员信息 是否参与合算计提 0不参与，1参与';
comment on column TM_OSS_EMPLOYEE_HISTORY.POSITION_ATTR
  is '级别类型（一线、二线）';
comment on column TM_OSS_EMPLOYEE_HISTORY.DUTY_SERIAL
  is '岗位序列';
comment on column TM_OSS_EMPLOYEE_HISTORY.VERSION_NUMBER
  is '版本号';
