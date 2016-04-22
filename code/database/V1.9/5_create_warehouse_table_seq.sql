create table tt_wareh_scheduled_modify_log
(
       id number(10),
       department_id number(19),
       employee_code varchar2(20),
       year_month varchar2(25),
       modify_day_count number(10),
       modify_time date default sysdate,
	   modify_emp_code varchar2(20)
);

COMMENT ON COLUMN tt_wareh_scheduled_modify_log.id IS 'ID';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.department_id IS '网点ID';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.employee_code IS '员工工号';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.year_month IS '年月(YYYYMM)';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.modify_day_count IS '修改数量';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.modify_time IS '修改时间';
COMMENT ON COLUMN tt_wareh_scheduled_modify_log.modify_emp_code IS '修改人';

COMMENT ON TABLE tt_wareh_scheduled_modify_log  IS '仓管修改记录表';

create table tt_wareh_scheduled_agree_rate
(
       id number(10),
	   year_month varchar2(25),
       department_code varchar2(30),
       sched_agree_rate number(10,4),
       sched_agree_count number(10),
	   working_emp_count number(10),
       sched_total number(10),
       create_time date default sysdate
);

COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.id IS 'ID';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.year_month IS '月份(YYYYMM)';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.department_code IS '网点代码';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.sched_agree_rate IS '排班吻合率';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.sched_agree_count IS '排班吻合数';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.working_emp_count IS '在职人数';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.sched_total IS '排班总量';
COMMENT ON COLUMN tt_wareh_scheduled_agree_rate.create_time IS '创建时间';
COMMENT ON TABLE tt_wareh_scheduled_agree_rate  IS '排班吻合表';

create sequence seq_wareh_sched_modify_log
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;