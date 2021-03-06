-- Create table
create table TT_TASK_COMPARE
(
  ID             NUMBER(38) not null,
  EMPLOYEE_CODE  VARCHAR2(20),
  DAY_OF_MONTH   VARCHAR2(210),
  SYNC_STATUS    VARCHAR2(1) default 0,
  CONFIGURE_CODE VARCHAR2(20),
  OPERATION_TYPE VARCHAR2(1),
  CREATED_TIME   DATE default sysdate,
  UPDATED_TIME   DATE,
  DEPT_CODE      VARCHAR2(20),
  YEAR_MONTH     VARCHAR2(20)
);