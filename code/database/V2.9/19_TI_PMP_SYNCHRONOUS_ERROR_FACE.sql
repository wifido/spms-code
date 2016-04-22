-- Create table
create table TI_PMP_SYNCHRONOUS_ERROR_FACE
(
  ID            NUMBER(38) not null,
  EMP_CODE      VARCHAR2(30),
  BEGIN_DATE    VARCHAR2(8),
  END_DATE      VARCHAR2(8),
  BEGIN_TM      VARCHAR2(6),
  END_TM        VARCHAR2(6),
  TMR_DAY_FLAG  VARCHAR2(1),
  OFF_DUTY_FLAG VARCHAR2(20),
  CLASS_SYSTEM  VARCHAR2(1),
  THEME_NAME    VARCHAR2(100),
  ERROR_INFO    VARCHAR2(300),
  LASTUPDATE    DATE,
  EXTRAINFO1    VARCHAR2(100),
  EXTRAINFO2    VARCHAR2(300),
  EXTRAINFO3    VARCHAR2(1000),
  DEAL_FLAG     NUMBER(1)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_PMP_SYNCHRONOUS_ERROR_FACE
  add constraint PMP_SYNCHRONOUS_ERROR_FACE_PK primary key (ID);
-- Grant/Revoke object privileges 
grant select, insert, update, delete on TI_PMP_SYNCHRONOUS_ERROR_FACE to PMPETL;
