-----------------------------------------------
-- Export file for user PUSHPN               --
-- Created by sfit0505 on 2014/9/15, 9:29:33 --
-----------------------------------------------

spool Initializes tables.log

prompt
prompt Creating table TI_OSS_HR_EMP_INFO
prompt =================================
prompt
create table TI_OSS_HR_EMP_INFO
(
  EMP_ID            NUMBER(18) not null,
  EMP_CODE          VARCHAR2(25) not null,
  EMP_DUTY_NAME     VARCHAR2(50),
  EMP_TYPE_NAME     VARCHAR2(50),
  EMP_NAME          VARCHAR2(500),
  EMP_GENDER        VARCHAR2(4),
  EMP_EMAIL         VARCHAR2(60),
  EMP_MOBILE        VARCHAR2(30),
  EMP_OFFICEPHONE   VARCHAR2(32),
  EMP_STUS          VARCHAR2(32) default '1',
  REGISTER_DT       DATE,
  LOGOUT_DT         DATE,
  EMP_DESC          VARCHAR2(200),
  VALID_FLG         NUMBER(1) default 1,
  DEPT_CODE         VARCHAR2(30),
  CHANGE_ZONE_TM    DATE,
  INNER_FLG         NUMBER(1) default 1,
  PERSON_TYPE       VARCHAR2(50),
  POSITION_NAME     VARCHAR2(200),
  POSITION_STATUS   VARCHAR2(50),
  CREATED_EMP_CODE  VARCHAR2(30) default 'hrs_emp_init',
  CREATED_TM        DATE default sysdate,
  MODIFIED_EMP_CODE VARCHAR2(30),
  MODIFIED_TM       DATE,
  HRS_EMP_ID        VARCHAR2(50),
  JOURNAL_ID        VARCHAR2(200),
  ERRMSG            VARCHAR2(1000),
  BATCH_NUMBER      VARCHAR2(200),
  XML_SIZE          NUMBER(18),
  CANCEL_DATE       VARCHAR2(20),
  SF_DATE           VARCHAR2(20),
  CANCEL_FLAG       VARCHAR2(20),
  DUTY_SERIAL       VARCHAR2(100)
)
;
comment on table TI_OSS_HR_EMP_INFO
  is 'HR系统接口获取(人员表)初始化员工数据：当前在职员工全量+预生效员工数据';
comment on column TI_OSS_HR_EMP_INFO.EMP_ID
  is '职员id';
comment on column TI_OSS_HR_EMP_INFO.EMP_CODE
  is '职员代码（工号）';
comment on column TI_OSS_HR_EMP_INFO.EMP_DUTY_NAME
  is '职员岗位';
comment on column TI_OSS_HR_EMP_INFO.EMP_TYPE_NAME
  is '职员类别';
comment on column TI_OSS_HR_EMP_INFO.EMP_NAME
  is '姓名';
comment on column TI_OSS_HR_EMP_INFO.EMP_GENDER
  is '性别:M=男、F=女';
comment on column TI_OSS_HR_EMP_INFO.EMP_EMAIL
  is '电子邮箱';
comment on column TI_OSS_HR_EMP_INFO.EMP_MOBILE
  is '手机号码';
comment on column TI_OSS_HR_EMP_INFO.EMP_OFFICEPHONE
  is '办公电话';
comment on column TI_OSS_HR_EMP_INFO.EMP_STUS
  is '状态：新增、修改、换网络、启用、注销';
comment on column TI_OSS_HR_EMP_INFO.REGISTER_DT
  is '注册日期';
comment on column TI_OSS_HR_EMP_INFO.LOGOUT_DT
  is '注销日期';
comment on column TI_OSS_HR_EMP_INFO.EMP_DESC
  is '描述';
comment on column TI_OSS_HR_EMP_INFO.VALID_FLG
  is '是否启用 1.是 0.否';
comment on column TI_OSS_HR_EMP_INFO.DEPT_CODE
  is '网络网点编号';
comment on column TI_OSS_HR_EMP_INFO.CHANGE_ZONE_TM
  is '转网络生效时间';
comment on column TI_OSS_HR_EMP_INFO.INNER_FLG
  is '是否内部职工 1.是 0.否';
comment on column TI_OSS_HR_EMP_INFO.PERSON_TYPE
  is '人员类型';
comment on column TI_OSS_HR_EMP_INFO.POSITION_NAME
  is '职位名称';
comment on column TI_OSS_HR_EMP_INFO.POSITION_STATUS
  is '职位状态';
comment on column TI_OSS_HR_EMP_INFO.CREATED_EMP_CODE
  is '创建人员';
comment on column TI_OSS_HR_EMP_INFO.CREATED_TM
  is '创建时间';
comment on column TI_OSS_HR_EMP_INFO.MODIFIED_EMP_CODE
  is '更新人员';
comment on column TI_OSS_HR_EMP_INFO.MODIFIED_TM
  is '更新时间';
comment on column TI_OSS_HR_EMP_INFO.HRS_EMP_ID
  is 'hrs职员id';
comment on column TI_OSS_HR_EMP_INFO.JOURNAL_ID
  is '流水号(处理批次号)';
comment on column TI_OSS_HR_EMP_INFO.ERRMSG
  is '错误信息';
comment on column TI_OSS_HR_EMP_INFO.BATCH_NUMBER
  is '文件批次号';
comment on column TI_OSS_HR_EMP_INFO.XML_SIZE
  is '文件数据量';
comment on column TI_OSS_HR_EMP_INFO.CANCEL_DATE
  is 'HRS离职注销日期';
comment on column TI_OSS_HR_EMP_INFO.SF_DATE
  is 'HRS顺丰入职日期';
comment on column TI_OSS_HR_EMP_INFO.CANCEL_FLAG
  is '离职标识N=否,Y=是';
comment on column TI_OSS_HR_EMP_INFO.DUTY_SERIAL
  is '岗位序列';
alter table TI_OSS_HR_EMP_INFO
  add constraint PK_TI_OSS_HR_EMP_INFO01 primary key (EMP_ID);
create index IDX_TI_OSS_HR_EMP_INFO01 on TI_OSS_HR_EMP_INFO (EMP_CODE);

prompt
prompt Creating table TI_OSS_HR_EMP_INFO_ALTER
prompt =======================================
prompt
create table TI_OSS_HR_EMP_INFO_ALTER
(
  EMP_ALTER_ID      NUMBER(18) not null,
  EMP_CODE          VARCHAR2(25) not null,
  EMP_DUTY_NAME     VARCHAR2(50),
  EMP_TYPE_NAME     VARCHAR2(50),
  EMP_NAME          VARCHAR2(500),
  EMP_GENDER        VARCHAR2(4),
  EMP_EMAIL         VARCHAR2(60),
  EMP_MOBILE        VARCHAR2(30),
  EMP_OFFICEPHONE   VARCHAR2(32),
  EMP_STUS          VARCHAR2(32),
  REGISTER_DT       DATE,
  LOGOUT_DT         DATE,
  EMP_DESC          VARCHAR2(200),
  VALID_FLG         NUMBER(1),
  DEPT_CODE         VARCHAR2(30),
  CHANGE_ZONE_TM    DATE,
  INNER_FLG         NUMBER(1),
  PERSON_TYPE       VARCHAR2(50),
  POSITION_NAME     VARCHAR2(200),
  POSITION_STATUS   VARCHAR2(50),
  CREATED_EMP_CODE  VARCHAR2(30) default 'hrs_emp_one',
  CREATED_TM        DATE default sysdate not null,
  MODIFIED_EMP_CODE VARCHAR2(30),
  MODIFIED_TM       DATE,
  EFFECTIVE_DATE    DATE,
  STATUS            NUMBER(1) default 0 not null,
  JOURNAL_ID        VARCHAR2(200) not null,
  ERRMSG            VARCHAR2(1000),
  BATCH_NUMBER      VARCHAR2(200),
  XML_SIZE          NUMBER(18),
  HRS_EMP_ID        VARCHAR2(50),
  HRS_PERSON_ID     VARCHAR2(50),
  CANCEL_DATE       VARCHAR2(20),
  CANCEL_FLAG       VARCHAR2(20),
  SF_DATE           VARCHAR2(20),
  DUTY_SERIAL       VARCHAR2(100)
)
;
comment on table TI_OSS_HR_EMP_INFO_ALTER
  is 'HR系统接口增量,新员工入职或者员工离职/员工的岗位调动或者部门调动';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_ALTER_ID
  is '职员id';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_CODE
  is '职员代码（工号）';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_DUTY_NAME
  is '职员岗位';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_TYPE_NAME
  is '职员类别';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_NAME
  is '姓名';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_GENDER
  is '性别:男、女';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_EMAIL
  is '电子邮箱';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_MOBILE
  is '手机号码';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_OFFICEPHONE
  is '办公电话';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_STUS
  is '状态：新增、修改、换网络、启用、注销';
comment on column TI_OSS_HR_EMP_INFO_ALTER.REGISTER_DT
  is '注册日期';
comment on column TI_OSS_HR_EMP_INFO_ALTER.LOGOUT_DT
  is '注销日期';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EMP_DESC
  is '描述';
comment on column TI_OSS_HR_EMP_INFO_ALTER.VALID_FLG
  is '是否启用 1.是 0.否';
comment on column TI_OSS_HR_EMP_INFO_ALTER.DEPT_CODE
  is '网络网点编号';
comment on column TI_OSS_HR_EMP_INFO_ALTER.CHANGE_ZONE_TM
  is '转网络生效时间';
comment on column TI_OSS_HR_EMP_INFO_ALTER.INNER_FLG
  is '是否内部职工 1.是 0.否';
comment on column TI_OSS_HR_EMP_INFO_ALTER.PERSON_TYPE
  is '人员类型';
comment on column TI_OSS_HR_EMP_INFO_ALTER.POSITION_NAME
  is '职位名称';
comment on column TI_OSS_HR_EMP_INFO_ALTER.POSITION_STATUS
  is '职位状态';
comment on column TI_OSS_HR_EMP_INFO_ALTER.CREATED_EMP_CODE
  is '创建人员';
comment on column TI_OSS_HR_EMP_INFO_ALTER.CREATED_TM
  is '创建时间';
comment on column TI_OSS_HR_EMP_INFO_ALTER.MODIFIED_EMP_CODE
  is '更新人员';
comment on column TI_OSS_HR_EMP_INFO_ALTER.MODIFIED_TM
  is '更新时间';
