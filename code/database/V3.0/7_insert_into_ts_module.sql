INSERT INTO TS_MODULE
  (module_id,
   parent_id,
   module_name,
   module_code,
   module_desc,
   module_type,
   app_type,
   action_url,
   sort)
VALUES
  (SEQ_TS_Module.nextval,
   (SELECT m1.module_id
      FROM ts_module m1
      JOIN ts_module m2
        ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'spmsSysMaintenance'),
   '存储过程执行日志',
   'procedureExecutionLog',
   '存储过程执行日志',
   '4',
   '1',
   '/common/procedureExecutionLog.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'spmsSysMaintenance')));
commit;