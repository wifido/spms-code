insert into tl_spms_sys_config
  (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values
  (SEQ_BASE.NEXTVAL,
   'HONGKONG_DEPTCODE',
   '852W,852WA',
   '������������,��Ӣ�Ķ��ŷָ�',
   sysdate,
   null);
commit;