comment on column TI_OSS_HR_EMP_INFO_ALTER.EFFECTIVE_DATE
  is '生效日期';
comment on column TI_OSS_HR_EMP_INFO_ALTER.STATUS
  is '数据处理标示(0=未处理)';
comment on column TI_OSS_HR_EMP_INFO_ALTER.JOURNAL_ID
  is '流水号(处理批次号)';
comment on column TI_OSS_HR_EMP_INFO_ALTER.ERRMSG
  is '错误信息';
comment on column TI_OSS_HR_EMP_INFO_ALTER.BATCH_NUMBER
  is '文件批次号';
comment on column TI_OSS_HR_EMP_INFO_ALTER.XML_SIZE
  is '文件数据量';
comment on column TI_OSS_HR_EMP_INFO_ALTER.HRS_EMP_ID
  is 'hrs主键id';
comment on column TI_OSS_HR_EMP_INFO_ALTER.HRS_PERSON_ID
  is 'hrs员工ID';
comment on column TI_OSS_HR_EMP_INFO_ALTER.CANCEL_DATE
  is 'HRS离职注销日期';
comment on column TI_OSS_HR_EMP_INFO_ALTER.CANCEL_FLAG
  is 'HRS离职注销标志';
comment on column TI_OSS_HR_EMP_INFO_ALTER.SF_DATE
  is 'HRS顺丰入职日期';
comment on column TI_OSS_HR_EMP_INFO_ALTER.DUTY_SERIAL
  is '岗位序列';
alter table TI_OSS_HR_EMP_INFO_ALTER
  add constraint PK_TI_OSS_HR_EMP_INFO_ALTER01 primary key (EMP_ALTER_ID);
create index IDX_TI_OSS_HR_EMP_INFO_ALTE01 on TI_OSS_HR_EMP_INFO_ALTER (EMP_CODE);
create index IDX_TI_OSS_HR_EMP_INFO_ALTE02 on TI_OSS_HR_EMP_INFO_ALTER (CREATED_TM);

prompt
prompt Creating table TI_OSS_HR_EMP_NEW_CHANGEDEPT
prompt ===========================================
prompt
create table TI_OSS_HR_EMP_NEW_CHANGEDEPT
(
  EMP_ID            NUMBER(18) not null,
  EMP_CODE          VARCHAR2(25) not null,
  EMP_DUTY_NAME     VARCHAR2(50),
  EMP_TYPE_NAME     VARCHAR2(50),
  EMP_NAME          VARCHAR2(500),
  EMP_GENDER        VARCHAR2(4),
  EMP_EMAIL         VARCHAR2(60),
  EMP_MOBILE        VARCHAR2(30),
  EMP_OFFICEPHONE   VARCHAR2(32),
  EMP_STUS          VARCHAR2(32) default '1',
  REGISTER_DT       DATE,
  LOGOUT_DT         DATE,
  EMP_DESC          VARCHAR2(200),
  VALID_FLG         NUMBER(1) default 1,
  DEPT_CODE         VARCHAR2(30),
  CHANGE_ZONE_TM    DATE,
  INNER_FLG         NUMBER(1) default 1,
  PERSON_TYPE       VARCHAR2(50),
  POSITION_NAME     VARCHAR2(200),
  POSITION_STATUS   VARCHAR2(50),
  CREATED_EMP_CODE  VARCHAR2(30) default 'hrs_emp_init',
  CREATED_TM        DATE default sysdate,
  MODIFIED_EMP_CODE VARCHAR2(30),
  MODIFIED_TM       DATE,
  HRS_EMP_ID        VARCHAR2(50),
  CANCEL_DATE       VARCHAR2(20),
  SF_DATE           VARCHAR2(20),
  CANCEL_FLAG       VARCHAR2(20),
  DEAL_FLAG         NUMBER(1),
  INTERFACE_ID      NUMBER(19)
)
;
comment on table TI_OSS_HR_EMP_NEW_CHANGEDEPT
  is 'HR系统接口获取(人员表)初始化员工数据：当前在职员工全量+预生效员工数据';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_ID
  is '职员id';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_CODE
  is '职员代码（工号）';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_DUTY_NAME
  is '职员岗位';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_TYPE_NAME
  is '职员类别';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_NAME
  is '姓名';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_GENDER
  is '性别:M=男、F=女';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_EMAIL
  is '电子邮箱';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_MOBILE
  is '手机号码';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_OFFICEPHONE
  is '办公电话';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_STUS
  is '状态：新增:1、换网络:2、';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.REGISTER_DT
  is '注册日期';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.LOGOUT_DT
  is '注销日期';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.EMP_DESC
  is '描述';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.VALID_FLG
  is '是否启用 1.是 0.否';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.DEPT_CODE
  is '网络网点编号';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.CHANGE_ZONE_TM
  is '转网络生效时间';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.INNER_FLG
  is '是否内部职工 1.是 0.否';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.PERSON_TYPE
  is '人员类型';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.POSITION_NAME
  is '职位名称';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.POSITION_STATUS
  is '职位状态';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.CREATED_EMP_CODE
  is '创建人员';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.CREATED_TM
  is '创建时间';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.MODIFIED_EMP_CODE
  is '更新人员';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.MODIFIED_TM
  is '更新时间';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.HRS_EMP_ID
  is 'hrs职员id';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.CANCEL_DATE
  is 'HRS离职注销日期';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.SF_DATE
  is 'HRS顺丰入职日期';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.CANCEL_FLAG
  is '离职标识N=否,Y=是';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.DEAL_FLAG
  is '1:已处理0:为处理2：同步错误';
comment on column TI_OSS_HR_EMP_NEW_CHANGEDEPT.INTERFACE_ID
  is 'TI_OSS_HR_EMP_INFO_ALTER判断ID';
alter table TI_OSS_HR_EMP_NEW_CHANGEDEPT
  add constraint PK_TI_OSS_HR_CHANGEDEPT primary key (EMP_ID);
create index IDX_TI_OSS__NEW_CHANGEDEPT01 on TI_OSS_HR_EMP_NEW_CHANGEDEPT (EMP_CODE);
create index IDX_TI_OSS__NEW_CHANGEDEPT02 on TI_OSS_HR_EMP_NEW_CHANGEDEPT (EMP_STUS);

prompt
prompt Creating table TL_EXCEPTION_LOG
prompt ===============================
prompt
create table TL_EXCEPTION_LOG
(
  SEQ_NO         NUMBER(20) not null,
  PROCEDURE_NAME VARCHAR2(120),
  EXCEPTION_TM   DATE,
  EXCEPTION_CODE VARCHAR2(60),
  EXCEPTION_DESC VARCHAR2(1000),
  EXCEPTION_REMK VARCHAR2(600),
  LINE_NO        NUMBER(5),
  PACKAGE_NAME   VARCHAR2(120),
  CALL_SNO       NUMBER
)
;
comment on table TL_EXCEPTION_LOG
  is '异常日志表';
comment on column TL_EXCEPTION_LOG.SEQ_NO
  is '异常发生时有关流水号的值';
comment on column TL_EXCEPTION_LOG.PROCEDURE_NAME
  is '过程名称';
comment on column TL_EXCEPTION_LOG.EXCEPTION_TM
  is '异常发生时间';
comment on column TL_EXCEPTION_LOG.EXCEPTION_CODE
  is '异常代码';
comment on column TL_EXCEPTION_LOG.EXCEPTION_DESC
  is '异常描述';
comment on column TL_EXCEPTION_LOG.EXCEPTION_REMK
  is '异常备注:BEGIN表示开始,END表示结束,ERROR表示失败';
comment on column TL_EXCEPTION_LOG.LINE_NO
  is '发生异常的位置(行号)';
comment on column TL_EXCEPTION_LOG.PACKAGE_NAME
  is '包名称';
comment on column TL_EXCEPTION_LOG.CALL_SNO
  is '调用序号';
alter table TL_EXCEPTION_LOG
  add constraint PK_TL_EXCEPTION_LOG_SEQ_NO primary key (SEQ_NO);

prompt
prompt Creating table TL_OSS_SYS_LOG
prompt =============================
prompt
create table TL_OSS_SYS_LOG
(
  LOG_NAME    VARCHAR2(500),
  LOG_CONTENT NVARCHAR2(2000),
  LOG_TM      DATE,
  LOG_USER    VARCHAR2(20)
)
;
comment on table TL_OSS_SYS_LOG
  is '系统日志';
comment on column TL_OSS_SYS_LOG.LOG_NAME
  is '过程名称';
comment on column TL_OSS_SYS_LOG.LOG_CONTENT
  is '错误信息';
comment on column TL_OSS_SYS_LOG.LOG_TM
  is '发生时间';
comment on column TL_OSS_SYS_LOG.LOG_USER
  is '使用者';
create index IDX_OSS_SYS_LOG_1 on TL_OSS_SYS_LOG (LOG_TM);

prompt
prompt Creating table TM_DEMO
prompt ======================
prompt
create table TM_DEMO
(
  DEMO_ID           NUMBER(38) not null,
  DEMO_NAME         VARCHAR2(60),
  DEMO_CODE         VARCHAR2(15),
  REMARK            VARCHAR2(90),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20)
)
;
comment on table TM_DEMO
  is 'Demo练习表';
alter table TM_DEMO
  add constraint PK_TM_DEMOO primary key (DEMO_ID);

prompt
prompt Creating table TM_DEPARTMENT
prompt ============================
prompt
create table TM_DEPARTMENT
(
  DEPT_ID           NUMBER(18) not null,
  TYPE_CODE         VARCHAR2(30),
  DEPT_NAME         VARCHAR2(100),
  DEPT_CODE         VARCHAR2(30),
  DEPT_DESC         VARCHAR2(600),
  DIVISION_CODE     VARCHAR2(30),
  AREA_CODE         VARCHAR2(30),
  HQ_CODE           VARCHAR2(30),
  VALID_DT          DATE,
  DELETE_FLG        NUMBER(1),
  INVALID_TM        DATE,
  PARENT_DEPT_CODE  VARCHAR2(30),
  CREATED_EMP_CODE  VARCHAR2(30),
  CREATED_TM        DATE,
  MODIFIED_EMP_CODE VARCHAR2(30),
  MODIFIED_TM       DATE
)
;
comment on table TM_DEPARTMENT
  is '网络网点信息';
comment on column TM_DEPARTMENT.DEPT_ID
  is '网点id';
