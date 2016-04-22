-- Create/Recreate indexes 
alter table TT_DRIVER_LINE_CONFIGURE
add constraint DEPARTMENT_CODE_MONTH unique (CODE, DEPARTMENT_CODE, MONTH)
using index 
tablespace USERS
pctfree 10
initrans 2
maxtrans 255
storage
(
initial 64K
next 1M
minextents 1
maxextents unlimited
);