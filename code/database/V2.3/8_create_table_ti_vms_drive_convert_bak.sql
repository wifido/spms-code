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
  is '驾驶员换班信息(删除数据备份)';
-- Add comments to the columns 
comment on column TI_VMS_DRIVE_CONVERT_BAK.ID
  is '主键ID';
comment on column TI_VMS_DRIVE_CONVERT_BAK.VEHICLE_CODE
  is '车牌号';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_MEMBER
  is '换班驾驶员';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_MILES
  is '换班里程';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_TM
  is '换班时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVE_SPAN
  is '本次行驶时长秒';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DRIVING_LOG_ITEM_ID
  is '行驶日志明细主键ID';
comment on column TI_VMS_DRIVE_CONVERT_BAK.START_MILES
  is '行驶日志明细出车里程';
comment on column TI_VMS_DRIVE_CONVERT_BAK.END_MILES
  is '行驶日志明细收车里程';
comment on column TI_VMS_DRIVE_CONVERT_BAK.START_TM
  is '行驶日志明细出车时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.END_TM
  is '行驶日志明细收车时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT_BAK.CREATED_EMP_CODE
  is '创建时间';
comment on column TI_VMS_DRIVE_CONVERT_BAK.CREATED_TM
  is '创建人';
comment on column TI_VMS_DRIVE_CONVERT_BAK.MODIFIED_EMP_CODE
  is '修改人';
comment on column TI_VMS_DRIVE_CONVERT_BAK.MODIFIED_TM
  is '修改时间';
comment on column TI_VMS_DRIVE_CONVERT_BAK.DELETE_TM
  is '删除时间';
comment on column TI_VMS_DRIVE_CONVERT_BAK.SYNC_FLAG
  is '同步状态';
comment on column TI_VMS_DRIVE_CONVERT_BAK.SYS_TM
  is '同步时间';
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_VMS_DRIVE_CONVERT_BAK
  add constraint PK_TI_VMS_DRIVE_CONVERT_BAK primary key (ID);
-- Create/Recreate indexes 
create index INX_VMS_CONVERT_BAR_ITEMID on TI_VMS_DRIVE_CONVERT_BAK (DRIVING_LOG_ITEM_ID);
create index INX_VMS_DRIVE_CONVERT_BAK_TM on TI_VMS_DRIVE_CONVERT_BAK (START_TM);

grant all on TI_VMS_DRIVE_CONVERT_BAK to spmsetl ;
