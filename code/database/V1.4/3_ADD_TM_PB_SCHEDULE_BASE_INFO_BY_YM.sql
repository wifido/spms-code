alter table TM_PB_SCHEDULE_BASE_INFO add YM VARCHAR2(25);
-- Add comments to the columns 
comment on column TM_PB_SCHEDULE_BASE_INFO.YM
  is '年月';



alter table TM_PB_SCHEDULE_BASE_INFO add COPY_TO_NEXT_MONTH NUMBER(1);
-- Add comments to the columns 
comment on column TM_PB_SCHEDULE_BASE_INFO.COPY_TO_NEXT_MONTH
  is '是否复制到下个月0或空为否 1表示是';




DROP INDEX UK1_TM_PB_SCHEDULE_BASE_INFO;