comment on column TM_DEPARTMENT.TYPE_CODE
  is '机构类型';
comment on column TM_DEPARTMENT.DEPT_NAME
  is '机构名称';
comment on column TM_DEPARTMENT.DEPT_CODE
  is '网络网点代码';
comment on column TM_DEPARTMENT.DEPT_DESC
  is '描述';
comment on column TM_DEPARTMENT.DIVISION_CODE
  is '分点部代码';
comment on column TM_DEPARTMENT.AREA_CODE
  is '区部代码';
comment on column TM_DEPARTMENT.HQ_CODE
  is '经营本部代码';
comment on column TM_DEPARTMENT.VALID_DT
  is '有效日期';
comment on column TM_DEPARTMENT.DELETE_FLG
  is '删除标识 1.是（表示被删除） 0.否';
comment on column TM_DEPARTMENT.INVALID_TM
  is '失效时间';
comment on column TM_DEPARTMENT.PARENT_DEPT_CODE
  is '上级部门编码';
comment on column TM_DEPARTMENT.CREATED_EMP_CODE
  is '创建人员';
comment on column TM_DEPARTMENT.CREATED_TM
  is '创建时间';
comment on column TM_DEPARTMENT.MODIFIED_EMP_CODE
  is '更新人员';
comment on column TM_DEPARTMENT.MODIFIED_TM
  is '更新时间';
alter table TM_DEPARTMENT
  add constraint PK_TM_DEPARTMENT primary key (DEPT_ID);
create index IDX_DEPT_CODE on TM_DEPARTMENT (DEPT_CODE);
create index IDX_PARENT_DEPT_CODE on TM_DEPARTMENT (PARENT_DEPT_CODE);

prompt
prompt Creating table TM_EMPLOYEE
prompt ==========================
prompt
create table TM_EMPLOYEE
(
  EMP_ID            NUMBER(18) not null,
  EMP_CODE          VARCHAR2(25),
  EMP_DUTY_NAME     VARCHAR2(50),
  EMP_TYPE_NAME     VARCHAR2(50),
  EMP_NAME          VARCHAR2(500),
  EMP_GENDER        VARCHAR2(4),
  EMP_EMAIL         VARCHAR2(60),
  EMP_MOBILE        VARCHAR2(30),
  EMP_OFFICEPHONE   VARCHAR2(32),
  EMP_STUS          VARCHAR2(32),
  REGISTER_DT       DATE,
  LOGOUT_DT         DATE,
  EMP_DESC          VARCHAR2(200),
  VALID_FLG         NUMBER(1),
  DEPT_CODE         VARCHAR2(30) not null,
  CHANGE_ZONE_TM    DATE,
  INNER_FLG         NUMBER(1),
  CREATED_EMP_CODE  VARCHAR2(30),
  CREATED_TM        DATE,
  MODIFIED_EMP_CODE VARCHAR2(30),
  MODIFIED_TM       DATE
)
;
comment on table TM_EMPLOYEE
  is '职员表';
comment on column TM_EMPLOYEE.EMP_ID
  is '职员id';
comment on column TM_EMPLOYEE.EMP_CODE
  is '职员代码（工号）';
comment on column TM_EMPLOYEE.EMP_DUTY_NAME
  is '职员岗位';
comment on column TM_EMPLOYEE.EMP_TYPE_NAME
  is '职员类别';
comment on column TM_EMPLOYEE.EMP_NAME
  is '姓名';
comment on column TM_EMPLOYEE.EMP_GENDER
  is '性别:男、女';
comment on column TM_EMPLOYEE.EMP_EMAIL
  is '电子邮箱';
comment on column TM_EMPLOYEE.EMP_MOBILE
  is '手机号码';
comment on column TM_EMPLOYEE.EMP_OFFICEPHONE
  is '办公电话';
comment on column TM_EMPLOYEE.EMP_STUS
  is '状态：新增、修改、换网络、启用、注销';
comment on column TM_EMPLOYEE.REGISTER_DT
  is '注册日期';
comment on column TM_EMPLOYEE.LOGOUT_DT
  is '注销日期';
comment on column TM_EMPLOYEE.EMP_DESC
  is '描述';
comment on column TM_EMPLOYEE.VALID_FLG
  is '是否启用 1.是 0.否';
comment on column TM_EMPLOYEE.DEPT_CODE
  is '网络网点编号';
comment on column TM_EMPLOYEE.CHANGE_ZONE_TM
  is '转网络生效时间';
comment on column TM_EMPLOYEE.INNER_FLG
  is '是否内部职工 1.是 0.否';
comment on column TM_EMPLOYEE.CREATED_EMP_CODE
  is '创建人员';
comment on column TM_EMPLOYEE.CREATED_TM
  is '创建时间';
comment on column TM_EMPLOYEE.MODIFIED_EMP_CODE
  is '更新人员';
comment on column TM_EMPLOYEE.MODIFIED_TM
  is '更新时间';
alter table TM_EMPLOYEE
  add constraint PK_TM_EMPLOYEE primary key (EMP_ID);
create index IDX_EMP_CODE on TM_EMPLOYEE (EMP_CODE);
create index IDX_EMP_DEPT_CODE on TM_EMPLOYEE (DEPT_CODE);

prompt
prompt Creating table TM_PB_GROUP_INFO
prompt ===============================
prompt
create table TM_PB_GROUP_INFO
(
  GROUP_ID          NUMBER(38) not null,
  GROUP_NAME        VARCHAR2(60),
  GROUP_CODE        VARCHAR2(50),
  DEPT_ID           NUMBER(19),
  DISABLE_DT        DATE,
  REMARK            VARCHAR2(90),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20)
)
;
comment on table TM_PB_GROUP_INFO
  is '小组基础表';
comment on column TM_PB_GROUP_INFO.GROUP_ID
  is '主键';
comment on column TM_PB_GROUP_INFO.GROUP_NAME
  is '小组名称';
comment on column TM_PB_GROUP_INFO.GROUP_CODE
  is '小组代码';
comment on column TM_PB_GROUP_INFO.DEPT_ID
  is '网点ID';
comment on column TM_PB_GROUP_INFO.DISABLE_DT
  is '失效日期';
comment on column TM_PB_GROUP_INFO.REMARK
  is '备注信息';
comment on column TM_PB_GROUP_INFO.CREATE_TM
  is '创建时间';
comment on column TM_PB_GROUP_INFO.MODIFIED_TM
  is '修改时间';
comment on column TM_PB_GROUP_INFO.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_PB_GROUP_INFO.MODIFIED_EMP_CODE
  is '修改人工号';
alter table TM_PB_GROUP_INFO
  add constraint PK_TM_PB_GROUP_INFO primary key (GROUP_ID);

prompt
prompt Creating table TM_OSS_EMPLOYEE
prompt ==============================
prompt
create table TM_OSS_EMPLOYEE
(
  EMP_ID            NUMBER(38) not null,
  EMP_CODE          VARCHAR2(20),
  EMP_NAME          VARCHAR2(500),
  EMP_DUTY_NAME     VARCHAR2(100),
  DEPT_ID           NUMBER(19),
  GROUP_ID          NUMBER(38),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  WORK_TYPE         NUMBER(1),
  EMAIL             VARCHAR2(100),
  DIMISSION_DT      DATE,
  SF_DATE           DATE,
  EMP_POST_TYPE     VARCHAR2(1)
)
;
comment on table TM_OSS_EMPLOYEE
  is '人员基础信息表';
comment on column TM_OSS_EMPLOYEE.EMP_ID
  is '主键ID';
comment on column TM_OSS_EMPLOYEE.EMP_CODE
  is '工号（系统自动生成，从100000000开始;内部人员使用人资工号)';
comment on column TM_OSS_EMPLOYEE.EMP_NAME
  is '姓名';
comment on column TM_OSS_EMPLOYEE.EMP_DUTY_NAME
  is '职位';
comment on column TM_OSS_EMPLOYEE.DEPT_ID
  is '网点ID';
comment on column TM_OSS_EMPLOYEE.GROUP_ID
  is '小组ID';
comment on column TM_OSS_EMPLOYEE.CREATE_TM
  is '创建时间';
comment on column TM_OSS_EMPLOYEE.MODIFIED_TM
  is '修改时间';
comment on column TM_OSS_EMPLOYEE.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_OSS_EMPLOYEE.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TM_OSS_EMPLOYEE.WORK_TYPE
  is '用工类型(1-非全日制工、2-基地见习生、3-劳务派遣、4-全日制员工、5-实习生、6-外包)';
comment on column TM_OSS_EMPLOYEE.EMAIL
  is '电子邮箱';
comment on column TM_OSS_EMPLOYEE.DIMISSION_DT
  is '离职日期';
comment on column TM_OSS_EMPLOYEE.SF_DATE
  is '入职日期';
comment on column TM_OSS_EMPLOYEE.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员、3-仓管、4-客服）';
alter table TM_OSS_EMPLOYEE
  add constraint PK_TM_OSS_EMPLOYEE primary key (EMP_ID);
alter table TM_OSS_EMPLOYEE
  add constraint FK_EMPLOYEE_3 foreign key (GROUP_ID)
  references TM_PB_GROUP_INFO (GROUP_ID);
create unique index UK_TM_OSS_EMPLOYEE on TM_OSS_EMPLOYEE (EMP_CODE);

prompt
prompt Creating table TM_PB_PROCESS_INFO
prompt =================================
prompt
create table TM_PB_PROCESS_INFO
(
  PROCESS_ID              NUMBER(38) not null,
  DEPT_ID                 NUMBER(19),
  PROCESS_CODE            VARCHAR2(18),
  PROCESS_NAME            VARCHAR2(45),
  PROCESS_AREA            VARCHAR2(150),
  PROCESS_TOOL            VARCHAR2(45),
  ESTIMATE_VALUE          NUMBER(3,1),
  INTENSITY_VALUE         NUMBER(3,1),
  SKILL_VALUE             NUMBER(3,1),
  DIFFICULTY_VALUE        NUMBER(4,2),
  DIFFICULTY_MODIFY_VALUE NUMBER(3,1),
  CREATE_TM               DATE,
  MODIFIED_TM             DATE,
  MODIFIED_EMP_CODE       VARCHAR2(20),
  CREATE_EMP_CODE         VARCHAR2(20),
  STATUS                  NUMBER(1)
)
;
comment on table TM_PB_PROCESS_INFO
  is '工序基础表';
