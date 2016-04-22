-- Create table
create table TI_ZTHR_ETL_HR_EMF_TMP
(
  EMP_ID             NUMBER(38) not null,
  EMP_CODE           VARCHAR2(20),
  EMP_NAME           VARCHAR2(500),
  EMP_DUTY_NAME      VARCHAR2(255),
  DEPT_ID            NUMBER(19),
  GROUP_ID           NUMBER(38),
  CREATE_TM          DATE,
  MODIFIED_TM        DATE,
  CREATE_EMP_CODE    VARCHAR2(20),
  MODIFIED_EMP_CODE  VARCHAR2(20),
  WORK_TYPE          NUMBER(2),
  EMAIL              VARCHAR2(100),
  DIMISSION_DT       DATE,
  SF_DATE            DATE,
  EMP_POST_TYPE      VARCHAR2(1),
  IS_HAVE_COMMISSION VARCHAR2(1),
  POSITION_ATTR      VARCHAR2(60),
  DUTY_SERIAL        VARCHAR2(150),
  VERSION_NUMBER     NUMBER,
  PERSON_TYPE        VARCHAR2(90),
  PERSG              VARCHAR2(4),
  PERSG_TXT          VARCHAR2(150),
  PERSK              VARCHAR2(6),
  PERSK_TXT          VARCHAR2(150),
  DATE_FROM          DATE,
  LAST_ZNO           VARCHAR2(36),
  CANCEL_FLAG        VARCHAR2(10),
  DATA_SOURCE        VARCHAR2(1),
  EFFECTIVE_DATE     DATE,
  ORG_ASS_DATE       DATE,
  NET_CODE           VARCHAR2(36)
);
-- Add comments to the table 
comment on table TI_ZTHR_ETL_HR_EMF_TMP
  is '人员基础信息表临时表';
-- Add comments to the columns 
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_ID
  is 'SAP人员信息表ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_CODE
  is '工号（系统自动生成，从100000000开始;内部人员使用人资工号)';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_NAME
  is '姓名';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_DUTY_NAME
  is '职位';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DEPT_ID
  is '网点ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.GROUP_ID
  is '小组ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CREATE_TM
  is '创建时间';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.MODIFIED_TM
  is '修改时间';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CREATE_EMP_CODE
  is '创建人工号';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.WORK_TYPE
  is '用工类型:11-长期合同制、12-劳务派遣、13-业务外包、14-基地见习生、15-退休返聘、21-短期合同制、22-小时工、23-顾

问、24-实习生、25-灵活派遣、26-勤工助学、99-非员工、0-其它、6-外包、8-代理、9-个人承包经营者';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMAIL
  is '电子邮箱';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DIMISSION_DT
  is '离职日期';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.SF_DATE
  is '入职日期';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员、3-仓管、4-客服）';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.IS_HAVE_COMMISSION
  is '仓管人员信息 是否参与合算计提 0不参与，1参与';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.POSITION_ATTR
  is '级别类型（一线、二线）';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DUTY_SERIAL
  is '岗位序列';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.VERSION_NUMBER
  is '版本号';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSON_TYPE
  is '人员类型文本';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSG
  is '员工组:A:全日制用工 C:非全日制用工';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSG_TXT
  is '员工组文本';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSK
  is '员工子组';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSK_TXT
  is '员工子组文本';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DATE_FROM
  is '调入当前网络时间';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.LAST_ZNO
  is '上一网络编码';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CANCEL_FLAG
  is '离职标志';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DATA_SOURCE
  is '数据源类型：1-SPMS;2-SAP';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EFFECTIVE_DATE
  is '生效时间';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.ORG_ASS_DATE
  is '转岗时间';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.NET_CODE
  is '当前网络编码';
-- Create/Recreate indexes 
create index IDX_TI_ZTHR_ETL_HR_EMF_TMP on TI_ZTHR_ETL_HR_EMF_TMP (EMP_CODE);
