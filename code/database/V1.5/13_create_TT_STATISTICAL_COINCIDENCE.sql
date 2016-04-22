-- Create table
create table TT_STATISTICAL_COINCIDENCE
(
  DEPT_ID               VARCHAR2(18),
  YEAR_MONTH            VARCHAR2(10),
  COINCIDENCE_RATE      VARCHAR2(10),
  COINCIDENCERATE_COUNT NUMBER(10),
  DEPT_COUNT            NUMBER(10),
  POSITION_TYPE         NUMBER(1),
  DEPT_CODE             VARCHAR2(30)
);
-- Add comments to the table 
comment on table TT_STATISTICAL_COINCIDENCE
  is '�Ǻ���ͳ�Ʊ�';
-- Add comments to the columns 
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_ID
  is '����ID';
comment on column TT_STATISTICAL_COINCIDENCE.YEAR_MONTH
  is '����';
comment on column TT_STATISTICAL_COINCIDENCE.COINCIDENCE_RATE
  is '�Ǻ���';
comment on column TT_STATISTICAL_COINCIDENCE.COINCIDENCERATE_COUNT
  is '�Ǻ���';
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_COUNT
  is '��������';
comment on column TT_STATISTICAL_COINCIDENCE.POSITION_TYPE
  is '1��ʶΪ�Ű࣬2��ʶΪ����';
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_CODE
  is '�������';
