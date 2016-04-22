package com.sf.module.warehouse.dao;

public class WarehouseAttendanceDetailHql {
	public static final String SELECT = "select";
	
	public static final String getQueryTotalSizeSql() {
		return new StringBuilder()
				.append(SELECT)
				.append(" count(*) totalSize")
				.append(getQueryTable())
				.toString();
	}
	
	public static final String getDepartmentSql() {
		return new StringBuilder()
				.append(" SELECT dept_code")
				.append(" FROM TM_DEPARTMENT")
				.append(" WHERE DELETE_FLG = 0")
				.append(" START WITH dept_code in (:departmentCode)")
				.append(" CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE")
				.toString();
	}
	
	public static final String getQuerySql(String condition) {
		return new StringBuilder("SELECT T.*,ROWNUM RN FROM (").append(SELECT)
				.append(getQueryColumn())
				.append(getQueryTable())
				.append(condition)
				.append(WarehouseAttendanceDetailHql.orderBy())
				.append(") t")
				.toString();
	}
	
	public static final String buildQueryTableHead(String sql) {
		return new StringBuilder()
				.append(SELECT)
				.append(" t.WORK_DATE,")
				.append(" t.AREA_CODE,")
				.append(" t.DEPT_CODE,")
				.append(" t.SCHEDULED_DEPT,")
				.append(" t.EMP_CODE,")
				.append(" t.EMP_NAME,")
				.append(" t.PERSON_TYPE,")
				.append(" decode(to_char(t.SCHEDULE_CODE) ,'-' ,'' ,to_char(t.SCHEDULE_CODE)) SCHEDULE_CODE,")
				.append(" t.EMP_DUTY_NAME,")
				.append(" decode(t.ARBST, null, 0, t.ARBST) ARBST,")
				.append(" decode(t.KQ_XSS, null, 0, t.KQ_XSS) KQ_XSS,")
				.append(" decode(t.STDAZ, null, 0, t.STDAZ) STDAZ,")
				.append(" decode(t.WORK_TIME, null, 0, t.WORK_TIME) WORK_TIME")
				.append(" from (" + sql+ ") t")
				.toString();
	}
	
	private static final String buildAppendScheduledCodeTable() {
		return new StringBuilder("select spms.emp_code,")
				.append(" spms.dept_code,")
				.append(" spms.work_date,")
				.append(" wmsys.wm_concat(base.start1_time || '-' || base.end1_time) schedule_code")
				.append(" from (select spms.emp_code,")
				.append(" spms.dept_code,")
				.append(" spms.work_date,")
				.append(" sc.department_code,")
				.append(" sc.scheduling_code,")
				.append(" d.dept_id")
				.append(" from ti_tcas_spms_schedule spms,")
				.append(" tt_schedule_daily sc,")
				.append(" tm_department d")
				.append(" where spms.emp_code = sc.employee_code(+)")
				.append(" and spms.dept_code = d.dept_code")
				.append(" and to_char(spms.work_date, 'yyyymmdd') = sc.day_of_month(+)")
				.append(" and spms.position_type = '2') spms,")
				.append(" tm_pb_schedule_base_info base,")
				.append(" tm_department d")
				.append(" where spms.dept_code = d.dept_code")
				.append(" and spms.scheduling_code = base.schedule_code(+)")
				.append(" and spms.dept_id = base.dept_id(+)")
				.append(" group by spms.emp_code, spms.dept_code, spms.work_date")
				.toString();
	}
	
	private static final String orderBy() {
		return " order by sch.emp_code,sch.work_date,sch.dept_code";
	}
	
	private static final String getQueryColumn() {
		return new StringBuilder()
				.append(" to_char(SPMS.WORK_DATE,'yyyy/mm/dd') WORK_DATE,")
				.append(" DEPT.AREA_CODE,")
				.append(" DEPT.DEPT_CODE ,")
				.append(" SPMS.DEPT_CODE SCHEDULED_DEPT,")
				.append(" SPMS.EMP_CODE,")
				.append(" SCH.EMP_NAME,")
				.append(" SCH.PERSON_TYPE,")
				.append(" SPMS.SCHEDULE_CODE SCHEDULE_CODE,")
				.append(" emp.EMP_DUTY_NAME,")
				.append(" SCH.ARBST,")
				.append(" SCH.KQ_XSS,")
				.append(" SCH.STDAZ,")
				.append(" SCH.WORK_TIME")
				.toString();
	}
	
	private static final String getQueryTable() {
		return new StringBuilder()
				.append(" FROM (" + buildAppendScheduledCodeTable() + ") SPMS,")
				.append(" TI_TCAS_SPMS_SCHEDULE SCH,")
				.append(" TM_DEPARTMENT DEPT,")
				.append(" TM_OSS_EMPLOYEE EMP")
				.append(" WHERE SPMS.EMP_CODE = SCH.EMP_CODE")
				.append(" AND SPMS.DEPT_CODE = SCH.DEPT_CODE")
				.append(" AND SPMS.WORK_DATE = SCH.WORK_DATE")
				.append(" AND DEPT.DEPT_ID = EMP.DEPT_ID")
				.append(" AND SPMS.EMP_CODE = EMP.EMP_CODE")
				.append(" AND SCH.EMP_CODE = EMP.EMP_CODE").toString();
	}
}
