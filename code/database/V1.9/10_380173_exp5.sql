

--ϵͳά���˵�
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
  (seq_ts_module.nextval,
   1,
   'ϵͳά��',
   'spmsSysMaintenance',
   'ϵͳά��',
   '',
   '4',
   '1',
   '',
   1,
   null,
   null);
commit;   
  
--ϵͳ���ò˵�
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
  (seq_ts_module.nextval,
   (SELECT m1.module_id
      FROM ts_module m1
      JOIN ts_module m2 ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'spmsSysMaintenance'
       AND m2.module_code = 'oss'),
   'ϵͳ��������',
   'spmsSysConfig',
   'ϵͳ��������',
   '',
   '4',
   '1',
   '/common/sysConfig.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'spmsSysMaintenance')),
   null,
  '/index.htm');
commit;    
