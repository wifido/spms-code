insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107, 1, '运作报表', 'operationReport', '运作报表', '', 4, 1, '', 1, null, '');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107101, 107, '排班明细报表', 'SchedulingDetailedReports', '排班明细报表', '', 4, 1, '/report/detailedReports.action', 2, null, '');

insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107102, 107, '排班报表', 'toSchedulingInputStatisticalReports', '排班报表', '', 4, 1, '/report/toSchedulingInputStatisticalReports.action', 3, null, '');


-----------运作排班明细报表权限控制

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
  select round(DBMS_RANDOM.VALUE * 1000000),
         t.module_id,
         '查询',
         'lineQuery',
         '查询',
         '',
         7,
         1,
         '/report/report_querySchedule.action',
         1,
         null,
         '/index.htm'
    from ts_module t
   where t.module_name = '排班明细报表';

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
  select round(DBMS_RANDOM.VALUE * 1000000),
         t.module_id,
         '导出',
         'lineQuery',
         '导出',
         '',
         7,
         1,
         '/report/report_exportSchedule.action',
         4,
         null,
         '/index.htm'
    from ts_module t
   where t.module_name = '排班明细报表';
 
-----------运作排班报表权限控制

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
  select round(DBMS_RANDOM.VALUE * 1000000),
         t.module_id,
         '查询',
         'lineQuery',
         '查询',
         '',
         7,
         1,
         '/report/querySchedulingInputStatistical.action',
         1,
         null,
         '/index.htm'
    from ts_module t
   where t.module_name = '排班报表';

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
  select round(DBMS_RANDOM.VALUE * 1000000),
         t.module_id,
         '导出',
         'lineQuery',
         '导出',
         '',
         7,
         1,
         '/report/export.action',
         4,
         null,
         '/index.htm'
    from ts_module t
   where t.module_name = '排班报表';

commit;
   