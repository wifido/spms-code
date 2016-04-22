
create table tt_driver_scheduling
(
id number(38,0),
department_code varchar2(30),
employee_code varchar2(20),
configure_code varchar2(20),
day_of_month varchar2(10),
year_month varchar2(10),
created_employee_code VARCHAR2(20),
modified_employee_code VARCHAR2(20),
create_time DATE, 
modified_time DATE,
SCHEDULING_TYPE NUMBER(1),
WORK_TYPE    VARCHAR2(30)
);

COMMENT ON COLUMN tt_driver_scheduling.id IS 'ID';
COMMENT ON COLUMN tt_driver_scheduling.department_code IS '�������';
COMMENT ON COLUMN tt_driver_scheduling.employee_code IS 'Ա������';
COMMENT ON COLUMN tt_driver_scheduling.configure_code IS '������';
COMMENT ON COLUMN tt_driver_scheduling.day_of_month IS '������ ��ʽ��20141010��';
COMMENT ON COLUMN tt_driver_scheduling.year_month IS '���� ��ʽ��2014-10��';
COMMENT ON COLUMN tt_driver_scheduling.created_employee_code IS '������';
COMMENT ON COLUMN tt_driver_scheduling.modified_employee_code IS '�޸���';
COMMENT ON COLUMN tt_driver_scheduling.create_time IS '����ʱ��';
COMMENT ON COLUMN tt_driver_scheduling.modified_time IS '�޸�ʱ��';
COMMENT ON COLUMN tt_driver_scheduling.scheduling_type IS '�Ű����� 1��ʵ���Ű�  0 Ԥ�Ű�';
comment on column TT_DRIVER_SCHEDULING.WORK_TYPE is '���� ������������';