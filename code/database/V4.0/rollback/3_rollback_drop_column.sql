alter table OP_ATTENDANCE_COUNT_REPORT drop column COM_FULL_ATTENDANCE_NUM ;

alter table OP_ATTENDANCE_COUNT_REPORT drop column COM_NOT_FULL_ATTENDANCE_NUM ;

alter table OP_ATTENDANCE_COUNT_REPORT drop column COM_OUT_ATTENDANCE_NUM;


alter table TI_TCAS_SPMS_SCHEDULE drop column ATTENDANCE_RATE;

alter table TI_TCAS_SPMS_tmp1 drop column ATTENDANCE_RATE;

alter table TI_TCAS_SPMS_tmp2 drop column ATTENDANCE_RATE;

alter table TI_TCAS_SPMS_tmp3 drop column ATTENDANCE_RATE;

alter table TI_TCAS_SPMS_tmp4 drop column ATTENDANCE_RATE;
