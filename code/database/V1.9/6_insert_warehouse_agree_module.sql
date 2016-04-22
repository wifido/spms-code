insert into ts_module
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
values
  (SEQ_TS_Module.nextval,
   (SELECT m1.module_id
      FROM ts_module m1
      JOIN ts_module m2
        ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'warehouse'),
   '排班吻合率',
   'toWarehouseCoincidencePage',
   '排班吻合率',
   '',
   4,
   1,
   '/warehouse/toWarehouseCoincidencePage.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'warehouse')),
   null,
   null);
   
   
insert into ts_module
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
values
  (SEQ_TS_Module.nextval,
   (SELECT m1.module_id
      FROM ts_module m1
      JOIN ts_module m2
        ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'toWarehouseCoincidencePage'),
   '查询',
   'query_WarehouseCoincidenceRate',
   '查询',
   '',
   7,
   1,
   '/warehouse/query_WarehouseCoincidenceRate.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'toWarehouseCoincidencePage')),
   null,
   '/index.htm');
   commit;
   
insert into ts_module
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
values
  (SEQ_TS_Module.nextval,
   (SELECT m1.module_id
      FROM ts_module m1
      JOIN ts_module m2
        ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'toWarehouseCoincidencePage'),
   '导出',
   'export_WarehouseCoincidenceRate',
   '导出',
   '',
   7,
   1,
   '/warehouse/export_WarehouseCoincidenceRate.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'toWarehouseCoincidencePage')),
   null,
   '/index.htm');
   commit;