comment on column TM_PB_PROCESS_INFO.PROCESS_ID
  is '工序ID';
comment on column TM_PB_PROCESS_INFO.DEPT_ID
  is '网点ID；导入时值为总部ID';
comment on column TM_PB_PROCESS_INFO.PROCESS_CODE
  is '工序代码';
comment on column TM_PB_PROCESS_INFO.PROCESS_NAME
  is '工序名称';
comment on column TM_PB_PROCESS_INFO.PROCESS_AREA
  is '工序区域';
comment on column TM_PB_PROCESS_INFO.PROCESS_TOOL
  is '工序使用工具';
comment on column TM_PB_PROCESS_INFO.ESTIMATE_VALUE
  is '判断需求值';
comment on column TM_PB_PROCESS_INFO.INTENSITY_VALUE
  is '强度需求值';
comment on column TM_PB_PROCESS_INFO.SKILL_VALUE
  is '技能需求值';
comment on column TM_PB_PROCESS_INFO.DIFFICULTY_VALUE
  is '难度系数值';
comment on column TM_PB_PROCESS_INFO.DIFFICULTY_MODIFY_VALUE
  is '难度修正值（中转场才有）';
comment on column TM_PB_PROCESS_INFO.CREATE_TM
  is '创建时间';
comment on column TM_PB_PROCESS_INFO.MODIFIED_TM
  is '修改时间';
comment on column TM_PB_PROCESS_INFO.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TM_PB_PROCESS_INFO.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_PB_PROCESS_INFO.STATUS
  is '1:有效；0无效（表示之前有用过，但现在不用而无法删除)';
alter table TM_PB_PROCESS_INFO
  add constraint PK_TM_PB_PROCESS_INFO primary key (PROCESS_ID);

prompt
prompt Creating table TM_PB_SCHEDULE_BASE_INFO
prompt =======================================
prompt
create table TM_PB_SCHEDULE_BASE_INFO
(
  SCHEDULE_ID       NUMBER(38) not null,
  SCHEDULE_CODE     VARCHAR2(9),
  SCHEDULE_NAME     VARCHAR2(30),
  DEPT_ID           NUMBER(19),
  START1_TIME       VARCHAR2(10),
  END1_TIME         VARCHAR2(10),
  START2_TIME       VARCHAR2(5),
  END2_TIME         VARCHAR2(5),
  START3_TIME       VARCHAR2(5),
  END3_TIME         VARCHAR2(5),
  ENABLE_DT         DATE,
  DISABLE_DT        DATE,
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20)
)
;
comment on table TM_PB_SCHEDULE_BASE_INFO
  is '班别基础信息表';
comment on column TM_PB_SCHEDULE_BASE_INFO.SCHEDULE_ID
  is '主键ID';
comment on column TM_PB_SCHEDULE_BASE_INFO.SCHEDULE_CODE
  is '班别代码';
comment on column TM_PB_SCHEDULE_BASE_INFO.SCHEDULE_NAME
  is '班别名称';
comment on column TM_PB_SCHEDULE_BASE_INFO.DEPT_ID
  is '网点ID';
comment on column TM_PB_SCHEDULE_BASE_INFO.START1_TIME
  is '开始时间一';
comment on column TM_PB_SCHEDULE_BASE_INFO.END1_TIME
  is '结束时间一';
comment on column TM_PB_SCHEDULE_BASE_INFO.START2_TIME
  is '开始时间二';
comment on column TM_PB_SCHEDULE_BASE_INFO.END2_TIME
  is '结束时间二';
comment on column TM_PB_SCHEDULE_BASE_INFO.START3_TIME
  is '开始时间三';
comment on column TM_PB_SCHEDULE_BASE_INFO.END3_TIME
  is '结束时间三';
comment on column TM_PB_SCHEDULE_BASE_INFO.ENABLE_DT
  is '生效日期';
comment on column TM_PB_SCHEDULE_BASE_INFO.DISABLE_DT
  is '失效日期';
comment on column TM_PB_SCHEDULE_BASE_INFO.CREATE_TM
  is '创建时间';
comment on column TM_PB_SCHEDULE_BASE_INFO.MODIFIED_TM
  is '修改时间';
comment on column TM_PB_SCHEDULE_BASE_INFO.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_PB_SCHEDULE_BASE_INFO.MODIFIED_EMP_CODE
  is '修改人工号';
alter table TM_PB_SCHEDULE_BASE_INFO
  add constraint PK_TM_PB_SCHEDULE_BASE_INFO primary key (SCHEDULE_ID);
create unique index UK1_TM_PB_SCHEDULE_BASE_INFO on TM_PB_SCHEDULE_BASE_INFO (SCHEDULE_NAME);

prompt
prompt Creating table TM_VALIDATE_EMP
prompt ==============================
prompt
create table TM_VALIDATE_EMP
(
  EMP_CODE        VARCHAR2(50),
  YM              VARCHAR2(50),
  DEPT_ID         NUMBER(19),
  CREATE_EMP_CODE VARCHAR2(30),
  VERSION         NUMBER(5)
)
;
comment on table TM_VALIDATE_EMP
  is '待自动排班和工序的临时表';
comment on column TM_VALIDATE_EMP.EMP_CODE
  is '工号';
comment on column TM_VALIDATE_EMP.YM
  is '月份';
comment on column TM_VALIDATE_EMP.DEPT_ID
  is '部门ID';
comment on column TM_VALIDATE_EMP.CREATE_EMP_CODE
  is '创建人工号';
comment on column TM_VALIDATE_EMP.VERSION
  is '版本号';

prompt
prompt Creating table TS_USER
prompt ======================
prompt
create table TS_USER
(
  USER_ID         NUMBER(18) not null,
  USERNAME        VARCHAR2(30),
  PASSWORD        VARCHAR2(100),
  STATUS          VARCHAR2(30),
  USED_TM         DATE,
  UNUSED_TM       DATE,
  DEPT_ID         NUMBER(18) not null,
  EMP_ID          NUMBER(18) not null,
  TYPE_CODE       VARCHAR2(18),
  DATA_RIGHT_FLG  NUMBER(1),
  PWD_MODIFIED_TM DATE,
  CREATE_EMP      VARCHAR2(16),
  CREATE_TM       DATE,
  MODIFIED_EMP    VARCHAR2(16),
  MODIFIED_TM     DATE
)
;
comment on table TS_USER
  is '用户表';
comment on column TS_USER.USER_ID
  is '用户id';
comment on column TS_USER.USERNAME
  is '用户名';
comment on column TS_USER.PASSWORD
  is '密码';
comment on column TS_USER.STATUS
  is '状态';
comment on column TS_USER.USED_TM
  is '启用时间';
comment on column TS_USER.UNUSED_TM
  is '停用时间';
comment on column TS_USER.DEPT_ID
  is '网点ID（引用网点部门表ID）';
comment on column TS_USER.EMP_ID
  is '员工id';
comment on column TS_USER.TYPE_CODE
  is '用户类型编码 1-内部职员 0-外部职员';
comment on column TS_USER.DATA_RIGHT_FLG
  is '是否设置数据权限 0-否 1-是';
comment on column TS_USER.PWD_MODIFIED_TM
  is '密码修改时间';
comment on column TS_USER.CREATE_EMP
  is '创建人员';
comment on column TS_USER.CREATE_TM
  is '创建时间';
comment on column TS_USER.MODIFIED_EMP
  is '更新人员';
comment on column TS_USER.MODIFIED_TM
  is '更新时间';
alter table TS_USER
  add constraint PK_TS_USER_ID primary key (USER_ID);
alter table TS_USER
  add constraint UNI_TS_USER_NAME unique (USERNAME);

prompt
prompt Creating table TS_ACCREDIT_DEPT
prompt ===============================
prompt
create table TS_ACCREDIT_DEPT
(
  DEPT_ID    NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  CREATE_EMP VARCHAR2(16),
  CREATE_TM  DATE,
  UPDATE_EMP VARCHAR2(16),
  UPDATE_TM  DATE
)
;
comment on table TS_ACCREDIT_DEPT
  is '授权网点';
comment on column TS_ACCREDIT_DEPT.DEPT_ID
  is '网点id';
comment on column TS_ACCREDIT_DEPT.USER_ID
  is '用户id';
comment on column TS_ACCREDIT_DEPT.CREATE_EMP
  is '创建人员';
comment on column TS_ACCREDIT_DEPT.CREATE_TM
  is '创建时间';
comment on column TS_ACCREDIT_DEPT.UPDATE_EMP
  is '更新人员';
comment on column TS_ACCREDIT_DEPT.UPDATE_TM
  is '更新时间';
alter table TS_ACCREDIT_DEPT
  add constraint PK_TS_ACCREDIT_DEPT primary key (USER_ID, DEPT_ID);
alter table TS_ACCREDIT_DEPT
  add constraint FK_TS_ACCREDIT_DEPT_DEPT foreign key (DEPT_ID)
  references TM_DEPARTMENT (DEPT_ID);
alter table TS_ACCREDIT_DEPT
  add constraint FK_TS_ACCREDIT_DEPT_USER foreign key (USER_ID)
  references TS_USER (USER_ID);

prompt
prompt Creating table TS_ROLE
prompt ======================
prompt
create table TS_ROLE
(
  ROLE_ID        NUMBER(18) not null,
  ROLE_NAME      VARCHAR2(100),
  ROLE_DESC      VARCHAR2(600),
  USED_TM        DATE,
  UNUSED_TM      DATE,
  ROLE_TYPE_CODE VARCHAR2(18),
  SYS_INIT_FLG   NUMBER(6),
  EMP_DUTY       VARCHAR2(32),
  CREATE_EMP     VARCHAR2(16),
  CREATE_TM      DATE,
  UPDATE_EMP     VARCHAR2(16),
  UPDATE_TM      DATE
)
;
comment on table TS_ROLE
  is '角色表';
comment on column TS_ROLE.ROLE_ID
  is '角色id';
comment on column TS_ROLE.ROLE_NAME
  is '角色名';
comment on column TS_ROLE.ROLE_DESC
  is '描述信息';
comment on column TS_ROLE.USED_TM
  is '生效时间';
comment on column TS_ROLE.UNUSED_TM
  is '失效时间';
comment on column TS_ROLE.ROLE_TYPE_CODE
  is '角色类型';
comment on column TS_ROLE.SYS_INIT_FLG
  is '是否系统内置 0-否 1-是';
