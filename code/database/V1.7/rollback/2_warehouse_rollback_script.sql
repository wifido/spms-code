
drop sequence SEQ_TS_Module;

drop view v_scheduled_time_detail;

delete from ts_module t
 where t.module_code in ('export_warehouseAttendanceDetail',
                         'query_warehouseAttendanceDetail',
                         'warehouseAttendanceDetail',
                         'exportWarehouseAttenceGather',
                         'queryWarehouseAttenceGather',
                         'warehouseAttenceGather');

COMMIT;
