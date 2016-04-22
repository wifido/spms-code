
-- TT_SCHEDULE_DAILY_HIS
create table TT_SCHEDULE_DAILY_HIS
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
  CROSS_DAY_TYPE         VARCHAR2(1),
  SYNCHRO_STATUS         NUMBER,
  SCHEDULING_CODE        VARCHAR2(20),
  DATA_SOURCE            VARCHAR2(1) default 0
);

--TT_SAP_SYNCHRONOUS_HIS
create table TT_SAP_SYNCHRONOUS_HIS
(
  ID                NUMBER(38),
  EMP_CODE          VARCHAR2(30),
  BEGIN_DATE        VARCHAR2(8),
  END_DATE          VARCHAR2(8),
  BEGIN_TM          VARCHAR2(6),
  END_TM            VARCHAR2(6),
  TMR_DAY_FLAG      VARCHAR2(1),
  OFF_DUTY_FLAG     VARCHAR2(20),
  CLASS_SYSTEM      VARCHAR2(1),
  CREATE_TM         DATE,
  NODE_KEY          VARCHAR2(30),
  STATE_FLG         NUMBER(1),
  ERROR_INFO        VARCHAR2(1000),
  SCHEDULE_DAILY_ID NUMBER(38),
  EMP_POST_TYPE     VARCHAR2(1),
  SYNC_TM           DATE
);

-- TT_PB_SHEDULE_BY_MONTH_HIS
create table TT_PB_SHEDULE_BY_MONTH_HIS
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
  VERSION           NUMBER(5),
  COMMIT_STATUS     NUMBER,
  SYNCHRO_STATUS    NUMBER,
  EMP_NAME          VARCHAR2(500),
  WORK_TYPE         NUMBER(2)
);

-- TT_PB_SHEDULE_BY_DAY_HIS
create table TT_PB_SHEDULE_BY_DAY_HIS
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
  SHEDULE_CODE      VARCHAR2(20),
  SYNCHRO_STATUS    NUMBER,
  COMMIT_STATUS     NUMBER(1)
);

-- TT_DRIVER_SCHEDULING_HIS
create table TT_DRIVER_SCHEDULING_HIS
(
  ID                     NUMBER(38) not null,
  DEPARTMENT_CODE        VARCHAR2(30),
  EMPLOYEE_CODE          VARCHAR2(20),
  CONFIGURE_CODE         VARCHAR2(20),
  DAY_OF_MONTH           VARCHAR2(10),
  YEAR_MONTH             VARCHAR2(10),
  CREATED_EMPLOYEE_CODE  VARCHAR2(20),
  MODIFIED_EMPLOYEE_CODE VARCHAR2(20),
  CREATE_TIME            DATE,
  MODIFIED_TIME          DATE,
  SCHEDULING_TYPE        NUMBER(1),
  WORK_TYPE              VARCHAR2(30) default 'Õý³£',
  YEAR_WEEK              VARCHAR2(10),
  CONFIRM_STATUS         NUMBER(1),
  CONFIRM_DATE           DATE,
  SYNC_STATE             NUMBER(1) default 0
);
