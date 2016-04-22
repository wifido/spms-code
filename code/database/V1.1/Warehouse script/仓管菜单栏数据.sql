insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (108, 1, '仓管员排班', 'warehouse', '仓管员排班', null, 4, 1, null, 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (108101, 108, '仓管人员管理', 'toWarehousePage', '仓管人员管理', null, 4, 1, '/warehouse/toWarehousePage.action', 1, null, null);

insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (108102, 108, '仓管班别管理', 'toWarehouseClassPage', '仓管班别管理', null, 4, 1, '/warehouse/toWarehouseClassPage.action', 1, null, null);


insert into TS_MODULE (MODULE_ID, PARENT_ID, MODULE_NAME, MODULE_CODE, MODULE_DESC, MODULE_ICON, MODULE_TYPE, APP_TYPE, ACTION_URL, SORT, BUNDLE_ID, HELP_URL)
values (108103, 108, '仓管排班管理', 'toWarehouseSchedulingPage', '仓管排班管理', null, 4, 1, '/warehouse/toWarehouseSchedulingPage.action', 1, null, null);