comment on column TS_ROLE.EMP_DUTY
  is '关联岗位';
comment on column TS_ROLE.CREATE_EMP
  is '创建人员';
comment on column TS_ROLE.CREATE_TM
  is '创建时间';
comment on column TS_ROLE.UPDATE_EMP
  is '更新人员';
comment on column TS_ROLE.UPDATE_TM
  is '更新时间';
alter table TS_ROLE
  add constraint PK_TS_ROLE primary key (ROLE_ID);

prompt
prompt Creating table TS_ACCREDIT_ROLE
prompt ===============================
prompt
create table TS_ACCREDIT_ROLE
(
  ROLE_ID    NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  CREATE_EMP VARCHAR2(16),
  CREATE_TM  DATE,
  UPDATE_EMP VARCHAR2(16),
  UPDATE_TM  DATE
)
;
comment on table TS_ACCREDIT_ROLE
  is '授权角色';
comment on column TS_ACCREDIT_ROLE.ROLE_ID
  is '角色id';
comment on column TS_ACCREDIT_ROLE.USER_ID
  is '用户id';
comment on column TS_ACCREDIT_ROLE.CREATE_EMP
  is '创建人员';
comment on column TS_ACCREDIT_ROLE.CREATE_TM
  is '创建时间';
comment on column TS_ACCREDIT_ROLE.UPDATE_EMP
  is '更新人员';
comment on column TS_ACCREDIT_ROLE.UPDATE_TM
  is '更新时间';
alter table TS_ACCREDIT_ROLE
  add constraint PK_TS_ACCREDIT_ROLE primary key (ROLE_ID, USER_ID);
alter table TS_ACCREDIT_ROLE
  add constraint FK_ACCREDIT_ROLE_ROLE foreign key (ROLE_ID)
  references TS_ROLE (ROLE_ID);
alter table TS_ACCREDIT_ROLE
  add constraint FK_ACCREDIT_ROLE_USER foreign key (USER_ID)
  references TS_USER (USER_ID);

prompt
prompt Creating table TS_ACCREDIT_USER
prompt ===============================
prompt
create table TS_ACCREDIT_USER
(
  ACCREDIT_ID NUMBER(18) not null,
  OPERATER_ID NUMBER(18) not null,
  OPERATE_TM  DATE not null,
  USER_ID     NUMBER(18) not null,
  USED_TM     DATE not null,
  TYPE_CODE   VARCHAR2(30) not null,
  CREATE_EMP  VARCHAR2(16),
  CREATE_TM   DATE,
  UPDATE_EMP  VARCHAR2(16),
  UPDATE_TM   DATE
)
;
comment on table TS_ACCREDIT_USER
  is '权限钥匙';
comment on column TS_ACCREDIT_USER.ACCREDIT_ID
  is '钥匙id';
comment on column TS_ACCREDIT_USER.OPERATER_ID
  is '操作人员';
comment on column TS_ACCREDIT_USER.OPERATE_TM
  is '操作时间';
comment on column TS_ACCREDIT_USER.USER_ID
  is '授权人员';
comment on column TS_ACCREDIT_USER.USED_TM
  is '有效时间';
comment on column TS_ACCREDIT_USER.TYPE_CODE
  is '管理员类型 0-子系统管理员,1- 区域管理员,2- 外包管理员';
comment on column TS_ACCREDIT_USER.CREATE_EMP
  is '创建人员';
comment on column TS_ACCREDIT_USER.CREATE_TM
  is '创建时间';
comment on column TS_ACCREDIT_USER.UPDATE_EMP
  is '更新人员';
comment on column TS_ACCREDIT_USER.UPDATE_TM
  is '更新时间';
alter table TS_ACCREDIT_USER
  add constraint PK_TS_ACCREDIT_USER primary key (ACCREDIT_ID);
alter table TS_ACCREDIT_USER
  add constraint FK_ACCREDIT_USER_USER foreign key (USER_ID)
  references TS_USER (USER_ID) on delete set null;

prompt
prompt Creating table TS_MODULE
prompt ========================
prompt
create table TS_MODULE
(
  MODULE_ID   NUMBER(18) not null,
  PARENT_ID   NUMBER(18),
  MODULE_NAME VARCHAR2(100) not null,
  MODULE_CODE VARCHAR2(100),
  MODULE_DESC VARCHAR2(600),
  MODULE_ICON VARCHAR2(300),
  MODULE_TYPE NUMBER(1),
  APP_TYPE    NUMBER(1),
  ACTION_URL  VARCHAR2(300),
  SORT        NUMBER(3),
  BUNDLE_ID   NUMBER(11),
  HELP_URL    VARCHAR2(300)
)
;
comment on table TS_MODULE
  is '模块（功能）表';
comment on column TS_MODULE.MODULE_ID
  is '模块id';
comment on column TS_MODULE.PARENT_ID
  is '父模块id';
comment on column TS_MODULE.MODULE_NAME
  is '名称';
comment on column TS_MODULE.MODULE_CODE
  is '代码';
comment on column TS_MODULE.MODULE_DESC
  is '描述';
comment on column TS_MODULE.MODULE_ICON
  is '图标';
comment on column TS_MODULE.MODULE_TYPE
  is '类型 1项目根类型 2子系统 4菜单目录 5插件包 6面板 7按钮，文本框等';
comment on column TS_MODULE.APP_TYPE
  is '所属子系统 1.web 2.gui';
comment on column TS_MODULE.ACTION_URL
  is '请求链接';
comment on column TS_MODULE.SORT
  is '排序序号';
comment on column TS_MODULE.BUNDLE_ID
  is '插件包ID';
comment on column TS_MODULE.HELP_URL
  is '帮助链接';
alter table TS_MODULE
  add constraint PK_TS_MODULE primary key (MODULE_ID);
alter table TS_MODULE
  add constraint FK_TS_MODULE foreign key (PARENT_ID)
  references TS_MODULE (MODULE_ID) on delete cascade;

prompt
prompt Creating table TS_OSS_DATACONTROL
prompt =================================
prompt
create table TS_OSS_DATACONTROL
(
  ID      NUMBER(19) not null,
  TASK    NUMBER(1),
  STARTTM DATE,
  ENDTM   DATE,
  STATE   NUMBER(1),
  TIMES   NUMBER(1) default 0
)
;
comment on table TS_OSS_DATACONTROL
  is '接口及邮件发送控制表';
comment on column TS_OSS_DATACONTROL.ID
  is '主键ID';
comment on column TS_OSS_DATACONTROL.TASK
  is '任务编号1：邮件发送；2：接口同步';
comment on column TS_OSS_DATACONTROL.STARTTM
  is '任务开始时间';
comment on column TS_OSS_DATACONTROL.ENDTM
  is '任务结束时间';
comment on column TS_OSS_DATACONTROL.STATE
  is '任务状态0:发送中1：已经完成';
comment on column TS_OSS_DATACONTROL.TIMES
  is '发送次数,默认为0';
alter table TS_OSS_DATACONTROL
  add constraint PK_TS_OSS_DATACONTROL primary key (ID);

prompt
prompt Creating table TS_OSS_ESB_BIG_FILE_RESEND
prompt =========================================
prompt
create table TS_OSS_ESB_BIG_FILE_RESEND
(
  RESEND_ID        NUMBER(18) not null,
  CREATED_TM       DATE default sysdate not null,
  STATE            NUMBER(2),
  SYSTEM_ID        VARCHAR2(100),
  DATA_TYPE        VARCHAR2(100),
  SELF_SYSTEM_ID   VARCHAR2(100),
  TIME_STAMP       VARCHAR2(100),
  JOURNAL_ID       VARCHAR2(200),
  MD5_CODE         VARCHAR2(400),
  REMOTE_PATH      VARCHAR2(400),
  REMOTE_FILE_NAME VARCHAR2(400),
  IS_ZIP           VARCHAR2(10),
  MODIFIED_TM      DATE,
  INTERVAL_TIME    NUMBER(2) default 20
)
;
comment on table TS_OSS_ESB_BIG_FILE_RESEND
  is 'ESB数据重发请求(BigFileResendData)参数配置表';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.RESEND_ID
  is 'ID';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.CREATED_TM
  is '创建时间';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.STATE
  is '-9=空请求,-1=开始下载,0=开始解析,1=成功,2=解析失败,3=下载失败,4=已重发,5=(存储过程)已开始处理完数据,6=(存储过程)已成功处理完数据';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.SYSTEM_ID
  is '源系统ID';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.DATA_TYPE
  is '数据类型';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.SELF_SYSTEM_ID
  is '自己系统ID';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.TIME_STAMP
  is '获取到的数据时间戳';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.JOURNAL_ID
  is '流水号，数据重发';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.MD5_CODE
  is 'MD5值';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.REMOTE_PATH
  is '远程文件路径';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.REMOTE_FILE_NAME
  is '远程文件名称';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.IS_ZIP
  is '是否压缩';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.MODIFIED_TM
  is '修改时间';
comment on column TS_OSS_ESB_BIG_FILE_RESEND.INTERVAL_TIME
  is '重发请求间隔时长(单位分钟)';
alter table TS_OSS_ESB_BIG_FILE_RESEND
  add constraint PK_TS_OSS_ESB_BIG_FILE_RESE01 primary key (RESEND_ID);
create index IDX_TS_OSS_ESB_BIG_FILE_RES01 on TS_OSS_ESB_BIG_FILE_RESEND (CREATED_TM);

prompt
prompt Creating table TS_ROLE_MODULE
prompt =============================
prompt
create table TS_ROLE_MODULE
(
  ROLE_ID    NUMBER(18) not null,
  MODULE_ID  NUMBER(18) not null,
  CREATE_TM  DATE,
  CREATE_EMP VARCHAR2(16),
  UPDATE_EMP VARCHAR2(16),
  UPDATE_TM  DATE
)
;
comment on table TS_ROLE_MODULE
  is '角色功能表';
comment on column TS_ROLE_MODULE.ROLE_ID
  is '角色id';
comment on column TS_ROLE_MODULE.MODULE_ID
  is '功能id';
comment on column TS_ROLE_MODULE.CREATE_TM
  is '创建时间';
comment on column TS_ROLE_MODULE.CREATE_EMP
  is '创建人员';
comment on column TS_ROLE_MODULE.UPDATE_EMP
  is '更新人员';
comment on column TS_ROLE_MODULE.UPDATE_TM
  is '更新时间';
