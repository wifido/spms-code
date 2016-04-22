-- Create table
create table TT_DISPATCH_MONITOR_REPORT
(
  DEPT_CODE                   VARCHAR2(30),
  DAY_OF_MONTH                DATE,
  HQ_CODE                     VARCHAR2(30),
  AREA_CODE                   VARCHAR2(30),
  FULLTIME_EMP_NUM            NUMBER(10),
  NOT_FULLTIME_EMP_NUM        NUMBER(10),
  FULLTIME_SCHEDULING_NUM     NUMBER(10),
  FULLTIME_REST_NUM           NUMBER(10),
  NOT_FULLTIME_SCHEDULING_NUM NUMBER(10),
  NOT_FULLTIME_REST_NUM       NUMBER(10),
  FULLTIME_NOT_SCHEDULING     NUMBER(10),
  NOT_FULLTIME_NOT_SCHEDULING NUMBER(10),
  DIVISION_CODE               VARCHAR2(30),
  CREATE_DATE                 DATE,
  TYPE_LEVEL                  NUMBER(10)
);
-- Add comments to the table 
comment on table TT_DISPATCH_MONITOR_REPORT
  is '一线监控报表';
-- Add comments to the columns 
comment on column TT_DISPATCH_MONITOR_REPORT.DEPT_CODE
  is '网点代码';
comment on column TT_DISPATCH_MONITOR_REPORT.DAY_OF_MONTH
  is '日期';
comment on column TT_DISPATCH_MONITOR_REPORT.HQ_CODE
  is '经营本部代码';
comment on column TT_DISPATCH_MONITOR_REPORT.AREA_CODE
  is '地区代码';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_EMP_NUM
  is '全日制人数';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_EMP_NUM
  is '非全日制人数';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_SCHEDULING_NUM
  is '全日制排班人数';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_REST_NUM
  is '全日制排休人数';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_SCHEDULING_NUM
  is '非全日制排班人数';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_REST_NUM
  is '非全日制排休人数';
comment on column TT_DISPATCH_MONITOR_REPORT.FULLTIME_NOT_SCHEDULING
  is '全日制未排班人数';
comment on column TT_DISPATCH_MONITOR_REPORT.NOT_FULLTIME_NOT_SCHEDULING
  is '非全日制未排班人数';
comment on column TT_DISPATCH_MONITOR_REPORT.DIVISION_CODE
  is '分点部代码';
comment on column TT_DISPATCH_MONITOR_REPORT.CREATE_DATE
  is '创建时间';
comment on column TT_DISPATCH_MONITOR_REPORT.TYPE_LEVEL
  is '网点级别';
