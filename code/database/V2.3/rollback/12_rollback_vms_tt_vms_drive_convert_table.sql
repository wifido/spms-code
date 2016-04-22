-- Create table
create table TT_VMS_DRIVE_CONVERT
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
  SYNC_FLAG           NUMBER(1) default 0
);
-- Add comments to the table 
comment on table TT_VMS_DRIVE_CONVERT
  is '驾驶员换班信息';
-- Add comments to the columns 
comment on column TT_VMS_DRIVE_CONVERT.ID
  is '主键ID';
comment on column TT_VMS_DRIVE_CONVERT.VEHICLE_CODE
  is '车牌号';
comment on column TT_VMS_DRIVE_CONVERT.DRIVE_MEMBER
  is '换班驾驶员';
comment on column TT_VMS_DRIVE_CONVERT.DRIVE_MILES
  is '换班里程';
comment on column TT_VMS_DRIVE_CONVERT.DRIVE_TM
  is '换班时间<Y-m-d h:i:s>';
comment on column TT_VMS_DRIVE_CONVERT.DRIVE_SPAN
  is '本次行驶时长秒';
comment on column TT_VMS_DRIVE_CONVERT.DRIVING_LOG_ITEM_ID
  is '行驶日志明细主键ID';
comment on column TT_VMS_DRIVE_CONVERT.START_MILES
  is '行驶日志明细出车里程';
comment on column TT_VMS_DRIVE_CONVERT.END_MILES
  is '行驶日志明细收车里程';
comment on column TT_VMS_DRIVE_CONVERT.START_TM
  is '行驶日志明细出车时间<Y-m-d h:i:s>';
comment on column TT_VMS_DRIVE_CONVERT.END_TM
  is '行驶日志明细收车时间<Y-m-d h:i:s>';
comment on column TT_VMS_DRIVE_CONVERT.CREATED_EMP_CODE
  is '创建时间';
comment on column TT_VMS_DRIVE_CONVERT.CREATED_TM
  is '创建人';
comment on column TT_VMS_DRIVE_CONVERT.MODIFIED_EMP_CODE
  is '修改人';
comment on column TT_VMS_DRIVE_CONVERT.MODIFIED_TM
  is '修改时间';
comment on column TT_VMS_DRIVE_CONVERT.SYNC_FLAG
  is '预警同步状态,1为已同步';
-- Grant/Revoke object privileges 
grant select on TT_VMS_DRIVE_CONVERT to SPMSETL;
