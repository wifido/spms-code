INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='operationReport'),
   '历史排班修改记录',
   'historyModifyScheduleRecord',
   '历史排班修改记录',
   '',
   '4',
   '1',
   '/report/schedulingModify.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'operationReport')),
   null,
   '/index.htm');
   
   INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='historyModifyScheduleRecord'),
   '查询',
   'query_schedulingModify',
   '查询',
   '',
   '7',
   '1',
   '/report/query_schedulingModify.action', 
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'historyModifyScheduleRecord')),
   null,
   '/index.htm');  

 INSERT INTO ts_module
  (MODULE_ID,
   PARENT_ID,
   MODULE_NAME,
   MODULE_CODE,
   MODULE_DESC,
   MODULE_ICON,
   MODULE_TYPE,
   APP_TYPE,
   ACTION_URL,
   SORT,
   BUNDLE_ID,
   HELP_URL)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m.module_id
      FROM ts_module m
     WHERE m.module_code='historyModifyScheduleRecord'),
   '导出',
   'export_schedulingModify',
   '导出',
   '',
   '7',
   '1',
   '/report/export_schedulingModify.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'historyModifyScheduleRecord')),
   null,
   '/index.htm');  
   
commit;