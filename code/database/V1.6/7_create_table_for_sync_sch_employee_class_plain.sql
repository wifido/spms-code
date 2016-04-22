-- Create table
create table TI_SCH_EMPLOYEECLASS_PLAIN
(
  ID         NUMBER(20) not null,
  EMPLOYEEID VARCHAR2(20),
  DUTYDATE   VARCHAR2(20),
  CREATETIME DATE,
  SYNC_TM    DATE default SYSDATE
);
-- Add comments to the table 
comment on table TI_SCH_EMPLOYEECLASS_PLAIN
  is 'SCH_CDH收派员值班信息接口表';
-- Add comments to the columns 
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.ID
  is '逻辑主键';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.EMPLOYEEID
  is '收派员工号';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.DUTYDATE
  is '值班日期';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.CREATETIME
  is 'SCH_CDH创建时间';
comment on column TI_SCH_EMPLOYEECLASS_PLAIN.SYNC_TM
  is '同步时间';
-- Grant/Revoke object privileges 
grant all on TI_SCH_EMPLOYEECLASS_PLAIN to SPMSETL;

