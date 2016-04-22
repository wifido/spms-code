-- Create table
create table TI_SPMS2CDP_OPERATION_REPORT
(
  SCHEDULE_DT         DATE,
  AREA_CODE           VARCHAR2(50),
  DEPT_CODE           VARCHAR2(50),
  SCHEDULE_CODE       VARCHAR2(50),
  BEGIN_TM            DATE,
  END_TM              DATE,
  SHUTTLE_LENGTH      NUMBER,
  FTE_NUM             NUMBER,
  FTE_SCHEDULE_NUM    NUMBER,
  FTE_ATTENDANCE_NUM  NUMBER(8,2),
  OUT_EMP_NUM         NUMBER,
  OUT_SCHEDULE_NUM    NUMBER,
  OUT_ATTENDANCE_NUM  NUMBER(8,2),
  NFTE_NUM            NUMBER,
  NFTE_SCHEDULE_NUM   NUMBER,
  NFTE_ATTENDANCE_NUM NUMBER(8,2),
  TOTAL_OF_ATTENDANCE NUMBER(8,2),
  SYNC_DATE           DATE
);
-- Add comments to the table 
comment on table TI_SPMS2CDP_OPERATION_REPORT
  is 'SPMS排班系统-CDP接口表';
-- Add comments to the columns 
comment on column TI_SPMS2CDP_OPERATION_REPORT.SCHEDULE_DT
  is '日期
';
comment on column TI_SPMS2CDP_OPERATION_REPORT.AREA_CODE
  is '地区';
comment on column TI_SPMS2CDP_OPERATION_REPORT.DEPT_CODE
  is '中转场';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SCHEDULE_CODE
  is '班次编码';
comment on column TI_SPMS2CDP_OPERATION_REPORT.BEGIN_TM
  is '计划开始时间';
comment on column TI_SPMS2CDP_OPERATION_REPORT.END_TM
  is '计划结束时间';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SHUTTLE_LENGTH
  is '班次时长';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_NUM
  is '全日制员工在职人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_SCHEDULE_NUM
  is '全日制员工排班人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.FTE_ATTENDANCE_NUM
  is '全日制员工计划出勤时长';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_EMP_NUM
  is '外包在职人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_SCHEDULE_NUM
  is '外包排班人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.OUT_ATTENDANCE_NUM
  is '外包员工计划出勤时长';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_NUM
  is '非全日制员工在职人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_SCHEDULE_NUM
  is '非全日制员工排班人数';
comment on column TI_SPMS2CDP_OPERATION_REPORT.NFTE_ATTENDANCE_NUM
  is '非全日制员工计划出勤时长';
comment on column TI_SPMS2CDP_OPERATION_REPORT.TOTAL_OF_ATTENDANCE
  is '总计划出勤时长';
comment on column TI_SPMS2CDP_OPERATION_REPORT.SYNC_DATE
  is '传输时间';
