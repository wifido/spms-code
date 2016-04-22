-- Create sequence 
create sequence SEQ_REPORT_DRIVER_SCHEDULING1
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

-- Create table
create table REPORT_DRIVER_SCHEDULING
(
  ID                  NUMBER(20) not null,
  EMPLOYEE_CODE       VARCHAR2(20),
  EMPLOYEE_NAME       VARCHAR2(100),
  DEPARTMENT_CODE     VARCHAR2(30),
  DEPARTMENT_NAME     VARCHAR2(100),
  AREA_CODE           VARCHAR2(30),
  AREA_NAME           VARCHAR2(100),
  SCHEDULE_TYPE       VARCHAR2(10),
  DAY1                VARCHAR2(20),
  DAY2                VARCHAR2(20),
  DAY3                VARCHAR2(20),
  DAY4                VARCHAR2(20),
  DAY5                VARCHAR2(20),
  DAY6                VARCHAR2(20),
  DAY7                VARCHAR2(20),
  DAY8                VARCHAR2(20),
  DAY9                VARCHAR2(20),
  DAY10               VARCHAR2(20),
  DAY11               VARCHAR2(20),
  DAY12               VARCHAR2(20),
  DAY13               VARCHAR2(20),
  DAY14               VARCHAR2(20),
  DAY15               VARCHAR2(20),
  DAY16               VARCHAR2(20),
  DAY17               VARCHAR2(20),
  DAY18               VARCHAR2(20),
  DAY19               VARCHAR2(20),
  DAY20               VARCHAR2(20),
  DAY21               VARCHAR2(20),
  DAY22               VARCHAR2(20),
  DAY23               VARCHAR2(20),
  DAY24               VARCHAR2(20),
  DAY25               VARCHAR2(20),
  DAY26               VARCHAR2(20),
  DAY27               VARCHAR2(20),
  DAY28               VARCHAR2(20),
  DAY29               VARCHAR2(20),
  DAY30               VARCHAR2(20),
  DAY31               VARCHAR2(20),
  MODIFY_TIME         DATE default SYSDATE,
  TOTAL_REST_COUNT    NUMBER(10),
  DRIVE_TIME_MONTH_S  NUMBER(10,2),
  DRIVE_TIME_MONTH_T  NUMBER(10,2),
  ATTENDANCE_DURATION NUMBER(10,2),
  MATCH_RATE          NUMBER(10,4),
  MONTH               VARCHAR2(10)
);


-- Add comments to the table 
comment on table REPORT_DRIVER_SCHEDULING
  is '�Űർ������';
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.ID
  is '����';
comment on column REPORT_DRIVER_SCHEDULING.EMPLOYEE_CODE
  is 'Ա������';
comment on column REPORT_DRIVER_SCHEDULING.EMPLOYEE_NAME
  is 'Ա������';
comment on column REPORT_DRIVER_SCHEDULING.DEPARTMENT_CODE
  is '�������';
comment on column REPORT_DRIVER_SCHEDULING.DEPARTMENT_NAME
  is '��������';
comment on column REPORT_DRIVER_SCHEDULING.AREA_CODE
  is '�������';
comment on column REPORT_DRIVER_SCHEDULING.AREA_NAME
  is '��������';
comment on column REPORT_DRIVER_SCHEDULING.SCHEDULE_TYPE
  is '�Ű�����''����''';
comment on column REPORT_DRIVER_SCHEDULING.DAY1
  is '��01��';
comment on column REPORT_DRIVER_SCHEDULING.DAY2
  is '��02��';
comment on column REPORT_DRIVER_SCHEDULING.DAY3
  is '��03��';
comment on column REPORT_DRIVER_SCHEDULING.DAY4
  is '��04��';
comment on column REPORT_DRIVER_SCHEDULING.DAY5
  is '��05��';
comment on column REPORT_DRIVER_SCHEDULING.DAY6
  is '��06��';
comment on column REPORT_DRIVER_SCHEDULING.DAY7
  is '��07��';
comment on column REPORT_DRIVER_SCHEDULING.DAY8
  is '��08��';
comment on column REPORT_DRIVER_SCHEDULING.DAY9
  is '��09��';
comment on column REPORT_DRIVER_SCHEDULING.DAY10
  is '��10��';
comment on column REPORT_DRIVER_SCHEDULING.DAY11
  is '��11��';
comment on column REPORT_DRIVER_SCHEDULING.DAY12
  is '��12��';
comment on column REPORT_DRIVER_SCHEDULING.DAY13
  is '��13��';
comment on column REPORT_DRIVER_SCHEDULING.DAY14
  is '��14��';
comment on column REPORT_DRIVER_SCHEDULING.DAY15
  is '��15��';
comment on column REPORT_DRIVER_SCHEDULING.DAY16
  is '��16��';
comment on column REPORT_DRIVER_SCHEDULING.DAY17
  is '��17��';
comment on column REPORT_DRIVER_SCHEDULING.DAY18
  is '��18��';
comment on column REPORT_DRIVER_SCHEDULING.DAY19
  is '��19��';
comment on column REPORT_DRIVER_SCHEDULING.DAY20
  is '��20��';
comment on column REPORT_DRIVER_SCHEDULING.DAY21
  is '��21��';
comment on column REPORT_DRIVER_SCHEDULING.DAY22
  is '��22��';
comment on column REPORT_DRIVER_SCHEDULING.DAY23
  is '��23��';
comment on column REPORT_DRIVER_SCHEDULING.DAY24
  is '��24��';
comment on column REPORT_DRIVER_SCHEDULING.DAY25
  is '��25��';
comment on column REPORT_DRIVER_SCHEDULING.DAY26
  is '��26��';
comment on column REPORT_DRIVER_SCHEDULING.DAY27
  is '��27��';
comment on column REPORT_DRIVER_SCHEDULING.DAY28
  is '��28��';
comment on column REPORT_DRIVER_SCHEDULING.DAY29
  is '��29��';
comment on column REPORT_DRIVER_SCHEDULING.DAY30
  is '��30��';
comment on column REPORT_DRIVER_SCHEDULING.DAY31
  is '��31��';
comment on column REPORT_DRIVER_SCHEDULING.MODIFY_TIME
  is '����ʱ��';
comment on column REPORT_DRIVER_SCHEDULING.TOTAL_REST_COUNT
  is '��Ϣ������';
comment on column REPORT_DRIVER_SCHEDULING.DRIVE_TIME_MONTH_S
  is '�¶ȼ�ʻʱ��(�Ű�)';
comment on column REPORT_DRIVER_SCHEDULING.DRIVE_TIME_MONTH_T
  is '�¶ȼ�ʻʱ��(ʵ��)';
comment on column REPORT_DRIVER_SCHEDULING.ATTENDANCE_DURATION
  is '�¶ȼ�ʻ����ʱ��';
comment on column REPORT_DRIVER_SCHEDULING.MATCH_RATE
  is '�Ű��Ǻ���';
comment on column REPORT_DRIVER_SCHEDULING.MONTH
  is '�Ű��·�';
-- Create/Recreate primary, unique and foreign key constraints 
alter table REPORT_DRIVER_SCHEDULING
  add constraint REPORT_DRIVER_PK_ID primary key (ID);
  
  -- Create/Recreate indexes 
create index IDX_REPORT_DRIVER_SCH1 on REPORT_DRIVER_SCHEDULING (DEPARTMENT_CODE, MONTH);