alter table TS_ROLE_MODULE
  add constraint PK_TS_ROLE_MODULE primary key (ROLE_ID, MODULE_ID);
alter table TS_ROLE_MODULE
  add constraint FK_TS_ROLE_MODULE_MODULE foreign key (MODULE_ID)
  references TS_MODULE (MODULE_ID) on delete cascade;
alter table TS_ROLE_MODULE
  add constraint FK_TS_ROLE_MODULE_ROLE foreign key (ROLE_ID)
  references TS_ROLE (ROLE_ID);

prompt
prompt Creating table TS_USER_DEPT
prompt ===========================
prompt
create table TS_USER_DEPT
(
  USER_ID       NUMBER(18) not null,
  DEPT_ID       NUMBER(18) not null,
  INHERITED_FLG NUMBER(1),
  CREATE_EMP    VARCHAR2(16),
  CREATE_TM     DATE,
  UPDATE_EMP    VARCHAR2(16),
  UPDATE_TM     DATE
)
;
comment on table TS_USER_DEPT
  is '用户已授权网点关联表';
comment on column TS_USER_DEPT.USER_ID
  is '用户id';
comment on column TS_USER_DEPT.DEPT_ID
  is '网点id';
comment on column TS_USER_DEPT.INHERITED_FLG
  is '是否包括子孙网点权限, 0-否1-是';
comment on column TS_USER_DEPT.CREATE_EMP
  is '创建人员';
comment on column TS_USER_DEPT.CREATE_TM
  is '创建时间';
comment on column TS_USER_DEPT.UPDATE_EMP
  is '更新人员';
comment on column TS_USER_DEPT.UPDATE_TM
  is '更新时间';
alter table TS_USER_DEPT
  add constraint PK_TS_USER_DEPT primary key (USER_ID, DEPT_ID);

prompt
prompt Creating table TS_USER_LOG
prompt ==========================
prompt
create table TS_USER_LOG
(
  LOG_ID           NUMBER(18) not null,
  USERNAME         VARCHAR2(30),
  LOG_TM           DATE not null,
  IP_ADDRESS       VARCHAR2(120) not null,
  LOG_TYPE         NUMBER(2) not null,
  OPERATION_TYPE   NUMBER(2) not null,
  TARGET_ID        NUMBER(18),
  TARGET_DESC      VARCHAR2(600),
  OPERATION_RESULT NUMBER(1) not null,
  CONTENT          VARCHAR2(3000)
)
;
comment on table TS_USER_LOG
  is '用户日志表';
comment on column TS_USER_LOG.LOG_ID
  is '日志ID';
comment on column TS_USER_LOG.USERNAME
  is '用户账号，操作者的账号';
comment on column TS_USER_LOG.LOG_TM
  is '记录时间';
comment on column TS_USER_LOG.IP_ADDRESS
  is '来源IP';
comment on column TS_USER_LOG.LOG_TYPE
  is '日志类型：1登录 2用户管理 3角色管理 4授权管理';
comment on column TS_USER_LOG.OPERATION_TYPE
  is '操作类型 1登录 2新增、开通、启用 3删除、注销、禁用 4修改、角色分配、删除、网点权限修改 ';
comment on column TS_USER_LOG.TARGET_ID
  is '操作目标ID，根据日志类型不同而不同，可能为用户账号ID 角色ID等';
comment on column TS_USER_LOG.TARGET_DESC
  is '操作目标描述，可能为用户账号名称 角色名称等';
comment on column TS_USER_LOG.OPERATION_RESULT
  is '操作结果：0失败，1成功';
comment on column TS_USER_LOG.CONTENT
  is '日志详细描述';
alter table TS_USER_LOG
  add constraint PK_USER_LOG primary key (LOG_ID);

prompt
prompt Creating table TS_USER_MODULE
prompt =============================
prompt
create table TS_USER_MODULE
(
  MODULE_ID  NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  USEABLE    NUMBER(1),
  CREATE_EMP VARCHAR2(16),
  CREATE_TM  DATE,
  UPDATE_EMP VARCHAR2(16),
  UPDATE_TM  DATE
)
;
comment on table TS_USER_MODULE
  is '用户模块表';
comment on column TS_USER_MODULE.MODULE_ID
  is '模块id';
comment on column TS_USER_MODULE.USER_ID
  is '用户id';
comment on column TS_USER_MODULE.USEABLE
  is '是否启用';
comment on column TS_USER_MODULE.CREATE_EMP
  is '创建人员';
comment on column TS_USER_MODULE.CREATE_TM
  is '创建时间';
comment on column TS_USER_MODULE.UPDATE_EMP
  is '更新人员';
comment on column TS_USER_MODULE.UPDATE_TM
  is '更新时间';
alter table TS_USER_MODULE
  add constraint PK_USER_MODULE primary key (MODULE_ID, USER_ID);
alter table TS_USER_MODULE
  add constraint FK_USER_MODULE_MODULE foreign key (MODULE_ID)
  references TS_MODULE (MODULE_ID) on delete cascade;
alter table TS_USER_MODULE
  add constraint FK_USER_MODULE_USER foreign key (USER_ID)
  references TS_USER (USER_ID);

prompt
prompt Creating table TS_USER_ROLE
prompt ===========================
prompt
create table TS_USER_ROLE
(
  ROLE_ID    NUMBER(18) not null,
  USER_ID    NUMBER(18) not null,
  CREATE_EMP VARCHAR2(16),
  CREATE_TM  DATE,
  UPDATE_EMP VARCHAR2(16),
  UPDATE_TM  DATE
)
;
comment on table TS_USER_ROLE
  is '用户角色表';
comment on column TS_USER_ROLE.ROLE_ID
  is '角色id';
comment on column TS_USER_ROLE.USER_ID
  is '用户id';
comment on column TS_USER_ROLE.CREATE_EMP
  is '更新人员';
comment on column TS_USER_ROLE.CREATE_TM
  is '创建人员';
comment on column TS_USER_ROLE.UPDATE_EMP
  is '更新人员';
comment on column TS_USER_ROLE.UPDATE_TM
  is '更新时间';
alter table TS_USER_ROLE
  add constraint PK_TS_USER_ROLE primary key (ROLE_ID, USER_ID);
alter table TS_USER_ROLE
  add constraint FK_USER_ROLE_ROLE foreign key (ROLE_ID)
  references TS_ROLE (ROLE_ID);
alter table TS_USER_ROLE
  add constraint FK_USER_ROLE_USER foreign key (USER_ID)
  references TS_USER (USER_ID);

prompt
prompt Creating table TT_PB_OVERTIME
prompt =============================
prompt
create table TT_PB_OVERTIME
(
  APLY_DATE     DATE,
  DEPT_CODE     VARCHAR2(30),
  EMP_CODE      VARCHAR2(20),
  SCHEDULE_NAME VARCHAR2(30),
  STATUS        NUMBER(1) default 0,
  CHKEMP_CODE   VARCHAR2(20),
  APLY_ID       NUMBER(38) not null,
  CREATE_DATE   DATE,
  GROUP_ID      NUMBER(38),
  HOURS         NUMBER(2),
  START_TIME    DATE,
  END_TIME      DATE,
  EMP_NAME      VARCHAR2(500),
  CHKEMP_NAME   VARCHAR2(500),
  DEPT_NAME     VARCHAR2(100),
  GROUP_NAME    VARCHAR2(60),
  GROUP_CODE    VARCHAR2(15),
  DEPT_ID       NUMBER(18)
)
;
comment on table TT_PB_OVERTIME
  is '加班表';
comment on column TT_PB_OVERTIME.APLY_DATE
  is '申请日期';
comment on column TT_PB_OVERTIME.DEPT_CODE
  is '中转场代码';
comment on column TT_PB_OVERTIME.EMP_CODE
  is '员工工号';
comment on column TT_PB_OVERTIME.SCHEDULE_NAME
  is '班别名称';
comment on column TT_PB_OVERTIME.STATUS
  is '状态审批通过为1没通过为0默认为0';
comment on column TT_PB_OVERTIME.CHKEMP_CODE
  is '确认人';
comment on column TT_PB_OVERTIME.APLY_ID
  is '申请编号';
comment on column TT_PB_OVERTIME.CREATE_DATE
  is '创建时间';
comment on column TT_PB_OVERTIME.GROUP_ID
  is '原排小组ID';
comment on column TT_PB_OVERTIME.HOURS
  is '加班时长';
comment on column TT_PB_OVERTIME.START_TIME
  is '加班起始时间';
comment on column TT_PB_OVERTIME.END_TIME
  is '加班结束时间';
comment on column TT_PB_OVERTIME.EMP_NAME
  is '员工姓名';
comment on column TT_PB_OVERTIME.CHKEMP_NAME
  is '确认员工姓名';
comment on column TT_PB_OVERTIME.DEPT_NAME
  is '机构名称';
comment on column TT_PB_OVERTIME.GROUP_NAME
  is '小组名称';
comment on column TT_PB_OVERTIME.GROUP_CODE
  is '小组代码';
comment on column TT_PB_OVERTIME.DEPT_ID
  is '网点ID';
alter table TT_PB_OVERTIME
  add constraint PK_TT_PB_OVERTIME primary key (APLY_ID);

prompt
prompt Creating table TT_PB_PROCESS_BY_DAY
prompt ===================================
prompt
create table TT_PB_PROCESS_BY_DAY
(
  ID                NUMBER(38) not null,
  PROCESS_CODE      VARCHAR2(18),
  DEPT_ID           NUMBER(19),
  PROCESS_DT        DATE,
  EMP_CODE          VARCHAR2(20),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  PROCESS_MON_ID    NUMBER(38)
)
;
comment on table TT_PB_PROCESS_BY_DAY
  is '工序记录安排记录表';
comment on column TT_PB_PROCESS_BY_DAY.ID
  is '主键';
comment on column TT_PB_PROCESS_BY_DAY.PROCESS_CODE
  is '工序代码';
comment on column TT_PB_PROCESS_BY_DAY.DEPT_ID
  is '网点ID';
comment on column TT_PB_PROCESS_BY_DAY.PROCESS_DT
  is '指定工序日期';
comment on column TT_PB_PROCESS_BY_DAY.EMP_CODE
  is '被安排工序员工工号';
comment on column TT_PB_PROCESS_BY_DAY.CREATE_TM
  is '创建日期';
comment on column TT_PB_PROCESS_BY_DAY.MODIFIED_TM
  is '修改日期';
