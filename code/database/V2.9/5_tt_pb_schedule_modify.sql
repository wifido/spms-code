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
  is '主键ID';
comment on column tt_pb_schedule_modify.EMP_CODE
  is '被排班人工号';
comment on column tt_pb_schedule_modify.SCHEDULE_DT
  is '排班日期';
comment on column tt_pb_schedule_modify.SCHEDULE_CODE
  is '班别代码';
comment on column tt_pb_schedule_modify.MODIFIED_TM
  is '修改日期';
comment on column tt_pb_schedule_modify.MODIFIED_EMP_CODE
  is '修改人工号';
comment on column tt_pb_schedule_modify.MODIFY_TYPE
  is '标识（1表示3天前修改，0表示3天内修改）';
comment on column tt_pb_schedule_modify.DEPT_ID
  is '网点ID';
comment on column tt_pb_schedule_modify.DEPT_CODE
  is '网点代码';
comment on column tt_pb_schedule_modify.AREA_CODE
  is '地区代码';
comment on column tt_pb_schedule_modify.YEAR_MONTH
  is '年月';
-- Create/Recreate indexes 
create index INDEX_CONDITION on tt_pb_schedule_modify (AREA_CODE,YEAR_MONTH,EMP_CODE);


