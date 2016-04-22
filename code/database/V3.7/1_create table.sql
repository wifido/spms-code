-- Create table
-- 一线监控临时表-存放上月排班数据
create table TT_SCHEDULE_DAILY_MONTH
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
-- Create/Recreate indexes 
create index INDEX_MONTH_1 on TT_SCHEDULE_DAILY_MONTH (DEPARTMENT_CODE);
create index INDEX_MONTH_2 on TT_SCHEDULE_DAILY_MONTH (EMPLOYEE_CODE);
create index INDEX_MONTH_3 on TT_SCHEDULE_DAILY_MONTH (DAY_OF_MONTH);




-- Create table
-- 一线监控临时表-存放下周排班数据
create table TT_SCHEDULE_DAILY_DAY
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
-- Create/Recreate indexes 
create index INDEX_DAY_1 on TT_SCHEDULE_DAILY_DAY (DEPARTMENT_CODE);
create index INDEX_DAY_2 on TT_SCHEDULE_DAILY_DAY (EMPLOYEE_CODE);
create index INDEX_DAY_3 on TT_SCHEDULE_DAILY_DAY (DAY_OF_MONTH);

-- 一线监控表添加索引
create index IDX_MONITOR_REPORT1 on TT_DISPATCH_MONITOR_REPORT (DAY_OF_MONTH);


