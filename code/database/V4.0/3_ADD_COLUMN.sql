-- Add/modify columns 
alter table OP_ATTENDANCE_COUNT_REPORT add COM_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_NOT_FULL_ATTENDANCE_NUM NUMBER(10);
alter table OP_ATTENDANCE_COUNT_REPORT add COM_OUT_ATTENDANCE_NUM NUMBER(10);
-- Add comments to the columns 
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_FULL_ATTENDANCE_NUM
  is 'ȫ���ƿ���ƥ����';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_NOT_FULL_ATTENDANCE_NUM
  is '��ȫ���ƿ���ƥ����';
comment on column OP_ATTENDANCE_COUNT_REPORT.COM_OUT_ATTENDANCE_NUM
  is '�������ƥ����';

  
-- Add/modify columns 
alter table TI_TCAS_SPMS_SCHEDULE add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '�Ƿ�����ƥ����ڣ�1���㣬0�����㣩';


-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp1 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '�Ƿ�����ƥ����ڣ�1���㣬0�����㣩';

-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp2 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '�Ƿ�����ƥ����ڣ�1���㣬0�����㣩';

-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp3 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '�Ƿ�����ƥ����ڣ�1���㣬0�����㣩';
  
-- Add/modify columns 
alter table TI_TCAS_SPMS_tmp4 add ATTENDANCE_RATE NUMBER(1);
-- Add comments to the columns 
comment on column TI_TCAS_SPMS_SCHEDULE.ATTENDANCE_RATE
  is '�Ƿ�����ƥ����ڣ�1���㣬0�����㣩';