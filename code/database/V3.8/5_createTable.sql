-- Create table
-- 仓管控临时表-存放系统日期-1至系统日期+7排班数据
create table TT_WAREHOUSE_DAILY_DAY
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
create index INDEX_DAY1 on TT_WAREHOUSE_DAILY_DAY (DEPARTMENT_CODE);
create index INDEX_DAY2 on TT_WAREHOUSE_DAILY_DAY (EMPLOYEE_CODE);
create index INDEX_DAY3 on TT_WAREHOUSE_DAILY_DAY (DAY_OF_MONTH);