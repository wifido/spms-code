insert into tl_spms_sys_config
  (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values
  (SEQ_BASE.NEXTVAL,
   'MONITOR_EMAIL',
   'sfit0532@sf-express.com,sfit0509@sf-express.com',
   '数据处理监控邮件接收者(研发与测试)',
   sysdate,
   sysdate);
