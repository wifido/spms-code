-- Create table
create table TI_SPMS2CDP_OPERATION_REPORT
(
  SCHEDULE_DT         DATE,
  AREA_CODE           VARCHAR2(50),
  DEPT_CODE           VARCHAR2(50),
  SCHEDULE_CODE       VARCHAR2(50),
  BEGIN_TM            DATE,
  END_TM              DATE,
  SHUTTLE_LENGTH      NUMBER,
  FTE_NUM             NUMBER,
  FTE_SCHEDULE_NUM    NUMBER,
  FTE_ATTENDANCE_NUM  NUMBER(8,2),
  OUT_EMP_NUM         NUMBER,
  OUT_SCHEDULE_NUM    NUMBER,
  OUT_ATTENDANCE_NUM  NUMBER(8,2),
  NFTE_NUM            NUMBER,
  NFTE_SCHEDULE_NUM   NUMBER,
  NFTE_ATTENDANCE_NUM NUMBER(8,2),
  TOTAL_OF_ATTENDANCE NUMBER(8,2),
  SYNC_DATE           DATE
);
-- Add comments to the table 
comment on table TI_SPMS2CDP_OPERATION_REPORT
  is 'SPMS�Ű�ϵͳ-CDP�ӿڱ�';
-- Add comments to the columns 
comment on column TI_SPMS2CDP_OPERATION_REPORT.SCHEDULE_DT
  is '����
';
comment on column TI_SPMS2CDP_OPERATION_REPORT.AREA_CODE
  is '����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.DEPT_CODE
  is '��ת��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SCHEDULE_CODE
  is '��α���';
comment on column TI_SPMS2CDP_OPERATION_REPORT.BEGIN_TM
  is '�ƻ���ʼʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.END_TM
  is '�ƻ�����ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SHUTTLE_LENGTH
  is '���ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_NUM
  is 'ȫ����Ա����ְ����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_SCHEDULE_NUM
  is 'ȫ����Ա���Ű�����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_ATTENDANCE_NUM
  is 'ȫ����Ա���ƻ�����ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_EMP_NUM
  is '�����ְ����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_SCHEDULE_NUM
  is '����Ű�����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_ATTENDANCE_NUM
  is '���Ա���ƻ�����ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_NUM
  is '��ȫ����Ա����ְ����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_SCHEDULE_NUM
  is '��ȫ����Ա���Ű�����';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_ATTENDANCE_NUM
  is '��ȫ����Ա���ƻ�����ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.TOTAL_OF_ATTENDANCE
  is '�ܼƻ�����ʱ��';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SYNC_DATE
  is '����ʱ��';
