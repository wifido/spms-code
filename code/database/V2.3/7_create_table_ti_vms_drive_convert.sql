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
  is '驾驶员换班信息';
-- Add comments to the columns 
comment on column TI_VMS_DRIVE_CONVERT.ID
  is '主键ID';
comment on column TI_VMS_DRIVE_CONVERT.VEHICLE_CODE
  is '车牌号';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_MEMBER
  is '换班驾驶员';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_MILES
  is '换班里程';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_TM
  is '换班时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.DRIVE_SPAN
  is '本次行驶时长秒';
comment on column TI_VMS_DRIVE_CONVERT.DRIVING_LOG_ITEM_ID
  is '行驶日志明细主键ID';
comment on column TI_VMS_DRIVE_CONVERT.START_MILES
  is '行驶日志明细出车里程';
comment on column TI_VMS_DRIVE_CONVERT.END_MILES
  is '行驶日志明细收车里程';
comment on column TI_VMS_DRIVE_CONVERT.START_TM
  is '行驶日志明细出车时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.END_TM
  is '行驶日志明细收车时间<Y-m-d h:i:s>';
comment on column TI_VMS_DRIVE_CONVERT.CREATED_EMP_CODE
  is '创建时间';
comment on column TI_VMS_DRIVE_CONVERT.CREATED_TM
  is '创建人';
comment on column TI_VMS_DRIVE_CONVERT.MODIFIED_EMP_CODE
  is '修改人';
comment on column TI_VMS_DRIVE_CONVERT.MODIFIED_TM
  is '修改时间';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_FLAG
  is '同步状态';
  comment on column TI_VMS_DRIVE_CONVERT.SAP_SYNC_FLAG
  is '是否处理到SAP接口表 0：未处理 1：已处理  3：处理中';
comment on column TI_VMS_DRIVE_CONVERT.SYNC_TM
  is '同步时间';  

-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_VMS_DRIVE_CONVERT
  add constraint PK_TI_VMS_DRIVE_CONVERT primary key (ID);
-- Create/Recreate indexes 
create index INX_VMS_DRIVE_CONVERT_DETID on TI_VMS_DRIVE_CONVERT (DRIVING_LOG_ITEM_ID);
create index INX_VMS_DRIVE_CONVERT_DM on TI_VMS_DRIVE_CONVERT (DRIVE_MEMBER);
create index INX_VMS_DRIVE_CONVERT_TM on TI_VMS_DRIVE_CONVERT (START_TM);

grant all on TI_VMS_DRIVE_CONVERT to spmsetl;
