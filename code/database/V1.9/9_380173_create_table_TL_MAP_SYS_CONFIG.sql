
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
  is '系统配置表(序列：SEQ_BASE)';
-- Add comments to the columns 
comment on column TL_MAP_SYS_CONFIG.ID
  is '主键';
comment on column TL_MAP_SYS_CONFIG.KEY_NAME
  is 'key的名称';
comment on column TL_MAP_SYS_CONFIG.KEY_VALUE
  is 'key的值';
comment on column TL_MAP_SYS_CONFIG.KEY_DESC
  is 'key的描述';
comment on column TL_MAP_SYS_CONFIG.CREATED_TM
  is '创建时间';
comment on column TL_MAP_SYS_CONFIG.MODIFIED_TM
  is '修改时间';

-- Create/Recreate indexes 
create index IDX_MAP_SYS_CONFIG on TL_MAP_SYS_CONFIG (KEY_NAME);