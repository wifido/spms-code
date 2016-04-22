create table tt_pb_schedule_modify (
            ID NUMBER(38) not null,
            EMP_CODE VARCHAR2(20),
            SCHEDULE_DT DATE,
            SCHEDULE_CODE VARCHAR2(20),
            MODIFIED_TM DATE,
            MODIFIED_EMP_CODE VARCHAR2(20),
            MODIFY_TYPE NUMBER(1),
            DEPT_ID NUMBER(19),
			DEPT_CODE  VARCHAR2(30),
			AREA_CODE VARCHAR2(10),
            YEAR_MONTH VARCHAR2(10));
			
-- Add comments to the columns 
comment on column tt_pb_schedule_modify.ID
  is '����ID';
comment on column tt_pb_schedule_modify.EMP_CODE
  is '���Ű��˹���';
comment on column tt_pb_schedule_modify.SCHEDULE_DT
  is '�Ű�����';
comment on column tt_pb_schedule_modify.SCHEDULE_CODE
  is '������';
comment on column tt_pb_schedule_modify.MODIFIED_TM
  is '�޸�����';
comment on column tt_pb_schedule_modify.MODIFIED_EMP_CODE
  is '�޸��˹���';
comment on column tt_pb_schedule_modify.MODIFY_TYPE
  is '��ʶ��1��ʾ3��ǰ�޸ģ�0��ʾ3�����޸ģ�';
comment on column tt_pb_schedule_modify.DEPT_ID
  is '����ID';
comment on column tt_pb_schedule_modify.DEPT_CODE
  is '�������';
comment on column tt_pb_schedule_modify.AREA_CODE
  is '��������';
comment on column tt_pb_schedule_modify.YEAR_MONTH
  is '����';
-- Create/Recreate indexes 
create index INDEX_CONDITION on tt_pb_schedule_modify (AREA_CODE,YEAR_MONTH,EMP_CODE);


