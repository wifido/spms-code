-- Add/modify columns 
alter table TI_OSS_HR_EMP_INFO_ALTER add POSITION_ATTR VARCHAR2(20);
-- Add comments to the columns 
comment on column TI_OSS_HR_EMP_INFO_ALTER.POSITION_ATTR
  is '�������ͣ�һ�ߡ����ߣ�';


-- Add/modify columns 
alter table TI_OSS_HR_EMP_INFO add POSITION_ATTR VARCHAR2(20);
-- Add comments to the columns 
comment on column TI_OSS_HR_EMP_INFO.POSITION_ATTR
  is '�������ͣ�һ�ߡ����ߣ�';


-- Add/modify columns 
alter table TM_OSS_EMPLOYEE add POSITION_ATTR VARCHAR2(20);
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE.POSITION_ATTR
  is '�������ͣ�һ�ߡ����ߣ�';

-- Add/modify columns 
alter table TM_OSS_EMPLOYEE add DUTY_SERIAL VARCHAR2(20);
-- Add comments to the columns 
comment on column TM_OSS_EMPLOYEE.DUTY_SERIAL
  is '��λ����';