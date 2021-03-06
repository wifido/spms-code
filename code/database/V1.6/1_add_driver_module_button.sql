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
values
  (10810601,
   108106,
   '删除',
   'delete',
   '删除',
   '',
   7,
   1,
   '/driver/delete_lineConfigure.action',
   4,
   null,
   '/index.htm');

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
values
  (10810602,
   108106,
   '新增机动配班',
   'addMobileNetwork',
   '新增机动配班',
   '',
   7,
   1,
   '/driver/addMobileNetwork.action',
   9,
   null,
   '/index.htm');

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
values
  (10810603,
   108106,
   '修改机动配班',
   'updateMobileNetwork',
   '修改机动配班',
   '',
   7,
   1,
   '/driver/updateMobileNetwork.action',
   10,
   null,
   '/index.htm');


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
values
  (107105,
   107,
   '排班表',
   'schedulingTable',
   '排班表',
   '',
   4,
   1,
   '/report/schedulingTable.action',
   5,
   null,
   '');
commit;