-- Create table
create table TT_COMPARED_REPORT
(
  ID              NUMBER(18) not null,
  EMPLOYEE_CODE   VARCHAR2(18),
  EMPLOYEE_NAME   VARCHAR2(50),
  DEPARTMENT_CODE VARCHAR2(20),
  DEPARTMENT_NAME VARCHAR2(50),
  AREA_CODE       VARCHAR2(50),
  AREA_NAME       VARCHAR2(50),
  COMPARE_RESULT  VARCHAR2(50) default 0,
  DAY_MONTH       VARCHAR2(20),
  CREATED_TIME    DATE default SYSDATE,
  MODIFIED_TIME   DATE
);

-- Add comments to the table 
comment on table TT_COMPARED_REPORT
  is '异常对比数据报表';
-- Add comments to the columns 
comment on column TT_COMPARED_REPORT.ID
  is '主键';
comment on column TT_COMPARED_REPORT.EMPLOYEE_CODE
  is '员工代码';
comment on column TT_COMPARED_REPORT.EMPLOYEE_NAME
  is '员工姓名';
comment on column TT_COMPARED_REPORT.DEPARTMENT_CODE
  is '网点代码';
comment on column TT_COMPARED_REPORT.DEPARTMENT_NAME
  is '网点名称';
comment on column TT_COMPARED_REPORT.AREA_CODE
  is '区域代码';
comment on column TT_COMPARED_REPORT.AREA_NAME
  is '区域名称';
comment on column TT_COMPARED_REPORT.COMPARE_RESULT
  is '对比结果0符合/1排班未出勤/2出勤未排班/3出勤线路少于排班线路/4出勤线路超出排班线路/5无排班行车被删除';
comment on column TT_COMPARED_REPORT.DAY_MONTH
  is '排班年月';
comment on column TT_COMPARED_REPORT.CREATED_TIME
  is '创建时间';
comment on column TT_COMPARED_REPORT.MODIFIED_TIME
  is '修改时间';