delete ts_module t where t.module_code = 'errorSchedulingData';
delete ts_module t where t.module_code = 'queryErrorSchedulingData';
delete ts_module t where t.module_code = 'exportErrorSchedulingData';

delete ts_module t where t.module_code = 'syncSchedulingData';
delete ts_module t where t.module_code = 'exportSyncSchedulingData';
delete ts_module t where t.module_code = 'querySyncSchedulingData';
commit;