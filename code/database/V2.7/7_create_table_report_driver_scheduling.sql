-- Create sequence 
create sequence SEQ_REPORT_DRIVER_SCHEDULING1
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

-- Create table
create table REPORT_DRIVER_SCHEDULING
(
  ID                  NUMBER(20) not null,
  EMPLOYEE_CODE       VARCHAR2(20),
  EMPLOYEE_NAME       VARCHAR2(100),
  DEPARTMENT_CODE     VARCHAR2(30),
  DEPARTMENT_NAME     VARCHAR2(100),
  AREA_CODE           VARCHAR2(30),
  AREA_NAME           VARCHAR2(100),
  SCHEDULE_TYPE       VARCHAR2(10),
  DAY1                VARCHAR2(20),
  DAY2                VARCHAR2(20),
  DAY3                VARCHAR2(20),
  DAY4                VARCHAR2(20),
  DAY5                VARCHAR2(20),
  DAY6                VARCHAR2(20),
  DAY7                VARCHAR2(20),
  DAY8                VARCHAR2(20),
  DAY9                VARCHAR2(20),
  DAY10               VARCHAR2(20),
  DAY11               VARCHAR2(20),
  DAY12               VARCHAR2(20),
  DAY13               VARCHAR2(20),
  DAY14               VARCHAR2(20),
  DAY15               VARCHAR2(20),
  DAY16               VARCHAR2(20),
  DAY17               VARCHAR2(20),
  DAY18               VARCHAR2(20),
  DAY19               VARCHAR2(20),
  DAY20               VARCHAR2(20),
  DAY21               VARCHAR2(20),
  DAY22               VARCHAR2(20),
  DAY23               VARCHAR2(20),
  DAY24               VARCHAR2(20),
  DAY25               VARCHAR2(20),
  DAY26               VARCHAR2(20),
  DAY27               VARCHAR2(20),
  DAY28               VARCHAR2(20),
  DAY29               VARCHAR2(20),
  DAY30               VARCHAR2(20),
  DAY31               VARCHAR2(20),
  MODIFY_TIME         DATE default SYSDATE,
  TOTAL_REST_COUNT    NUMBER(10),
  DRIVE_TIME_MONTH_S  NUMBER(10,2),
  DRIVE_TIME_MONTH_T  NUMBER(10,2),
  ATTENDANCE_DURATION NUMBER(10,2),
  MATCH_RATE          NUMBER(10,4),
  MONTH               VARCHAR2(10)
);


-- Add comments to the table 
comment on table REPORT_DRIVER_SCHEDULING
  is '排班导出报表';
-- Add comments to the columns 
comment on column REPORT_DRIVER_SCHEDULING.ID
  is '主键';
comment on column REPORT_DRIVER_SCHEDULING.EMPLOYEE_CODE
  is '员工代码';
comment on column REPORT_DRIVER_SCHEDULING.EMPLOYEE_NAME
  is '员工姓名';
comment on column REPORT_DRIVER_SCHEDULING.DEPARTMENT_CODE
  is '网点代码';
comment on column REPORT_DRIVER_SCHEDULING.DEPARTMENT_NAME
  is '网点名称';
comment on column REPORT_DRIVER_SCHEDULING.AREA_CODE
  is '区域代码';
comment on column REPORT_DRIVER_SCHEDULING.AREA_NAME
  is '区域名称';
comment on column REPORT_DRIVER_SCHEDULING.SCHEDULE_TYPE
  is '排班类型''正常''';
comment on column REPORT_DRIVER_SCHEDULING.DAY1
  is '第01天';
comment on column REPORT_DRIVER_SCHEDULING.DAY2
  is '第02天';
comment on column REPORT_DRIVER_SCHEDULING.DAY3
  is '第03天';
comment on column REPORT_DRIVER_SCHEDULING.DAY4
  is '第04天';
comment on column REPORT_DRIVER_SCHEDULING.DAY5
  is '第05天';
comment on column REPORT_DRIVER_SCHEDULING.DAY6
  is '第06天';
comment on column REPORT_DRIVER_SCHEDULING.DAY7
  is '第07天';
comment on column REPORT_DRIVER_SCHEDULING.DAY8
  is '第08天';
comment on column REPORT_DRIVER_SCHEDULING.DAY9
  is '第09天';
comment on column REPORT_DRIVER_SCHEDULING.DAY10
  is '第10天';
comment on column REPORT_DRIVER_SCHEDULING.DAY11
  is '第11天';
comment on column REPORT_DRIVER_SCHEDULING.DAY12
  is '第12天';
comment on column REPORT_DRIVER_SCHEDULING.DAY13
  is '第13天';
comment on column REPORT_DRIVER_SCHEDULING.DAY14
  is '第14天';
comment on column REPORT_DRIVER_SCHEDULING.DAY15
  is '第15天';
comment on column REPORT_DRIVER_SCHEDULING.DAY16
  is '第16天';
comment on column REPORT_DRIVER_SCHEDULING.DAY17
  is '第17天';
comment on column REPORT_DRIVER_SCHEDULING.DAY18
  is '第18天';
comment on column REPORT_DRIVER_SCHEDULING.DAY19
  is '第19天';
comment on column REPORT_DRIVER_SCHEDULING.DAY20
  is '第20天';
comment on column REPORT_DRIVER_SCHEDULING.DAY21
  is '第21天';
comment on column REPORT_DRIVER_SCHEDULING.DAY22
  is '第22天';
comment on column REPORT_DRIVER_SCHEDULING.DAY23
  is '第23天';
comment on column REPORT_DRIVER_SCHEDULING.DAY24
  is '第24天';
comment on column REPORT_DRIVER_SCHEDULING.DAY25
  is '第25天';
comment on column REPORT_DRIVER_SCHEDULING.DAY26
  is '第26天';
comment on column REPORT_DRIVER_SCHEDULING.DAY27
  is '第27天';
comment on column REPORT_DRIVER_SCHEDULING.DAY28
  is '第28天';
comment on column REPORT_DRIVER_SCHEDULING.DAY29
  is '第29天';
comment on column REPORT_DRIVER_SCHEDULING.DAY30
  is '第30天';
comment on column REPORT_DRIVER_SCHEDULING.DAY31
  is '第31天';
comment on column REPORT_DRIVER_SCHEDULING.MODIFY_TIME
  is '创建时间';
comment on column REPORT_DRIVER_SCHEDULING.TOTAL_REST_COUNT
  is '休息总天数';
comment on column REPORT_DRIVER_SCHEDULING.DRIVE_TIME_MONTH_S
  is '月度驾驶时长(排班)';
comment on column REPORT_DRIVER_SCHEDULING.DRIVE_TIME_MONTH_T
  is '月度驾驶时长(实际)';
comment on column REPORT_DRIVER_SCHEDULING.ATTENDANCE_DURATION
  is '月度驾驶出勤时长';
comment on column REPORT_DRIVER_SCHEDULING.MATCH_RATE
  is '排班吻合率';
comment on column REPORT_DRIVER_SCHEDULING.MONTH
  is '排班月份';
-- Create/Recreate primary, unique and foreign key constraints 
alter table REPORT_DRIVER_SCHEDULING
  add constraint REPORT_DRIVER_PK_ID primary key (ID);
  
  -- Create/Recreate indexes 
create index IDX_REPORT_DRIVER_SCH1 on REPORT_DRIVER_SCHEDULING (DEPARTMENT_CODE, MONTH);
