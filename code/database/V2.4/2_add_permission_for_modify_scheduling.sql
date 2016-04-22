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
     WHERE m.module_code='newprocess'),
   '修改运作历史排班权限',
   'modify_operation_historty_scheduling',
   '修改运作历史排班权限',
   '',
   '7',
   '1',
   '/schedulingMgt/modify_historty_scheduling.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'newprocess')),
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
     WHERE m.module_code='toWarehouseSchedulingPage'),
   '修改历史排班',
   'modify_warehouse_historty_scheduling',
   '修改历史排班',
   '',
   '7',
   '1',
   '/schedulingMgt/modify_warehouse_historty_scheduling.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'toWarehouseSchedulingPage')),
   null,
   '/index.htm');  
   
commit;