comment on column TT_PB_PROCESS_BY_DAY.CREATE_EMP_CODE
  is '创建人工号';
comment on column TT_PB_PROCESS_BY_DAY.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TT_PB_PROCESS_BY_DAY.PROCESS_MON_ID
  is '月度主键ID';
alter table TT_PB_PROCESS_BY_DAY
  add constraint PK_TT_PB_PROCESS_BY_DAY primary key (ID);
create index INX_TT_PB_PROCESS_BY_DAY on TT_PB_PROCESS_BY_DAY (PROCESS_MON_ID);
create unique index UK_TT_PB_PROCESS_BY_DAY on TT_PB_PROCESS_BY_DAY (EMP_CODE, PROCESS_DT, DEPT_ID);

prompt
prompt Creating table TT_PB_PROCESS_BY_MONTH
prompt =====================================
prompt
create table TT_PB_PROCESS_BY_MONTH
(
  ID                NUMBER(38) not null,
  YM                VARCHAR2(50),
  DEPT_ID           NUMBER(19),
  EMP_CODE          VARCHAR2(50),
  DAY1              VARCHAR2(50),
  DAY2              VARCHAR2(50),
  DAY3              VARCHAR2(50),
  DAY4              VARCHAR2(50),
  DAY5              VARCHAR2(50),
  DAY6              VARCHAR2(50),
  DAY7              VARCHAR2(50),
  DAY8              VARCHAR2(50),
  DAY9              VARCHAR2(50),
  DAY10             VARCHAR2(50),
  DAY11             VARCHAR2(50),
  DAY12             VARCHAR2(50),
  DAY13             VARCHAR2(50),
  DAY14             VARCHAR2(50),
  DAY15             VARCHAR2(50),
  DAY16             VARCHAR2(50),
  DAY17             VARCHAR2(50),
  DAY18             VARCHAR2(50),
  DAY19             VARCHAR2(50),
  DAY20             VARCHAR2(50),
  DAY21             VARCHAR2(50),
  DAY22             VARCHAR2(50),
  DAY23             VARCHAR2(50),
  DAY24             VARCHAR2(50),
  DAY25             VARCHAR2(50),
  DAY26             VARCHAR2(50),
  DAY27             VARCHAR2(50),
  DAY28             VARCHAR2(50),
  DAY29             VARCHAR2(50),
  DAY30             VARCHAR2(50),
  DAY31             VARCHAR2(50),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  VERSION           NUMBER(5)
)
;
comment on table TT_PB_PROCESS_BY_MONTH
  is '工序月度记录表';
comment on column TT_PB_PROCESS_BY_MONTH.ID
  is '主键ID';
comment on column TT_PB_PROCESS_BY_MONTH.YM
  is '年月';
comment on column TT_PB_PROCESS_BY_MONTH.DEPT_ID
  is '网点ID';
comment on column TT_PB_PROCESS_BY_MONTH.EMP_CODE
  is '被排班人工号';
comment on column TT_PB_PROCESS_BY_MONTH.DAY1
  is '第1天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY2
  is '第2天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY3
  is '第3天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY4
  is '第4天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY5
  is '第5天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY6
  is '第6天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY7
  is '第7天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY8
  is '第8天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY9
  is '第9天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY10
  is '第10天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY11
  is '第11天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY12
  is '第12天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY13
  is '第13天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY14
  is '第14天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY15
  is '第15天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY16
  is '第16天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY17
  is '第17天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY18
  is '第18天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY19
  is '第19天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY20
  is '第20天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY21
  is '第21天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY22
  is '第22天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY23
  is '第23天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY24
  is '第24天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY25
  is '第25天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY26
  is '第26天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY27
  is '第27天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY28
  is '第28天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY29
  is '第29天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY30
  is '第30天';
comment on column TT_PB_PROCESS_BY_MONTH.DAY31
  is '第31天';
comment on column TT_PB_PROCESS_BY_MONTH.CREATE_TM
  is '创建日期';
comment on column TT_PB_PROCESS_BY_MONTH.MODIFIED_TM
  is '修改日期';
comment on column TT_PB_PROCESS_BY_MONTH.CREATE_EMP_CODE
  is '创建人工号';
comment on column TT_PB_PROCESS_BY_MONTH.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TT_PB_PROCESS_BY_MONTH.VERSION
  is '版本';
alter table TT_PB_PROCESS_BY_MONTH
  add constraint PK_TT_PB_PROCESS_BY_MONTH primary key (ID);
create index INX1_TT_PB_PROCESS_BY_MONTH on TT_PB_PROCESS_BY_MONTH (DEPT_ID);
create index INX2_TT_PB_PROCESS_BY_MONTH on TT_PB_PROCESS_BY_MONTH (EMP_CODE);
create unique index UK_TT_PB_PROCESS_BY_MONTH on TT_PB_PROCESS_BY_MONTH (YM, EMP_CODE, DEPT_ID);

prompt
prompt Creating table TT_PB_PROCESS_CONFIRM
prompt ====================================
prompt
create table TT_PB_PROCESS_CONFIRM
(
  ID            NUMBER(19) not null,
  DEPT_ID       NUMBER(19),
  YM            VARCHAR2(50),
  COMMIT_STATUS NUMBER(1)
)
;
comment on table TT_PB_PROCESS_CONFIRM
  is '工序月度提交确认状态表';
comment on column TT_PB_PROCESS_CONFIRM.DEPT_ID
  is '网点';
comment on column TT_PB_PROCESS_CONFIRM.YM
  is '月份';
comment on column TT_PB_PROCESS_CONFIRM.COMMIT_STATUS
  is '提交确认标志(1-已确认 null-未确认)';
alter table TT_PB_PROCESS_CONFIRM
  add constraint PK_TT_PB_PROCESS_CONFIRM primary key (ID);
create unique index UK_TT_PB_PROCESS_CONFIRM on TT_PB_PROCESS_CONFIRM (YM, DEPT_ID);

prompt
prompt Creating table TT_PB_SCHE_CONFIRM
prompt =================================
prompt
create table TT_PB_SCHE_CONFIRM
(
  ID            NUMBER(19) not null,
  DEPT_ID       NUMBER(19),
  YM            VARCHAR2(50),
  COMMIT_STATUS NUMBER(1)
)
;
comment on table TT_PB_SCHE_CONFIRM
  is '排班月度提交确认状态表';
comment on column TT_PB_SCHE_CONFIRM.DEPT_ID
  is '网点';
comment on column TT_PB_SCHE_CONFIRM.YM
  is '月份';
comment on column TT_PB_SCHE_CONFIRM.COMMIT_STATUS
  is '提交确认标志(1-已确认 null-未确认)';
alter table TT_PB_SCHE_CONFIRM
  add constraint PK_TT_PB_SCHE_CONFIRM primary key (ID);
create unique index UK_TT_PB_SCHE_CONFIRM on TT_PB_SCHE_CONFIRM (DEPT_ID, YM);

prompt
prompt Creating table TT_PB_SHEDULE_BY_DAY
prompt ===================================
prompt
create table TT_PB_SHEDULE_BY_DAY
(
  ID                NUMBER(38) not null,
  SHEDULE_ID        NUMBER(38),
  DEPT_ID           NUMBER(19),
  SHEDULE_DT        DATE,
  EMP_CODE          VARCHAR2(20),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  SHEDULE_MON_ID    NUMBER(38),
  SHEDULE_CODE      VARCHAR2(20)
)
;
comment on table TT_PB_SHEDULE_BY_DAY
  is '排班记录表';
comment on column TT_PB_SHEDULE_BY_DAY.ID
  is '主键ID';
comment on column TT_PB_SHEDULE_BY_DAY.SHEDULE_ID
  is '班别ID';
comment on column TT_PB_SHEDULE_BY_DAY.DEPT_ID
  is '网点ID';
comment on column TT_PB_SHEDULE_BY_DAY.SHEDULE_DT
  is '排班日期';
comment on column TT_PB_SHEDULE_BY_DAY.EMP_CODE
  is '被排班人工号';
comment on column TT_PB_SHEDULE_BY_DAY.CREATE_TM
  is '创建日期';
comment on column TT_PB_SHEDULE_BY_DAY.MODIFIED_TM
  is '修改日期';
comment on column TT_PB_SHEDULE_BY_DAY.CREATE_EMP_CODE
  is '创建人工号';
comment on column TT_PB_SHEDULE_BY_DAY.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TT_PB_SHEDULE_BY_DAY.SHEDULE_MON_ID
  is '月度表主键ID';
comment on column TT_PB_SHEDULE_BY_DAY.SHEDULE_CODE
  is '班别代码';
alter table TT_PB_SHEDULE_BY_DAY
  add constraint PK_TT_PB_SHEDULE_BY_DAY primary key (ID);
create index INX_TT_PB_SHEDULE_BY_DAY on TT_PB_SHEDULE_BY_DAY (SHEDULE_MON_ID);
create unique index UK_TT_PB_SHEDULE_BY_DAY on TT_PB_SHEDULE_BY_DAY (EMP_CODE, SHEDULE_DT, DEPT_ID);

prompt
prompt Creating table TT_PB_SHEDULE_BY_MONTH
prompt =====================================
prompt
create table TT_PB_SHEDULE_BY_MONTH
(
  ID                NUMBER(38) not null,
  YM                VARCHAR2(50),
  DEPT_ID           NUMBER(19),
  EMP_CODE          VARCHAR2(50),
  DAY1              VARCHAR2(50),
  DAY2              VARCHAR2(50),
  DAY3              VARCHAR2(50),
  DAY4              VARCHAR2(50),
  DAY5              VARCHAR2(50),
  DAY6              VARCHAR2(50),
  DAY7              VARCHAR2(50),
  DAY8              VARCHAR2(50),
  DAY9              VARCHAR2(50),
  DAY10             VARCHAR2(50),
  DAY11             VARCHAR2(50),
  DAY12             VARCHAR2(50),
  DAY13             VARCHAR2(50),
  DAY14             VARCHAR2(50),
  DAY15             VARCHAR2(50),
  DAY16             VARCHAR2(50),
  DAY17             VARCHAR2(50),
  DAY18             VARCHAR2(50),
  DAY19             VARCHAR2(50),
  DAY20             VARCHAR2(50),
  DAY21             VARCHAR2(50),
  DAY22             VARCHAR2(50),
  DAY23             VARCHAR2(50),
  DAY24             VARCHAR2(50),
  DAY25             VARCHAR2(50),
  DAY26             VARCHAR2(50),
  DAY27             VARCHAR2(50),
  DAY28             VARCHAR2(50),
  DAY29             VARCHAR2(50),
  DAY30             VARCHAR2(50),
  DAY31             VARCHAR2(50),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  VERSION           NUMBER(5)
)
;
comment on table TT_PB_SHEDULE_BY_MONTH
  is '排班月度记录表';
