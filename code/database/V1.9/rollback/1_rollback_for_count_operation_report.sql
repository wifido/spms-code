drop table op_attendance_count_report ;
drop procedure count_operation_report;
delete from ts_module t where t.MODULE_CODE='operationStatisticsReport';
delete from ts_module t where t.MODULE_CODE='query_statisticsReport';
delete from ts_module t where t.MODULE_CODE='export_statisticsReport';
commit;

