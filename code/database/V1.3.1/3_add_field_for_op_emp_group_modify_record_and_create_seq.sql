alter table op_emp_group_modify_record add (ID number(10));

alter table op_emp_group_modify_record add (department_id number(20) not null);

alter table op_emp_group_modify_record add constraint pk_id primary key(id);

ALTER TABLE op_emp_group_modify_record RENAME COLUMN enable_status to enable_state;


CREATE SEQUENCE  seq_op_emp_group_modify_record  
MINVALUE 1 
MAXVALUE 999999999999999999999999999 
INCREMENT BY 1 
START WITH 1 
CACHE 20 
NOORDER  NOCYCLE;