-- Create table
create table TT_DRIVER_MESSAGE
(
  MSG_ID      NUMBER(38) not null,
  USER_ID     VARCHAR2(50),
  MSG         VARCHAR2(500),
  STATUS      NUMBER(1) default 0,
  SEND_TIME   DATE,
  CREATE_TIME DATE,
  FAIL_DESC   VARCHAR2(500)
);
-- Add comments to the columns 
comment on column TT_DRIVER_MESSAGE.MSG_ID
  is '��Ϣ���';
comment on column TT_DRIVER_MESSAGE.USER_ID
  is '�û�ID';
comment on column TT_DRIVER_MESSAGE.MSG
  is '��Ϣ';
comment on column TT_DRIVER_MESSAGE.STATUS
  is '״̬ 0��δ���� 1�����ͳɹ� 2������ʧ��';
comment on column TT_DRIVER_MESSAGE.SEND_TIME
  is '����ʱ��';
comment on column TT_DRIVER_MESSAGE.CREATE_TIME
  is '��Ϣ����ʱ��';
comment on column TT_DRIVER_MESSAGE.FAIL_DESC
  is '����ʧ��ԭ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_DRIVER_MESSAGE
  add constraint PK_MSG_ID primary key (MSG_ID);
  
  

-- Create table
create table TT_DRIVER_MESSAGE_HIS
(
  MSG_ID      NUMBER(38) not null,
  USER_ID     VARCHAR2(50),
  MSG         VARCHAR2(500),
  STATUS      NUMBER(1) default 0,
  SEND_TIME   DATE,
  CREATE_TIME DATE,
  FAIL_DESC   VARCHAR2(500)
);
-- Add comments to the columns 
comment on column TT_DRIVER_MESSAGE_HIS.MSG_ID
  is '��Ϣ���';
comment on column TT_DRIVER_MESSAGE_HIS.USER_ID
  is '�û�ID';
comment on column TT_DRIVER_MESSAGE_HIS.MSG
  is '��Ϣ';
comment on column TT_DRIVER_MESSAGE_HIS.STATUS
  is '״̬ 0��δ���� 1�����ͳɹ� 2������ʧ��';
comment on column TT_DRIVER_MESSAGE_HIS.SEND_TIME
  is '����ʱ��';
comment on column TT_DRIVER_MESSAGE_HIS.CREATE_TIME
  is '��Ϣ����ʱ��';
comment on column TT_DRIVER_MESSAGE_HIS.FAIL_DESC
  is '����ʧ��ԭ��';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_DRIVER_MESSAGE_HIS
  add constraint PK_MSG_ID_HIS primary key (MSG_ID);
  
  
-- Create sequence 
create sequence SEQ_TT_DRIVER_MESSAGE
minvalue 1
maxvalue 9999999999999999999999
start with 41
increment by 1
cache 20;


