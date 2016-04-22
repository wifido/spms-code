-- Create table
create table TI_DRIVER_SCHEDULING_TMP
(
  ID              NUMBER(19) not null,
  DRIVE_MEMBER    VARCHAR2(25),
  DAY_OF_MONTH    VARCHAR2(50),
  DEPT_CODE       VARCHAR2(2000),
  SCHEDULING_TIME NUMBER(10,2)
);

-- Add comments to the table 
comment on table TI_DRIVER_SCHEDULING_TMP
  is '�Ű����ݶԱȱ�';
-- Add comments to the columns 
comment on column TI_DRIVER_SCHEDULING_TMP.ID
  is '����';
comment on column TI_DRIVER_SCHEDULING_TMP.DRIVE_MEMBER
  is 'Ա������';
comment on column TI_DRIVER_SCHEDULING_TMP.DAY_OF_MONTH
  is '�Ű�������';
comment on column TI_DRIVER_SCHEDULING_TMP.DEPT_CODE
  is 'ƴ������';
comment on column TI_DRIVER_SCHEDULING_TMP.SCHEDULING_TIME
  is '�Ű�ʱ��(ʵ��)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TI_DRIVER_SCHEDULING_TMP
  add constraint PK_ID_PRIMARY primary key (ID);
  
-- Create/Recreate indexes 
create index IDX_DRIVER_SCH1 on TI_DRIVER_SCHEDULING_TMP (DRIVE_MEMBER, DAY_OF_MONTH);
