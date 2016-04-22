-- Create table
create table TI_VMS_DRIVE_CONVERT
(
  ID                  NUMBER(19) not null,
  VEHICLE_CODE        VARCHAR2(50),
  DRIVE_MEMBER        VARCHAR2(25),
  DRIVE_MILES         NUMBER(9),
  DRIVE_TM            DATE,
  DRIVE_SPAN          NUMBER(19),
  DRIVING_LOG_ITEM_ID NUMBER(19),
  START_MILES         NUMBER(9),
  END_MILES           NUMBER(9),
  START_TM            DATE,
  END_TM              DATE,
  CREATED_EMP_CODE    VARCHAR2(25),
  CREATED_TM          DATE,
  MODIFIED_EMP_CODE   VARCHAR2(25),
  MODIFIED_TM         DATE,
  SYNC_FLAG           NUMBER(2) default 0,
  SAP_SYNC_FLAG       NUMBER(1) default 0,
  SYNC_TM             DATE DEFAULT SYSDATE
);

-- Add comments to the table 
comment on table TI_VMS_DRIVE_CONVERT
  is '��ʻԱ������Ϣ';
-- Add comments to the columns 
comment on column TI_VMS_DRIVE_CONVERT.ID
  is '����ID';
comment on column TI_VMS_DRIVE_CONVERT.VEHICLE_CODE
  is '���ƺ�';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_MEMBER
  is '�����ʻԱ';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_MILES
  is '�������';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_TM
  is '����ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_SPAN
  is '������ʻʱ����';
comment on column TI_VMS_DRIVE_CONVERT.DRIVING_LOG_ITEM_ID
  is '��ʻ��־��ϸ����ID';
comment on column TI_VMS_DRIVE_CONVERT.START_MILES
  is '��ʻ��־��ϸ�������';
comment on column TI_VMS_DRIVE_CONVERT.END_MILES
  is '��ʻ��־��ϸ�ճ����';
comment on column TI_VMS_DRIVE_CONVERT.START_TM
  is '��ʻ��־��ϸ����ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.END_TM
  is '��ʻ��־��ϸ�ճ�ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.CREATED_EMP_CODE
  is '����ʱ��';
comment on column TI_VMS_DRIVE_CONVERT.CREATED_TM
  is '������';
comment on column TI_VMS_DRIVE_CONVERT.MODIFIED_EMP_CODE
  is '�޸���';
comment on column TI_VMS_DRIVE_CONVERT.MODIFIED_TM
  is '�޸�ʱ��';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_FLAG
  is 'ͬ��״̬';
  comment on column TI_VMS_DRIVE_CONVERT.SAP_SYNC_FLAG
  is '�Ƿ���SAP�ӿڱ� 0��δ���� 1���Ѵ���  3��������';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_TM
  is 'ͬ��ʱ��';  

-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_VMS_DRIVE_CONVERT
  add constraint PK_TI_VMS_DRIVE_CONVERT primary key (ID);
-- Create/Recreate indexes 
create index INX_VMS_DRIVE_CONVERT_DETID on TI_VMS_DRIVE_CONVERT (DRIVING_LOG_ITEM_ID);
create index INX_VMS_DRIVE_CONVERT_DM on TI_VMS_DRIVE_CONVERT (DRIVE_MEMBER);
create index INX_VMS_DRIVE_CONVERT_TM on TI_VMS_DRIVE_CONVERT (START_TM);

grant all on TI_VMS_DRIVE_CONVERT to spmsetl;
