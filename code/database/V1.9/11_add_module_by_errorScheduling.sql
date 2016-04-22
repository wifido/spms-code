

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
     WHERE m.module_code='spmsSysMaintenance'),
   '排班错误数据查询',
   'errorSchedulingData',
   '排班错误数据查询',
   '',
   '4',
   '1',
   '/common/toErrorSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'spmsSysMaintenance')),
   null,
   '/index.htm');

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
     WHERE m1.module_code = 'errorSchedulingData'),
   '查询',
   'queryErrorSchedulingData',
   '查询',
   '',
   7,
   1,
   '/common/queryErrorSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'errorSchedulingData')),
   null,
   '/index.htm');


   
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
     WHERE m1.module_code = 'errorSchedulingData'),
   '导出',
   'exportErrorSchedulingData',
   '导出',
   '',
   7,
   1,
   '/common/exportErrorSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'errorSchedulingData')),
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
     WHERE m.module_code='spmsSysMaintenance'),
   '推送排班数据至SAP',
   'syncSchedulingData',
   '推送排班数据至SAP',
   '',
   '4',
   '1',
   '/common/toSyncSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'spmsSysMaintenance')),
   null,
   '/index.htm');


   
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
     WHERE m1.module_code = 'syncSchedulingData'),
   '导出',
   'exportSyncSchedulingData',
   '导出',
   '',
   7,
   1,
   '/common/exportSyncSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'syncSchedulingData')),
   null,
   '/index.htm');


   
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
     WHERE m1.module_code = 'syncSchedulingData'),
   '查询',
   'querySyncSchedulingData',
   '查询',
   '',
   7,
   1,
   '/common/querySyncSchedulingData.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'syncSchedulingData')),
   null,
   '/index.htm');
commit;