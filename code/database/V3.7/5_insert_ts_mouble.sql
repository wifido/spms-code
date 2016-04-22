
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
     WHERE m.module_code = 'lineConfigure'),
   '导入机动配班',
   'importDynamicLineConfigure_lineConfigure',
   '导入机动配班',
   '',
   '7',
   '1',
   '/driver/importDynamicLineConfigure_lineConfigure.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'lineConfigure')),
   null,
   '/index.htm');
commit;

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
     WHERE m.module_code = 'driverSchedulingManage'),
   '导出未排班',
   'exportNotSchedulingEmployee_scheduling',
   '导出未排班',
   '',
   '7',
   '1',
   '/driver/exportNotSchedulingEmployee_scheduling.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'driverSchedulingManage')),
   null,
   '/index.htm');

commit;
