delete TL_SPMS_SYS_CONFIG t where t.KEY_NAME in ('SEND_DRIVER_MAIL_HOT','DRIVER_MAIL_ROLE_NAME');
commit;