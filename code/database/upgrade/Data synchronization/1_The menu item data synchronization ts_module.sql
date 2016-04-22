prompt PL/SQL Developer import file
prompt Created on 2014年10月8日 by sfit0505
set feedback off
set define off
prompt Disabling triggers for TS_MODULE...
alter table TS_MODULE disable all triggers;
prompt Disabling foreign key constraints for TS_MODULE...
alter table TS_MODULE disable constraint FK_TS_MODULE;
prompt Deleting TS_MODULE...
delete from TS_MODULE;
commit;
prompt Loading TS_MODULE...

-- 当生产中存在 排班系统时，删除如下SQL
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (1, null, '排班系统', 'oss', '排班系统', null, 2, 1, '/authorization/home.action', 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (101, 1, '权限管理', 'authorization', '管理用户权限', null, 4, 1, null, 1, null, null);
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10101, 101, '用户管理', 'user', '用户管理', null, 4, 1, '/authorization/user.action', 1, null, null);
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10102, 101, '角色管理', 'role', '角色管理', null, 4, 1, '/authorization/role.action', 2, null, null);
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10103, 101, '权限钥匙管理', 'accredit', '权限钥匙管理', null, 4, 1, '/authorization/accredit.action', 3, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (107, 1, '排班明细报表', 'SchedulingDetailedReports', '排班明细报表', null, 4, 1, '/report/detailedReports.action', 1, null, null);


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (106, 1, ' 收派员排班', 'initAction', '收派员排班', null, 4, 1, '/dispatch/initAction.action', 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (106441, 106, '查询', 'querySchedule', '查询', null, 7, 1, '/dispatch/querySchedule.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (106442, 106, '修改', 'updateSchedule', '修改', null, 7, 1, '/dispatch/dispatch_updateSchedule.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (106443, 106, '导入', 'scheduleUploadFile', '导入', null, 7, 1, '/dispatch/dispatch_scheduleUploadFile.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (106444, 106, '导出', 'exportSchedule', '导出', null, 7, 1, '/dispatch/dispatch_exportSchedule.action', 4, null, '/index.htm');


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105, 1, '运作员', 'group', '运作员', null, 4, 1, null, 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105101, 105, '运作小组管理', 'group1', '运作小组管理', null, 4, 1, '/operation/groupBaseInfo.action', 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510113, 105101, '修改', 'groupInfoEdit', '修改', null, 7, 1, '/operation/groupInfoEdit.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510111, 105101, '查询', 'groupInfoSearch', '查询', null, 7, 1, '/operation/groupInfoSearch.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510112, 105101, '新增', 'groupInfoAdd', '新增', null, 7, 1, '/operation/groupInfoAdd.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510114, 105101, '删除', 'groupInfoDelete', '删除', null, 7, 1, '/operation/groupInfoDelete.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510115, 105101, '导出', 'groupInfoExport', '导出', null, 7, 1, '/operation/groupInfoExport.action', 5, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510116, 105101, '导入', 'groupInfoImport', '导入', null, 7, 1, '/operation/groupInfoImport.action', 6, null, '/index.htm');


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105102, 105, '运作人员管理', 'groupschedulingBase', '运作人员管理', null, 4, 1, '/operation/outEmployee.action', 2, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510210, 105102, '查询', 'queryOutEmployee', '查询', null, 7, 1, '/operation/outEmployeeMgt_queryOutEmployee.action', 0, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510211, 105102, '新增', 'saveEmployee', '新增', null, 7, 1, '/operation/outEmployeeMgt_saveEmployee.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510212, 105102, '修改', 'updateEmployee', '修改', null, 7, 1, '/operation/outEmployeeMgt_updateEmployee.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510213, 105102, '删除', 'deleteEmployee', '删除', null, 7, 1, '/operation/outEmployeeMgt_deleteEmployee.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510214, 105102, '人员信息同步 ', 'outEmployeeMgt', '人员信息同步 ', null, 7, 1, '/operation/outEmployeeMgt.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510215, 105102, '外包人员导入', 'employeeUploadFile', '外包人员导入', null, 7, 1, '/operation/employeeUploadFile.action', 5, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510216, 105102, '导出', 'exportEmployee', '导出', null, 7, 1, '/operation/outEmployeeMgt_exportEmployee.action', 6, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510217, 105102, '人员分组导入', 'groupImportEmployee', '人员分组导入', null, 7, 1, '/operation/outEmployeeMgt_groupImportEmployee.action', 7, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510218, 105102, '批量修改小组名称', 'batchModifyEmployee', '批量修改小组名称', null, 7, 1, '/operation/outEmployeeMgt_batchModifyEmployee.action', 8, null, '/index.htm');


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105103, 105, '运作班别管理', 'group12', '运作班别管理', null, 4, 1, '/operation/scheduling.action', 3, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510310, 105103, '查询', 'querySchedule', '查询', null, 7, 1, '/operation/schedule_querySchedule.action', 0, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510311, 105103, '新增', 'saveSchedule', '新增', null, 7, 1, '/operation/schedule_saveSchedule.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510312, 105103, '修改', 'updateSchedule', '修改', null, 7, 1, '/operation/schedule_updateSchedule.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510313, 105103, '删除', 'deleteSchedule', '删除', null, 7, 1, '/operation/schedule_deleteSchedule.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510314, 105103, '导出', 'exportSchedule', '导出', null, 7, 1, '/operation/schedule_exportSchedule.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510315, 105103, '导入', 'scheduleUploadFile', '导入', null, 7, 1, '/operation/scheduleUploadFile.action', 5, null, '/index.htm');


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105104, 105, '运作工序管理', 'group122', '运作工序管理', null, 4, 1, '/operation/process.action', 4, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510410, 105104, '查询', 'queryProcess', '查询', null, 7, 1, '/operation/processMgt_queryProcess.action', 0, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510411, 105104, '新增', 'queryProcess', '新增', null, 7, 1, '/operation/processMgt_saveProcess.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510412, 105104, '修改', 'updateProcess', '修改', null, 7, 1, '/operation/processMgt_updateProcess.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510413, 105104, '工序确认', 'confirmProcess', '工序确认', null, 7, 1, '/operation/processMgt_confirmProcess.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510414, 105104, '导入', 'processUploadFile', '导入', null, 7, 1, '/operation/processUploadFile.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510415, 105104, '导出', 'exportProcess', '导出', null, 7, 1, '/operation/processMgt_exportProcess.action', 5, null, '/index.htm');


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105105, 105, '运作排班管理', 'newprocess', '运作排班管理', null, 4, 1, '/operation/schedulingMgt.action', 5, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510510, 105105, '查询', 'search', '查询', null, 7, 1, '/schedulingMgt/search.action', 0, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510511, 105105, '新增', 'add', '新增', null, 7, 1, '/schedulingMgt/add.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510512, 105105, '修改', 'edit', '修改', null, 7, 1, '/schedulingMgt/edit.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510513, 105105, '删除', 'delete', '删除', null, 7, 1, '/schedulingMgt/delete.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510514, 105105, '导入', 'import', '导入', null, 7, 1, '/schedulingMgt/import.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510515, 105105, '导出', 'export', '导出', null, 7, 1, '/schedulingMgt/export.action', 5, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510516, 105105, '提交确认', 'confirm', '提交确认', null, 7, 1, '/schedulingMgt/confirm.action', 6, null, '/index.htm');
insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510517, 105105, '导出已提交确认信息', 'confirmExport', '导出已提交确认信息', null, 7, 1, '/schedulingMgt/confirmExport.action', 7, null, '/index.htm');



insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (105106, 105, '运作工序安排管理', 'newprocess1', '运作工序安排管理', null, 4, 1, '/operation/processMgt.action', 6, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510610, 105106, '查询', 'search', '查询', null, 7, 1, '/processMgt/search.action', 0, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510611, 105106, '新增', 'add', '新增', null, 7, 1, '/processMgt/add.action', 1, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510612, 105106, '修改', 'edit', '修改', null, 7, 1, '/processMgt/edit.action', 2, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510613, 105106, '删除', 'delete', '删除', null, 7, 1, '/processMgt/delete.action', 3, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510614, 105106, '导入', 'import', '导入', null, 7, 1, '/processMgt/import.action', 4, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510615, 105106, '导出', 'export', '导出', null, 7, 1, '/processMgt/export.action', 5, null, '/index.htm');
insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510616, 105106, '提交确认', 'confirm', '提交确认', null, 7, 1, '/processMgt/confirm.action', 6, null, '/index.htm');
insert into ts_module (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (10510617, 105106, '导出已提交确认信息', 'confirmExport', '导出已提交确认信息', null, 7, 1, '/processMgt/confirmExport.action', 7, null, '/index.htm');

commit;
prompt 59 records loaded
prompt Enabling foreign key constraints for TS_MODULE...
alter table TS_MODULE enable constraint FK_TS_MODULE;
prompt Enabling triggers for TS_MODULE...
alter table TS_MODULE enable all triggers;
set feedback on
set define on
prompt Done.
