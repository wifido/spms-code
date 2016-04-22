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
   '�޸�������ʷ�Ű�Ȩ��',
   'modify_operation_historty_scheduling',
   '�޸�������ʷ�Ű�Ȩ��',
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
   '�޸���ʷ�Ű�',
   'modify_warehouse_historty_scheduling',
   '�޸���ʷ�Ű�',
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
