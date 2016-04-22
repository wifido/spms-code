insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (108104, 108, '仓管员培训信息', 'warehousetrainingInfoAction', '仓管员培训信息', '', 4, 1, '/warehouse/detailedTraining.action','4', null, null);

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10810413, 108104, '导出', 'warehouexporttraining', '导出', '', 7, 1, '/warehouse/training_export.action', 4, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10810412, 108104, '导入', 'warehouimporttraining', '导入', '', 7, 1, '/warehouse/training_import.action', 3, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10810411, 108104, '删除', 'warehoudeletetraining', '删除', '', 7, 1, '/warehouse/training_delete.action', 2, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10810410, 108104, '查询', 'warehouquerytraining', '查询', '', 7, 1, '/warehouse/training_query.action', 1, null, '/index.htm');


insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105107, 105, '运作培训信息', 'operationtrainingInfoAction', '运作培训信息', '', 4, 1, '/operation/detailedTraining.action', 7, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510713, 105107, '导出', 'operationexporttraining', '导出', '', 7, 1, '/operation/training_export.action', 4, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510712, 105107, '导入', 'operationimporttraining', '导入', '', 7, 1, '/operation/training_import.action', 3, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510711, 105107, '删除', 'operationdeletetraining', '删除', '', 7, 1, '/operation/training_delete.action', 2, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510710, 105107, '查询', 'operationquerytraining', '查询', '', 7, 1, '/operation/training_query.action', 1, null, '/index.htm');




insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107103, 107, '排班吻合率', 'schedulingCoincidenceRate', '排班吻合率', '', 4, 1, '/report/schedulingConicidenceRate.action', 3, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710301, 107103, '导出', 'exportCoincidenceRate', '导出', '', 7, 1, '/report/queryCoincidenceRate.action', 2, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710302, 107103, '查询', 'queryCoincidenceRate', '查询', '', 7, 1, '/report/exportCoincidenceRate.action', 1, null, '/index.htm');


insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107104, 107, '工序吻合率', 'processCoincidenceRate', '排班吻合率', '', 4, 1, '/report/processConicidenceRate.action', 3, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710401, 107104, '导出', 'exportProcessCoincidenceRate', '导出', '', 7, 1, '/report/queryCoincidenceRate.action', 2, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710402, 107104, '查询', 'queryProcessCoincidenceRate', '查询', '', 7, 1, '/report/exportCoincidenceRate.action', 1, null, '/index.htm');




commit;


