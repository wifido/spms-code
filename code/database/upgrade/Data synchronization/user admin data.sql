prompt PL/SQL Developer import file
prompt Created on 2014年10月13日 by sfit0505
set feedback off
set define off
prompt Disabling triggers for TM_EMPLOYEE...
alter table TM_EMPLOYEE disable all triggers;
prompt Disabling triggers for TS_USER...
alter table TS_USER disable all triggers;
prompt Deleting TS_USER...
delete from TS_USER;
commit;
prompt Deleting TM_EMPLOYEE...
delete from TM_EMPLOYEE;
commit;
prompt Loading TM_EMPLOYEE...
insert into TM_EMPLOYEE (EMP_ID, EMP_CODE, EMP_DUTY_NAME, EMP_TYPE_NAME, EMP_NAME, EMP_GENDER, EMP_EMAIL, EMP_MOBILE, EMP_OFFICEPHONE, EMP_STUS, REGISTER_DT, LOGOUT_DT, EMP_DESC, VALID_FLG, DEPT_CODE, CHANGE_ZONE_TM, INNER_FLG, CREATED_EMP_CODE, CREATED_TM, MODIFIED_EMP_CODE, MODIFIED_TM)
values (1, 'admin', null, null, 'admin', null, null, null, null, null, null, null, null, 1, '001', null, 1, null, sysdate, null, sysdate);
commit;
prompt 1 records loaded
prompt Loading TS_USER...
insert into TS_USER (USER_ID, USERNAME, PASSWORD, STATUS, USED_TM, UNUSED_TM, DEPT_ID, EMP_ID, TYPE_CODE, DATA_RIGHT_FLG, PWD_MODIFIED_TM, CREATE_EMP, CREATE_TM, MODIFIED_EMP, MODIFIED_TM)
values (1, 'admin', 'QWWIRkMEFP4GEDCCmB73pA==', 'root', sysdate, null, 1, 1, '1', 1, sysdate, 'system', sysdate, 'system', sysdate);
commit;
prompt 1 records loaded
prompt Enabling triggers for TM_EMPLOYEE...
alter table TM_EMPLOYEE enable all triggers;
prompt Enabling triggers for TS_USER...
alter table TS_USER enable all triggers;
set feedback on
set define on
prompt Done.
