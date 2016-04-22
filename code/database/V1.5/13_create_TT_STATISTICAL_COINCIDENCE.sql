-- Create table
create table TT_STATISTICAL_COINCIDENCE
(
  DEPT_ID               VARCHAR2(18),
  YEAR_MONTH            VARCHAR2(10),
  COINCIDENCE_RATE      VARCHAR2(10),
  COINCIDENCERATE_COUNT NUMBER(10),
  DEPT_COUNT            NUMBER(10),
  POSITION_TYPE         NUMBER(1),
  DEPT_CODE             VARCHAR2(30)
);
-- Add comments to the table 
comment on table TT_STATISTICAL_COINCIDENCE
  is '吻合率统计表';
-- Add comments to the columns 
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_ID
  is '网点ID';
comment on column TT_STATISTICAL_COINCIDENCE.YEAR_MONTH
  is '年月';
comment on column TT_STATISTICAL_COINCIDENCE.COINCIDENCE_RATE
  is '吻合率';
comment on column TT_STATISTICAL_COINCIDENCE.COINCIDENCERATE_COUNT
  is '吻合数';
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_COUNT
  is '网点总量';
comment on column TT_STATISTICAL_COINCIDENCE.POSITION_TYPE
  is '1标识为排班，2标识为工序';
comment on column TT_STATISTICAL_COINCIDENCE.DEPT_CODE
  is '网点代码';
