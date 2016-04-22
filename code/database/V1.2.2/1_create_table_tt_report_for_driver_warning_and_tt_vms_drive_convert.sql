prompt PL/SQL Developer import file
prompt Created on 2015年1月9日 by sfit0505
set feedback off
set define off
prompt Creating TT_REPORT_FOR_DRIVER_WARNING...
create table TT_REPORT_FOR_DRIVER_WARNING
(
  ID              VARCHAR2(50),
  VEHICLE_CODE    VARCHAR2(50),
  DRIVER_IDENTIFY VARCHAR2(25),
  DRIVE_DAY       DATE,
  DEPT_CODE       VARCHAR2(20),
  CREATED_TM      DATE
)
;

alter table TT_REPORT_FOR_DRIVER_WARNING
  add constraint PK_ID primary key (ID);
  
comment on column TT_REPORT_FOR_DRIVER_WARNING.ID
  is 'ID';
comment on column TT_REPORT_FOR_DRIVER_WARNING.VEHICLE_CODE
  is '车牌号';
comment on column TT_REPORT_FOR_DRIVER_WARNING.DRIVER_IDENTIFY
  is '员工工号';
comment on column TT_REPORT_FOR_DRIVER_WARNING.DRIVE_DAY
  is '换班时间<Y-m-d h:i:s>';
comment on column TT_REPORT_FOR_DRIVER_WARNING.DEPT_CODE
  is '网点代码';
comment on column TT_REPORT_FOR_DRIVER_WARNING.CREATED_TM
  is '创建时间';
create index IDX_DATE on TT_REPORT_FOR_DRIVER_WARNING (DRIVE_DAY);
create index IDX_DATE_AND_DEPT_CODE on TT_REPORT_FOR_DRIVER_WARNING (DRIVER_IDENTIFY, DRIVE_DAY);

prompt Creating TT_VMS_DRIVE_CONVERT...
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
  SYNC_FLAG           NUMBER(2) default 0
)
;
comment on table TT_VMS_DRIVE_CONVERT
  is '驾驶员换班信息';
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
  is '同步状态,1为已经同步';
create index INX_VMS_DRIVE_CONVERT_DETID on TT_VMS_DRIVE_CONVERT (DRIVING_LOG_ITEM_ID);
create index INX_VMS_DRIVE_CONVERT_DM on TT_VMS_DRIVE_CONVERT (DRIVE_MEMBER);
create index INX_VMS_DRIVE_CONVERT_TM on TT_VMS_DRIVE_CONVERT (START_TM);
create index INX_VMS_DRIVE_ID on TT_VMS_DRIVE_CONVERT (ID);

prompt Disabling triggers for TT_REPORT_FOR_DRIVER_WARNING...
alter table TT_REPORT_FOR_DRIVER_WARNING disable all triggers;
prompt Disabling triggers for TT_VMS_DRIVE_CONVERT...
alter table TT_VMS_DRIVE_CONVERT disable all triggers;
prompt Deleting TT_VMS_DRIVE_CONVERT...
delete from TT_VMS_DRIVE_CONVERT;
commit;
prompt Deleting TT_REPORT_FOR_DRIVER_WARNING...
delete from TT_REPORT_FOR_DRIVER_WARNING;
commit;
prompt Done.
