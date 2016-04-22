
insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710501, 107105, '导出', 'exportSchedulingTable', '导出', '', 7, 1, '/report/exportSchedulingTable.action', 2, null, '/index.htm');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10710502, 107105, '查询', 'querySchedulingTable', '查询', '', 7, 1, '/report/querySchedulingTable.action', 1, null, '/index.htm');

commit;