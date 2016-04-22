insert into etlmgr.etl_incr_param
  (
   FOLDER,
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
  (
   'SPMS_SPMS',
   'WF_M_TT_SCH_EMP_ATTENCE_CLASS',
   'S_M_TT_SCH_EMP_ATTENCE_CLASS',
   'spms',
   'TT_SCH_EMP_ATTENCE_CLASS',
   'TIME',
   'CREATE_TM',
   'INIT',
   '1900-01-01 00:00:00' ,
   null,
   null,
   null,
   'YYYY-MM-DD HH24:MI:SS',
   0,
   0);
