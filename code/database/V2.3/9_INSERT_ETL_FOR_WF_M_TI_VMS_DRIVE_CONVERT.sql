insert into etlmgr.etl_incr_param
  (FOLDER,
   WORKFLOW,
   SESSION_NAME,
   TARGET_OWNER,
   TARGET_TABLE,
   INCR_TYPE,
   INCR_COLUMN,
   LAST_RUN_STATE,
   INCR_START_VALUE,
   INCR_END_VALUE,
   LAST_START_TIME,
   LAST_END_TIME,
   TIME_MODE,
   TIME_RANGE,
   TIME_BEFORE_NOW)
values
  ('SPMS_SPMS',
   'WF_M_TI_VMS_DRIVE_CONVERT',
   'S_M_TI_VMS_DRIVE_CONVERT',
   'SPMS',
   'TI_VMS_DRIVE_CONVERT',
   'TIME',
   'CREATED_TM',
   'INIT',
   '2015-09-01 00:00:00',
   '2015-09-01 00:00:00',
   '',
   '',
   'YYYY-MM-DD HH24:MI:SS',
   0,
   0);

insert into etlmgr.etl_incr_param
  (FOLDER,
   WORKFLOW,
   SESSION_NAME,
   TARGET_OWNER,
   TARGET_TABLE,
   INCR_TYPE,
   INCR_COLUMN,
   LAST_RUN_STATE,
   INCR_START_VALUE,
   INCR_END_VALUE,
   LAST_START_TIME,
   LAST_END_TIME,
   TIME_MODE,
   TIME_RANGE,
   TIME_BEFORE_NOW)
values
  ('SPMS_SPMS',
   'WF_M_TI_VMS_DRIVE_CONVERT_BAK',
   'S_M_TI_VMS_DRIVE_CONVERT_BAK',
   'SPMS',
   'TI_VMS_DRIVE_CONVERT_BAK',
   'TIME',
   'DELETE_TM',
   'INIT',
   '2015-09-01 00:00:00',
   '2015-09-01 00:00:00',
   '',
   '',
   'YYYY-MM-DD HH24:MI:SS',
   0,
   0);
commit;
