update TS_MODULE t
   set t.action_url = '/dispatch/initAction.action', t.module_code = 'initAction'
 where t.module_id = '106';
 commit;


update ts_module t
   set t.parent_id =106 
 where t.module_id in (106446,
273252,
106441,
106442,
106443,
106444,
106445);
commit;

delete TS_MODULE t where t.parent_id in (
select t.module_id from  TS_MODULE t  where t.parent_id = '106' and t.module_type = '4')
commit;

delete  ts_module t  where t.parent_id = '106' and t.module_type = '4';
commit;