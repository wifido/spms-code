alter table tm_oss_employee add(IS_HAVE_COMMISSION varchar2(1));

comment on column TM_OSS_EMPLOYEE.IS_HAVE_COMMISSION
  is '仓管人员信息 是否参与合算计提 0不参与，1参与';