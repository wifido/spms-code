update TS_MODULE t
   set t.action_url = '', t.module_code = 'dispatch'
 where t.module_id = '106';
 commit;

 INSERT INTO TS_MODULE(module_id,
                 parent_id,
                 module_name,
                 module_code,
                 module_desc,
                 module_type,
                 app_type,
                 action_url,
                 sort)
 VALUES(SEQ_TS_Module.nextval,
              (SELECT m1.module_id
                 FROM ts_module m1
                 JOIN ts_module m2
                   ON m1.parent_id = m2.module_id
                WHERE m1.module_code = 'dispatch'),
              'һ���Ű�',
              'initAction',
              'һ���Ű�',
              '4',
              '1',
              '/dispatch/initAction.action',
              (SELECT NVL(MAX(S.SORT), 0) + 1
                 FROM TS_MODULE S
                WHERE S.PARENT_ID =
                      (SELECT T.MODULE_ID
                         FROM TS_MODULE T
                        WHERE T.MODULE_CODE = 'dispatch')));
commit;  

update ts_module t
   set t.parent_id =
       (select ts.module_id
          from ts_module ts
         where ts.parent_id = '106'
           and ts.module_name = 'һ���Ű�')
 where t.parent_id = '106'
   and t.module_name <> 'һ���Ű�';
 commit;


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
     WHERE m1.module_code = 'dispatch'),
   'ά�����������',
   'initMaintenanceMailAction',
   'ά�����������',
   '4',
   '1',
   '/dispatch/initMaintenanceMailAction.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.MODULE_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'dispatch')));
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
     WHERE m.module_code='initMaintenanceMailAction'),
   '��ѯ',
   'maintenanceMail_query',
   '��ѯ',
   '',
   '7',
   '1',
   '/dispatch/maintenanceMail_query.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'initMaintenanceMailAction')),
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
     WHERE m.module_code='initMaintenanceMailAction'),
   '����',
   'maintenanceMail_add',
   '����',
   '',
   '7',
   '1',
   '/dispatch/maintenanceMail_add.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'initMaintenanceMailAction')),
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
     WHERE m.module_code='initMaintenanceMailAction'),
   '�޸�',
   'maintenanceMail_edit',
   '�޸�',
   '',
   '7',
   '1',
   '/dispatch/maintenanceMail_edit.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'initMaintenanceMailAction')),
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
     WHERE m.module_code='initMaintenanceMailAction'),
   'ɾ��',
   'maintenanceMail_delete',
   'ɾ��',
   '',
   '7',
   '1',
   '/dispatch/maintenanceMail_delete.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID =
           (SELECT T.MODULE_ID
              FROM TS_MODULE T
             WHERE T.MODULE_CODE = 'initMaintenanceMailAction')),
   null,
   '/index.htm');  
   
   commit;
   
         