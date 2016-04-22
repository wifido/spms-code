delete  from  ts_module where parent_id =(select module_id from ts_module where module_name ='值班人员统计表') and module_type='7';
commit;

delete  from  ts_module where module_id =(select module_id from ts_module where module_name ='值班人员统计表') and module_type='4';
commit;