comment on column TT_PB_SHEDULE_BY_MONTH.ID
  is '主键ID';
comment on column TT_PB_SHEDULE_BY_MONTH.YM
  is '年月';
comment on column TT_PB_SHEDULE_BY_MONTH.DEPT_ID
  is '网点ID';
comment on column TT_PB_SHEDULE_BY_MONTH.EMP_CODE
  is '被排班人工号';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY1
  is '第1天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY2
  is '第2天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY3
  is '第3天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY4
  is '第4天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY5
  is '第5天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY6
  is '第6天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY7
  is '第7天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY8
  is '第8天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY9
  is '第9天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY10
  is '第10天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY11
  is '第11天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY12
  is '第12天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY13
  is '第13天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY14
  is '第14天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY15
  is '第15天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY16
  is '第16天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY17
  is '第17天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY18
  is '第18天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY19
  is '第19天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY20
  is '第20天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY21
  is '第21天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY22
  is '第22天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY23
  is '第23天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY24
  is '第24天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY25
  is '第25天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY26
  is '第26天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY27
  is '第27天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY28
  is '第28天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY29
  is '第29天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY30
  is '第30天';
comment on column TT_PB_SHEDULE_BY_MONTH.DAY31
  is '第31天';
comment on column TT_PB_SHEDULE_BY_MONTH.CREATE_TM
  is '创建日期';
comment on column TT_PB_SHEDULE_BY_MONTH.MODIFIED_TM
  is '修改日期';
comment on column TT_PB_SHEDULE_BY_MONTH.CREATE_EMP_CODE
  is '创建人工号';
comment on column TT_PB_SHEDULE_BY_MONTH.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column TT_PB_SHEDULE_BY_MONTH.VERSION
  is '版本';
alter table TT_PB_SHEDULE_BY_MONTH
  add constraint PK_TT_PB_SHEDULE_BY_MONTH primary key (ID);
create index INX1_TT_PB_SHEDULE_BY_MONTH on TT_PB_SHEDULE_BY_MONTH (DEPT_ID);
create index INX2_TT_PB_SHEDULE_BY_MONTH on TT_PB_SHEDULE_BY_MONTH (EMP_CODE);
create unique index UK_TT_PB_SHEDULE_BY_MONTH on TT_PB_SHEDULE_BY_MONTH (YM, EMP_CODE, DEPT_ID);

prompt
prompt Creating table TT_PUSHMESSAGE
prompt =============================
prompt
create table TT_PUSHMESSAGE
(
  MSGID      VARCHAR2(256) not null,
  MSGCONTENT VARCHAR2(2000),
  STATUS     NUMBER(1) default 0,
  CREATETM   DATE default sysdate,
  MODIFIEDTM DATE,
  APPID      VARCHAR2(100),
  USERID     VARCHAR2(36)
)
;
comment on column TT_PUSHMESSAGE.MSGID
  is '消息ID';
comment on column TT_PUSHMESSAGE.MSGCONTENT
  is '消息内容';
comment on column TT_PUSHMESSAGE.STATUS
  is '消息处理状态';
comment on column TT_PUSHMESSAGE.CREATETM
  is '创建时间';
comment on column TT_PUSHMESSAGE.MODIFIEDTM
  is '修改时间';
comment on column TT_PUSHMESSAGE.APPID
  is '应用ID';
comment on column TT_PUSHMESSAGE.USERID
  is '用户ID';
alter table TT_PUSHMESSAGE
  add constraint PK_MSGID primary key (MSGID);

prompt
prompt Creating table TT_SAP_SYNCHRONOUS
prompt =================================
prompt
create table TT_SAP_SYNCHRONOUS
(
  ID            NUMBER(20),
  EMP_CODE      VARCHAR2(10),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  CREATE_TM     DATE default sysdate,
  NODE_KEY      VARCHAR2(30),
  STATE_FLG     NUMBER(1) default 0
)
;
comment on table TT_SAP_SYNCHRONOUS
  is '排班同步到SAP信息表';
comment on column TT_SAP_SYNCHRONOUS.ID
  is '主键,序号';
comment on column TT_SAP_SYNCHRONOUS.EMP_CODE
  is '员工工号';
comment on column TT_SAP_SYNCHRONOUS.BEGIN_DATE
  is '开始日期';
comment on column TT_SAP_SYNCHRONOUS.END_DATE
  is '结束日期';
comment on column TT_SAP_SYNCHRONOUS.BEGIN_TM
  is '开始时间';
comment on column TT_SAP_SYNCHRONOUS.END_TM
  is '结束时间';
comment on column TT_SAP_SYNCHRONOUS.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_SAP_SYNCHRONOUS.OFF_DUTY_FLAG
  is '休息标识(ON为休息，默认为OFF)';
comment on column TT_SAP_SYNCHRONOUS.CLASS_SYSTEM
  is '排班来源(2-调度系统)';
comment on column TT_SAP_SYNCHRONOUS.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_SAP_SYNCHRONOUS.STATE_FLG
  is '同步状态 0-初始状态1-正在处理2-同步成功3-同步失败';

prompt
prompt Creating table TT_SAP_SYNCHRONOUS_RECORD
prompt ========================================
prompt
create table TT_SAP_SYNCHRONOUS_RECORD
(
  ID                   NUMBER(8),
  SYNCHRONIZATION_TIME DATE,
  PROCEDURES_NAME      VARCHAR2(50)
)
;
comment on table TT_SAP_SYNCHRONOUS_RECORD
  is '排班与SAP同步时间表';

prompt
prompt Creating table TT_SCHEDULE_DAILY
prompt ================================
prompt
create table TT_SCHEDULE_DAILY
(
  ID                     NUMBER(38) not null,
  DEPARTMENT_CODE        VARCHAR2(30),
  BEGIN_TIME             VARCHAR2(10),
  END_TIME               VARCHAR2(10),
  DAY_OF_MONTH           VARCHAR2(10),
  MONTH_ID               VARCHAR2(10),
  EMPLOYEE_CODE          VARCHAR2(20),
  CREATED_EMPLOYEE_CODE  VARCHAR2(20),
  MODIFIED_EMPLOYEE_CODE VARCHAR2(20),
  CREATE_TIME            DATE,
  MODIFIED_TIME          DATE,
  EMP_POST_TYPE          VARCHAR2(1),
  CROSS_DAY_TYPE         VARCHAR2(1)
)
;
comment on table TT_SCHEDULE_DAILY
  is '排班信息表';
comment on column TT_SCHEDULE_DAILY.ID
  is 'ID';
comment on column TT_SCHEDULE_DAILY.DEPARTMENT_CODE
  is 'departmengtcode';
comment on column TT_SCHEDULE_DAILY.BEGIN_TIME
  is 'time for working starting';
comment on column TT_SCHEDULE_DAILY.END_TIME
  is 'time for working ending';
comment on column TT_SCHEDULE_DAILY.DAY_OF_MONTH
  is 'day of month';
comment on column TT_SCHEDULE_DAILY.MONTH_ID
  is 'month Id format (YYYY-MM)';
comment on column TT_SCHEDULE_DAILY.EMPLOYEE_CODE
  is 'user code';
comment on column TT_SCHEDULE_DAILY.CREATED_EMPLOYEE_CODE
  is 'created user code';
comment on column TT_SCHEDULE_DAILY.MODIFIED_EMPLOYEE_CODE
  is 'update user code';
comment on column TT_SCHEDULE_DAILY.CREATE_TIME
  is 'created time';
comment on column TT_SCHEDULE_DAILY.MODIFIED_TIME
  is 'update time';
comment on column TT_SCHEDULE_DAILY.EMP_POST_TYPE
  is '岗位类型（1-运作员、2-收派员、3-仓管、4-客服）';
comment on column TT_SCHEDULE_DAILY.CROSS_DAY_TYPE
  is '跨天为X，其他为空';

prompt
prompt Creating table TT_SCH_EMP_ATTENCE_CLASS
prompt =======================================
prompt
create table TT_SCH_EMP_ATTENCE_CLASS
(
  ID            NUMBER(20),
  EMP_CODE      VARCHAR2(10),
  EMP_TYPE      VARCHAR2(2),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  CREATE_TM     DATE,
  NODE_KEY      VARCHAR2(30),
  STATE_FLG     NUMBER(1)
)
;
comment on table TT_SCH_EMP_ATTENCE_CLASS
  is '调度信息同步表';
comment on column TT_SCH_EMP_ATTENCE_CLASS.ID
  is '主键,序号';
comment on column TT_SCH_EMP_ATTENCE_CLASS.EMP_CODE
  is '员工工号';
comment on column TT_SCH_EMP_ATTENCE_CLASS.EMP_TYPE
  is '1-全日制，2-非全日制';
comment on column TT_SCH_EMP_ATTENCE_CLASS.BEGIN_DATE
  is '开始日期';
comment on column TT_SCH_EMP_ATTENCE_CLASS.END_DATE
  is '结束日期';
comment on column TT_SCH_EMP_ATTENCE_CLASS.BEGIN_TM
  is '开始时间';
comment on column TT_SCH_EMP_ATTENCE_CLASS.END_TM
  is '结束时间';
comment on column TT_SCH_EMP_ATTENCE_CLASS.TMR_DAY_FLAG
  is '前一天标识';
comment on column TT_SCH_EMP_ATTENCE_CLASS.OFF_DUTY_FLAG
  is '休息标识(ON为休息，默认为OFF)';
comment on column TT_SCH_EMP_ATTENCE_CLASS.CLASS_SYSTEM
  is '排班来源(2-调度系统)';
comment on column TT_SCH_EMP_ATTENCE_CLASS.CREATE_TM
  is '数据生成日期(分区字段)';
comment on column TT_SCH_EMP_ATTENCE_CLASS.STATE_FLG
  is '0';


spool off
