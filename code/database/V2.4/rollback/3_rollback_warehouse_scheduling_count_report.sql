delete from ts_module t where t.MODULE_CODE ='warehouse_scheduling_count_report';
delete from ts_module t where t.MODULE_CODE ='query_schedulingAnalysisReport';
delete from ts_module t where t.MODULE_CODE ='export_schedulingAnalysisReport';
commit;
drop table TT_WAREH_SCHEDULED_REPORT;
drop sequence SEQ_TT_WAREH_SCHEDULED_REPORT;
drop PROCEDURE WAREH_COUNT_SCHE_REPORT;


