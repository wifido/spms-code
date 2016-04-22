-- Create table
create table TT_DISPATCH_MONITOR_REPORT
(
  DEPT_CODE                   VARCHAR2(30),
  DAY_OF_MONTH                DATE,
  HQ_CODE                     VARCHAR2(30),
  AREA_CODE                   VARCHAR2(30),
  FULLTIME_EMP_NUM            NUMBER(10),
  NOT_FULLTIME_EMP_NUM        NUMBER(10),
  FULLTIME_SCHEDULING_NUM     NUMBER(10),
  FULLTIME_REST_NUM           NUMBER(10),
  NOT_FULLTIME_SCHEDULING_NUM NUMBER(10),
  NOT_FULLTIME_REST_NUM       NUMBER(10),
  FULLTIME_NOT_SCHEDULING     NUMBER(10),
  NOT_FULLTIME_NOT_SCHEDULING NUMBER(10),
  DIVISION_CODE               VARCHAR2(30),
  CREATE_DATE                 DATE,
  TYPE_LEVEL                  NUMBER(10)
);
-- Add comments to the table 
comment on table TT_DISPATCH_MONITOR_REPORT
  is 'һ�߼�ر���';
-- Add comments to the columns 
comment on column TT_DISPATCH_MONITOR_REPORT.DEPT_CODE
  is '�������';
comment on column TT_DISPATCH_MONITOR_REPORT.DAY_OF_MONTH
  is '����';
comment on column TT_DISPATCH_MONITOR_REPORT.HQ_CODE
  is '��Ӫ��������';
comment on column TT_DISPATCH_MONITOR_REPORT.AREA_CODE
  is '��������';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_EMP_NUM
  is 'ȫ��������';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_EMP_NUM
  is '��ȫ��������';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_SCHEDULING_NUM
  is 'ȫ�����Ű�����';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_REST_NUM
  is 'ȫ������������';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_SCHEDULING_NUM
  is '��ȫ�����Ű�����';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_REST_NUM
  is '��ȫ������������';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_NOT_SCHEDULING
  is 'ȫ����δ�Ű�����';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_NOT_SCHEDULING
  is '��ȫ����δ�Ű�����';
comment on column TT_DISPATCH_MONITOR_REPORT.DIVISION_CODE
  is '�ֵ㲿����';
comment on column TT_DISPATCH_MONITOR_REPORT.CREATE_DATE
  is '����ʱ��';
comment on column TT_DISPATCH_MONITOR_REPORT.TYPE_LEVEL
  is '���㼶��';
