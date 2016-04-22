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
   (SELECT m1.parent_id
      FROM ts_module m1
      JOIN ts_module m2
        ON m1.parent_id = m2.module_id
     WHERE m1.module_code = 'modify_operation_historty_scheduling'),
   '历史排班整月修改权限',
   'historyMonthModifySchedule',
   '历史排班整月修改权限',
   '',
   7,
   1,
   '/schedulingMgt/historyMonthModifySchedule.action',
   (SELECT NVL(MAX(S.SORT), 0) + 1
      FROM TS_MODULE S
     WHERE S.PARENT_ID = (SELECT T.PARENT_ID
                            FROM TS_MODULE T
                           WHERE T.MODULE_CODE = 'modify_operation_historty_scheduling')),
   null,
   '/index.htm');
   commit;


update ts_module t set t.module_name = '历史排班七天修改权限' 
where t.module_code ='modify_operation_historty_scheduling';
commit;
   