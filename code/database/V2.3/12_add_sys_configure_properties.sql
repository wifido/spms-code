insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_HOST_IP', '10.0.44.171', 'ESB�ļ����ͷ�������ip', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_MESSAGE', 'reqSftpServerInfo', 'ESB ������Ϣ', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_NOTIFY_PORT', '10002', 'ESB sftp  ���Ѷ˿�', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_REQ_SFTPINFO_PORT', '10002', 'ESB sftp  ����˿�', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_WS_SAP_DELIVERY_SERVICE_URL', 'http://10.0.44.171:12021/esb/ws/SAPFileDelivery', 'ESB�ļ����ͷ���ӿڵ�ַ', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'PN_SERVER_HOST_ADDRESS', 'http://10.202.146.23:1080', 'PNServer �����ַ����Ϣ���ͽӿڵ�ַ��', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'SWITCH_PUSH_MESSAGE_TO_PNSERVER', '0', '������Ϣ��PNServer���� 0:������ 1������', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'SWITCH_PUSH_DRIVER_LOG_TO_SAP', '0', '�����г���־��SAP���� 0:������ 1������', sysdate, null);

commit;
