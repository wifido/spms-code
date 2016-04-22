-- Create table
create table TI_ZTHR_ETL_HR_EMF_TMP
(
  EMP_ID             NUMBER(38) not null,
  EMP_CODE           VARCHAR2(20),
  EMP_NAME           VARCHAR2(500),
  EMP_DUTY_NAME      VARCHAR2(255),
  DEPT_ID            NUMBER(19),
  GROUP_ID           NUMBER(38),
  CREATE_TM          DATE,
  MODIFIED_TM        DATE,
  CREATE_EMP_CODE    VARCHAR2(20),
  MODIFIED_EMP_CODE  VARCHAR2(20),
  WORK_TYPE          NUMBER(2),
  EMAIL              VARCHAR2(100),
  DIMISSION_DT       DATE,
  SF_DATE            DATE,
  EMP_POST_TYPE      VARCHAR2(1),
  IS_HAVE_COMMISSION VARCHAR2(1),
  POSITION_ATTR      VARCHAR2(60),
  DUTY_SERIAL        VARCHAR2(150),
  VERSION_NUMBER     NUMBER,
  PERSON_TYPE        VARCHAR2(90),
  PERSG              VARCHAR2(4),
  PERSG_TXT          VARCHAR2(150),
  PERSK              VARCHAR2(6),
  PERSK_TXT          VARCHAR2(150),
  DATE_FROM          DATE,
  LAST_ZNO           VARCHAR2(36),
  CANCEL_FLAG        VARCHAR2(10),
  DATA_SOURCE        VARCHAR2(1),
  EFFECTIVE_DATE     DATE,
  ORG_ASS_DATE       DATE,
  NET_CODE           VARCHAR2(36)
);
-- Add comments to the table 
comment on table TI_ZTHR_ETL_HR_EMF_TMP
  is '��Ա������Ϣ����ʱ��';
-- Add comments to the columns 
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_ID
  is 'SAP��Ա��Ϣ��ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_CODE
  is '���ţ�ϵͳ�Զ����ɣ���100000000��ʼ;�ڲ���Աʹ�����ʹ���)';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_NAME
  is '����';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_DUTY_NAME
  is 'ְλ';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DEPT_ID
  is '����ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.GROUP_ID
  is 'С��ID';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CREATE_TM
  is '����ʱ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.MODIFIED_TM
  is '�޸�ʱ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CREATE_EMP_CODE
  is '�����˹���';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.MODIFIED_EMP_CODE
  is '�޸��˹���';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.WORK_TYPE
  is '�ù�����:11-���ں�ͬ�ơ�12-������ǲ��13-ҵ�������14-���ؼ�ϰ����15-���ݷ�Ƹ��21-���ں�ͬ�ơ�22-Сʱ����23-��

�ʡ�24-ʵϰ����25-�����ǲ��26-�ڹ���ѧ��99-��Ա����0-������6-�����8-����9-���˳а���Ӫ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMAIL
  is '��������';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DIMISSION_DT
  is '��ְ����';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.SF_DATE
  is '��ְ����';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EMP_POST_TYPE
  is '��λ���ͣ�1-����Ա��2-����Ա��3-�ֹܡ�4-�ͷ���';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.IS_HAVE_COMMISSION
  is '�ֹ���Ա��Ϣ �Ƿ���������� 0�����룬1����';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.POSITION_ATTR
  is '�������ͣ�һ�ߡ����ߣ�';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DUTY_SERIAL
  is '��λ����';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.VERSION_NUMBER
  is '�汾��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSON_TYPE
  is '��Ա�����ı�';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSG
  is 'Ա����:A:ȫ�����ù� C:��ȫ�����ù�';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSG_TXT
  is 'Ա�����ı�';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSK
  is 'Ա������';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.PERSK_TXT
  is 'Ա�������ı�';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DATE_FROM
  is '���뵱ǰ����ʱ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.LAST_ZNO
  is '��һ�������';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.CANCEL_FLAG
  is '��ְ��־';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.DATA_SOURCE
  is '����Դ���ͣ�1-SPMS;2-SAP';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.EFFECTIVE_DATE
  is '��Чʱ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.ORG_ASS_DATE
  is 'ת��ʱ��';
comment on column TI_ZTHR_ETL_HR_EMF_TMP.NET_CODE
  is '��ǰ�������';
-- Create/Recreate indexes 
create index IDX_TI_ZTHR_ETL_HR_EMF_TMP on TI_ZTHR_ETL_HR_EMF_TMP (EMP_CODE);
