
-- Create table
create table TL_MAP_SYS_CONFIG
(
  ID          NUMBER(19) not null,
  KEY_NAME    VARCHAR2(90),
  KEY_VALUE   VARCHAR2(900),
  KEY_DESC    VARCHAR2(900),
  CREATED_TM  DATE,
  MODIFIED_TM DATE
);
-- Add comments to the table 
comment on table TL_MAP_SYS_CONFIG
  is 'ϵͳ���ñ�(���У�SEQ_BASE)';
-- Add comments to the columns 
comment on column TL_MAP_SYS_CONFIG.ID
  is '����';
comment on column TL_MAP_SYS_CONFIG.KEY_NAME
  is 'key������';
comment on column TL_MAP_SYS_CONFIG.KEY_VALUE
  is 'key��ֵ';
comment on column TL_MAP_SYS_CONFIG.KEY_DESC
  is 'key������';
comment on column TL_MAP_SYS_CONFIG.CREATED_TM
  is '����ʱ��';
comment on column TL_MAP_SYS_CONFIG.MODIFIED_TM
  is '�޸�ʱ��';

-- Create/Recreate indexes 
create index IDX_MAP_SYS_CONFIG on TL_MAP_SYS_CONFIG (KEY_NAME);