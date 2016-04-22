
delete from ts_module t where t.module_code in ('spmsSysConfig', 'spmsSysMaintenance');
commit;