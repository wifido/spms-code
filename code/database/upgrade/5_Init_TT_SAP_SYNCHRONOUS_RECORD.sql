prompt PL/SQL Developer import file
prompt Created on 2014年9月19日 by sfit0509
set feedback off
set define off
prompt Disabling triggers for TT_SAP_SYNCHRONOUS_RECORD...
alter table TT_SAP_SYNCHRONOUS_RECORD disable all triggers;
prompt Deleting TT_SAP_SYNCHRONOUS_RECORD...
delete from TT_SAP_SYNCHRONOUS_RECORD;
commit;
prompt Loading TT_SAP_SYNCHRONOUS_RECORD...
insert into TT_SAP_SYNCHRONOUS_RECORD (ID, SYNCHRONIZATION_TIME, PROCEDURES_NAME)
values (1, to_date('16-09-2014 16:27:05', 'dd-mm-yyyy hh24:mi:ss'), 'SCHEDULING_SAP_SYNCHRONIZATION');
commit;
prompt 2 records loaded
prompt Enabling triggers for TT_SAP_SYNCHRONOUS_RECORD...
alter table TT_SAP_SYNCHRONOUS_RECORD enable all triggers;
set feedback on
set define on
prompt Done.
