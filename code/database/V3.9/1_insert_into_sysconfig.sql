insert into tl_spms_sys_config
  (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values
  (SEQ_BASE.NEXTVAL,
   'HONGKONG_DEPTCODE',
   '852W,852WA',
   '香港区网点代码,以英文逗号分隔',
   sysdate,
   null);
commit;