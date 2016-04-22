-- Create table
create table TI_VMS_DRIVE_CONVERT_BAK
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
  DELETE_TM           DATE,
  SYNC_FLAG           NUMBER(2) default 0,
  SYS_TM              DATE DEFAULT SYSDATE
);

-- Add comments to the table 
comment on table TI_VMS_DRIVE_CONVERT_BAK
  is '��ʻԱ������Ϣ(ɾ�����ݱ���)';
-- Add comments to the columns 
comment on column TI_VMS_DRIVE_CONVERT_BAK.ID
  is '����ID';
comment on column TI_VMS_DRIVE_CONVERT_BAK.VEHICLE_CODE
  is '���ƺ�';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_MEMBER
  is '�����ʻԱ';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_MILES
  is '�������';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_TM
  is '����ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_SPAN
  is '������ʻʱ����';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVING_LOG_ITEM_ID
  is '��ʻ��־��ϸ����ID';
comment on column TI_VMS_DRIVE_CONVERT_BAK.START_MILES
  is '��ʻ��־��ϸ�������';
comment on column TI_VMS_DRIVE_CONVERT_BAK.END_MILES
  is '��ʻ��־��ϸ�ճ����';
comment on column TI_VMS_DRIVE_CONVERT_BAK.START_TM
  is '��ʻ��־��ϸ����ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.END_TM
  is '��ʻ��־��ϸ�ճ�ʱ��<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.CREATED_EMP_CODE
  is '����ʱ��';
comment on column TI_VMS_DRIVE_CONVERT_BAK.CREATED_TM
  is '������';
comment on column TI_VMS_DRIVE_CONVERT_BAK.MODIFIED_EMP_CODE
  is '�޸���';
comment on column TI_VMS_DRIVE_CONVERT_BAK.MODIFIED_TM
  is '�޸�ʱ��';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DELETE_TM
  is 'ɾ��ʱ��';
comment on column TI_VMS_DRIVE_CONVERT_BAK.SYNC_FLAG
  is 'ͬ��״̬';
comment on column TI_VMS_DRIVE_CONVERT_BAK.SYS_TM
  is 'ͬ��ʱ��';
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_VMS_DRIVE_CONVERT_BAK
  add constraint PK_TI_VMS_DRIVE_CONVERT_BAK primary key (ID);
-- Create/Recreate indexes 
create index INX_VMS_CONVERT_BAR_ITEMID on TI_VMS_DRIVE_CONVERT_BAK (DRIVING_LOG_ITEM_ID);
create index INX_VMS_DRIVE_CONVERT_BAK_TM on TI_VMS_DRIVE_CONVERT_BAK (START_TM);

grant all on TI_VMS_DRIVE_CONVERT_BAK to spmsetl ;
