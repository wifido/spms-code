insert into etlmgr.etl_incr_param(FOLDER,
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
values('SPMS_SPMS',
            'WF_M_TI_VMS_DRIVING_LOG_ITEM',
            'S_M_TI_VMS_DRIVING_LOG_ITEM',
            'SPMS',
            'TI_VMS_DRIVING_LOG_ITEM',
            'TIME',
            'CREATED_TM',
            'INIT',
            '2015-07-01 00:00:00',
            '',
            '',
            '',
            'YYYY-MM-DD HH24:MI:SS',
            0,
            0);
commit;