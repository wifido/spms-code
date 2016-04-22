-- Create table
create table TM_PMP_EMPLOYEE_TMP
(
  EMP_ID            NUMBER(38) not null,
  EMP_CODE          VARCHAR2(20),
  EMP_NAME          VARCHAR2(500),
  EMP_DUTY_NAME     VARCHAR2(100),
  DEPT_ID           NUMBER(19),
  CREATE_TM         DATE,
  MODIFIED_TM       DATE,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  WORK_TYPE         NUMBER(2),
  EMAIL             VARCHAR2(100),
  DIMISSION_DT      DATE,
  SF_DATE           DATE,
  EMP_POST_TYPE     VARCHAR2(1),
  POSITION_ATTR     VARCHAR2(20),
  CANCEL_FLAG       VARCHAR2(10),
  SYNC_TM           DATE,
  DATA_SOURCE       VARCHAR2(1),
  PERSK_TXT         VARCHAR2(150)
);
