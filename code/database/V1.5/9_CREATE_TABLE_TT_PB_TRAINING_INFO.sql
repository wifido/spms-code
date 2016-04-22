-- Create table
create table TT_PB_TRAINING_INFO
(
  ID                NUMBER(38) not null,
  DEPARTMENT_CODE   VARCHAR2(30),
  TRAINING_CODE     VARCHAR2(30),
  EMPLOYEE_CODE     VARCHAR2(30),
  YEARS_MONTH       VARCHAR2(30),
  DAY_OF_MONTH      VARCHAR2(30),
  POST_TYPE         NUMBER(1),
  CREATE_TM         DATE default sysdate,
  MODIFIED_TM       DATE default sysdate,
  CREATE_EMP_CODE   VARCHAR2(20),
  MODIFIED_EMP_CODE VARCHAR2(20),
  DESCRIPTION       VARCHAR2(200)
);
-- Add comments to the table 
comment on table TT_PB_TRAINING_INFO
  is '��ѵ��Ϣ��';
-- Add comments to the columns 
comment on column TT_PB_TRAINING_INFO.ID
  is '����ID';
comment on column TT_PB_TRAINING_INFO.DEPARTMENT_CODE
  is '�������';
comment on column TT_PB_TRAINING_INFO.TRAINING_CODE
  is '��ѵ����';
comment on column TT_PB_TRAINING_INFO.EMPLOYEE_CODE
  is 'Ա��';
comment on column TT_PB_TRAINING_INFO.YEARS_MONTH
  is '��ѵ����';
comment on column TT_PB_TRAINING_INFO.DAY_OF_MONTH
  is '��ѵ���ڣ������գ�';
comment on column TT_PB_TRAINING_INFO.POST_TYPE
  is '��λ���ͣ�1����������2����ֹܣ�';
comment on column TT_PB_TRAINING_INFO.CREATE_TM
  is '����ʱ��';
comment on column TT_PB_TRAINING_INFO.MODIFIED_TM
  is '�޸�ʱ��';
comment on column TT_PB_TRAINING_INFO.CREATE_EMP_CODE
  is '�����߹���';
comment on column TT_PB_TRAINING_INFO.MODIFIED_EMP_CODE
  is '�޸��߹���';
comment on column TT_PB_TRAINING_INFO.DESCRIPTION
  is '������Ϣ';
