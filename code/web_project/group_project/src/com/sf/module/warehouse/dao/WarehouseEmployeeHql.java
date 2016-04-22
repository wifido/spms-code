package com.sf.module.warehouse.dao;

import com.sf.module.common.util.QueryHelper;

public class WarehouseEmployeeHql extends QueryHelper {

	public static final String QUERY_STAFF_INFORMATION_COUNT = "select count(*) totalSize \n"
			+ "from tm_oss_employee emp,tm_department dept \n"
			+ " where emp.dept_id = dept.dept_id  " +
			"		and  (emp.dimission_dt is null or emp.dimission_dt + 60 > sysdate) " +
			"		and emp.emp_post_type = '3' ";

	public static final String QUERY_STAFF_INFORMATION = "select rownum rn, emp.emp_id, \n"
			+ " emp.emp_code, \n"
			+ " emp.emp_name, \n"
			+ " emp.emp_duty_name emp_duty_name, \n"
			+ " emp.dept_id, \n"
			+ " to_char(emp.create_tm,'yyyy-mm-dd') create_tm, \n"
			+ " to_char(emp.modified_tm,'yyyy-mm-dd') modified_tm, \n"
			+ " emp.create_emp_code, \n"
			+ " emp.modified_emp_code, \n"
			+ " decode_emp_worktype(emp.work_type) work_type_name, \n"
			+ " emp.work_type, \n"
			+ " emp.email, \n"
			+ " emp.dimission_dt, \n"
			+ " decode(emp.dimission_dt, null,'在职', decode(sign(emp.dimission_dt - sysdate), 1, '在职', '离职')) working_type, \n"
			+ " to_char(emp.sf_date,'yyyy-mm-dd') sf_date, \n"
			+ " decode(emp.is_have_commission,'0','否','1','是') is_have_commission, \n"
			+ " dept.area_code, \n"
			+ " dept.dept_code, \n"
			+ " dept.dept_name, \n"
			+ " Mobile_network \n"
			+ " from tm_oss_employee emp,tm_department dept, \n"
			+ " (select s2.emp_code, \n"
			+ " listagg(depts.dept_code, ',') within group(order by null) Mobile_network \n"
			+ " from tt_warehouse_emp_dept_r s2, \n"
			+ " tm_oss_employee     emp, \n"
			+ " tm_department         depts \n"
			+ " where s2.emp_code = emp.emp_code \n"
			+ " and s2.dept_id = depts.dept_id \n"
			+ " group by s2.emp_code) lef \n"
			+ " where emp.dept_id = dept.dept_id \n"
			+ " and  (emp.dimission_dt is null or emp.dimission_dt + 60 > sysdate)  \n"
			+ " and emp.emp_post_type = '3' \n"
			+ " and emp.emp_code = lef.emp_code(+) \n";

	public static final String UPDATE_EMPLOYEE_SQL = " update tm_oss_employee t \n"
			+ "     set "
			// " t.emp_duty_name     = :emp_duty_name, \n"
			// + "         t.emp_name          = :emp_name, \n"
			+ "         t.MODIFIED_TM       = sysdate, \n"
			+ "         t.IS_HAVE_COMMISSION = :IS_HAVE_COMMISSION, \n"
			+ "         t.MODIFIED_EMP_CODE = :MODIFIED_EMP_CODE \n"
			+ "   where t.EMP_ID = :EMP_ID";

	public static final String ADD_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL = "insert into tt_warehouse_emp_dept_r  values(:EMP_CODE,:DEPT_ID)";

	public static final String DELETE_EMPLOYEE_DYNAMIC_DEPARTMENT_SQL = "delete from tt_warehouse_emp_dept_r t where t.emp_code=:EMP_CODE";

	public final static String QUERY_EMPLOYEE_WORK_TYPE_BY_POST_TYPE = "select distinct tm_oss_employee.work_type work_type_id,"
			+ "      decode_emp_worktype(tm_oss_employee.work_type) work_type_name"
			+ "   from tm_oss_employee tm_oss_employee"
			+ "  where tm_oss_employee.emp_post_type = ?"
			+ "  order by tm_oss_employee.work_type";

    public final static String validDynamicDepartmentCodeSql = "select dept.dept_code  from tm_department dept where  dept.dept_code = ?";
}
