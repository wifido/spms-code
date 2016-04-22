-- Create table
create table TT_DISPATCH_MAINTENANCE_MAIL
(
  ID              NUMBER(38) not null,
  EMAIL_ACCOUNT   VARCHAR2(200),
  DEPARTMENT_CODE VARCHAR2(30),
  DIVISION_CODE   VARCHAR2(30),
  AREA_CODE       VARCHAR2(30)
);
-- Add comments to the columns 
comment on column TT_DISPATCH_MAINTENANCE_MAIL.ID
  is '主键ID';
comment on column TT_DISPATCH_MAINTENANCE_MAIL.EMAIL_ACCOUNT
  is '邮箱帐号';
comment on column TT_DISPATCH_MAINTENANCE_MAIL.DEPARTMENT_CODE
  is '网点代码';
comment on column TT_DISPATCH_MAINTENANCE_MAIL.DIVISION_CODE
  is '分点部代码';
comment on column TT_DISPATCH_MAINTENANCE_MAIL.AREA_CODE
  is '地区代码';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TT_DISPATCH_MAINTENANCE_MAIL
  add constraint PK_MAINTENANCE_MAIL_ID primary key (ID);
-- Create/Recreate indexes 
create index IDX_MAINTENANCE_MAIL1 on TT_DISPATCH_MAINTENANCE_MAIL (DEPARTMENT_CODE, EMAIL_ACCOUNT);