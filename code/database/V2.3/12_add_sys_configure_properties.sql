insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_HOST_IP', '10.0.44.171', 'ESB文件推送服务主机ip', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_MESSAGE', 'reqSftpServerInfo', 'ESB 服务信息', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_NOTIFY_PORT', '10002', 'ESB sftp  提醒端口', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_REQ_SFTPINFO_PORT', '10002', 'ESB sftp  请求端口', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'ESB_WS_SAP_DELIVERY_SERVICE_URL', 'http://10.0.44.171:12021/esb/ws/SAPFileDelivery', 'ESB文件推送服务接口地址', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'PN_SERVER_HOST_ADDRESS', 'http://10.202.146.23:1080', 'PNServer 服务地址（消息推送接口地址）', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'SWITCH_PUSH_MESSAGE_TO_PNSERVER', '0', '推送消息到PNServer开关 0:不推送 1：推送', sysdate, null);

insert into tl_spms_sys_config (ID, KEY_NAME, KEY_VALUE, KEY_DESC, CREATED_TM, MODIFIED_TM)
values (SEQ_BASE.Nextval, 'SWITCH_PUSH_DRIVER_LOG_TO_SAP', '0', '推送行车日志到SAP开关 0:不推送 1：推送', sysdate, null);

commit;
