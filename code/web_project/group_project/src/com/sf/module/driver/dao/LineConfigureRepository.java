package com.sf.module.driver.dao;

import static com.sf.module.common.domain.sql.QueryStatement.*;
import static com.sf.module.common.domain.sql.SqlColumn.*;
import static com.sf.module.common.domain.sql.SqlExpression.*;
import static com.sf.module.common.domain.sql.SqlHelper.toSqlParam;
import static com.sf.module.common.util.StringUtil.isNotBlank;
import java.util.Map;
import com.sf.module.common.domain.sql.QueryStatement;
import com.sf.module.common.domain.sql.QueryStatement.TableAlias;
import com.sf.module.frameworkimpl.biz.DepartmentCacheBiz;

public class LineConfigureRepository {
	private static final String COL_DEPARTMENT_CODE = "DEPARTMENT_CODE";
	private static final String COL_DEPT_ID = "DEPT_ID";
	private static final String COL_ID = "ID";
	private static final String COL_CODE = "CODE";
	private static final String COL_MONTH = "MONTH";
	private static final String COL_LINE_ID = "LINE_ID";
	private static final String COL_CONFIGURE_ID = "CONFIGURE_ID";
	private static final String COL_CONFIGURE_CODE = "CONFIGURE_CODE";
	private static final String COL_VALID_STATUS = "VALID_STATUS";
	private static final String COL_START_TIME = "START_TIME";
	private static final String COL_END_TIME = "END_TIME";
	private static final String COL_SOURCE_CODE = "SOURCE_CODE";
	private static final String COL_SOURCE_NAME = "SOURCE_NAME";
	private static final String COL_DESTINATION_CODE = "DESTINATION_CODE";
	private static final String COL_DESTINATION_NAME = "DESTINATION_NAME";
	private static final String COL_CREATE_TM = "CREATE_TM";
	private static final String COL_MODIFIED_TM = "MODIFIED_TM";
	private static final String COL_CREATE_EMP_CODE = "CREATE_EMP_CODE";
	private static final String COL_MODIFIED_EMP_CODE = "MODIFIED_EMP_CODE";
	private static final String COL_DEPT_CODE = "DEPT_CODE";
	private static final String COL_AREA_CODE = "AREA_CODE";
	private static final String COL_TYPE = "TYPE";
	private static final String COL_PARENT_DEPT_CODE = "PARENT_DEPT_CODE";
	private static final String COL_HQ_CODE = "HQ_CODE";
	private static final String COL_DELETE_FLG = "DELETE_FLG";
	private static final String COL_ROW_NUM = "ROWNUM";
	private static final String COL_YEAR_MONTH = "YEAR_MONTH";

	public static final String TABLE_TT_DRIVER_LINE_CONFIGURE = "TT_DRIVER_LINE_CONFIGURE";
	public static final String TABLE_TT_DRIVER_LINE_CONFIGURE_R = "TT_DRIVER_LINE_CONFIGURE_R";
	public static final String TABLE_TM_DEPARTMENT = "TM_DEPARTMENT";
	public static final String TABLE_LINE_CONFIGURE = "V_DIRVER_LINE_COFNIGURE";
	public static final String TABLE_EXPORT_LINE_CONFIGURE = "V_DRIVER_EXPORT_LINE_CONFIGURE";
	public static final String TABLE_TT_DRIVER_SCHEDULING = "TT_DRIVER_SCHEDULING";

	private static final TableAlias TABLE_ALIAS_DEPARTMENT = new TableAlias(TABLE_TM_DEPARTMENT, TABLE_TM_DEPARTMENT);
	private static final TableAlias TABLE_ALIAS_LINE_CONFIGURE = new TableAlias(TABLE_LINE_CONFIGURE, TABLE_LINE_CONFIGURE);
	private static final TableAlias TABLE_ALIAS_EXPORT_LINE_CONFIGURE = new TableAlias(TABLE_EXPORT_LINE_CONFIGURE, TABLE_EXPORT_LINE_CONFIGURE);
	private static final TableAlias TABLE_ALIAS_TT_DRIVER_LINE_CONFIGURE = new TableAlias(TABLE_TT_DRIVER_LINE_CONFIGURE, TABLE_TT_DRIVER_LINE_CONFIGURE);
	private static final TableAlias TABLE_ALIAS_TT_DRIVER_LINE_CONFIGURE_R = new TableAlias(TABLE_TT_DRIVER_LINE_CONFIGURE_R, TABLE_TT_DRIVER_LINE_CONFIGURE_R);
	private static final String NVL_MAX_CONFIG_CODE_DEPARTMENT_CODE = " max(to_number(nvl(code, 0))) DEPARTMENT_CODE";
	private static final TableAlias TABLE_ALIAS_TT_DRIVER_SCHEDULING = new TableAlias(TABLE_TT_DRIVER_SCHEDULING, TABLE_TT_DRIVER_SCHEDULING);

	private static final String SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID = select(COL_DEPT_CODE)
	        .from(TABLE_TM_DEPARTMENT)
	        .where(equal(COL_DELETE_FLG, 0))
	        .startWith(equal(COL_DEPT_ID, toSqlParam(COL_DEPT_ID)))
	        .connectBy(equal(COL_DEPT_CODE, COL_PARENT_DEPT_CODE))
	        .toQuery()
	        .toString();

	public static QueryStatement buildOriginalStatementForAllLineConfigures() {
		return select(
		        distinctColumn(TABLE_TT_DRIVER_SCHEDULING, COL_CONFIGURE_CODE),
		        column(TABLE_LINE_CONFIGURE, COL_ID),
		        column(TABLE_LINE_CONFIGURE, COL_CODE),
		        column(TABLE_LINE_CONFIGURE, COL_MONTH),
		        column(TABLE_LINE_CONFIGURE, COL_VALID_STATUS),
		        column(TABLE_LINE_CONFIGURE, COL_START_TIME),
		        column(TABLE_LINE_CONFIGURE, COL_END_TIME),
		        column(TABLE_LINE_CONFIGURE, COL_SOURCE_CODE),
		        column(TABLE_LINE_CONFIGURE, COL_SOURCE_NAME),
		        column(TABLE_LINE_CONFIGURE, COL_DESTINATION_NAME),
		        column(TABLE_LINE_CONFIGURE, COL_DESTINATION_CODE),
		        to_char_column(TABLE_LINE_CONFIGURE, COL_CREATE_TM, COL_CREATE_TM),
		        to_char_column(TABLE_LINE_CONFIGURE, COL_MODIFIED_TM, COL_MODIFIED_TM),
		        column(TABLE_LINE_CONFIGURE, COL_CREATE_EMP_CODE),
		        column(TABLE_LINE_CONFIGURE, COL_MODIFIED_EMP_CODE),
		        column(TABLE_LINE_CONFIGURE, COL_TYPE),
		        column(TABLE_TM_DEPARTMENT, COL_DEPT_CODE),
		        column(TABLE_TM_DEPARTMENT, COL_DEPT_ID),
		        column(TABLE_TM_DEPARTMENT, COL_AREA_CODE))
		        .from(TABLE_ALIAS_LINE_CONFIGURE, TABLE_ALIAS_DEPARTMENT, TABLE_ALIAS_TT_DRIVER_SCHEDULING)
		        .where(outjoin(column(TABLE_LINE_CONFIGURE, COL_CONFIGURE_CODE), column(TABLE_TT_DRIVER_SCHEDULING, COL_CONFIGURE_CODE)))
		        .and(outjoin(column(TABLE_LINE_CONFIGURE, COL_MONTH), column(TABLE_TT_DRIVER_SCHEDULING, COL_YEAR_MONTH)))
		        .and(equal(column(TABLE_LINE_CONFIGURE, COL_DEPARTMENT_CODE), column(TABLE_TM_DEPARTMENT, COL_DEPT_CODE)));
	}

	public static void main(String[] args) {
		System.out.println(buildOriginalStatementForAllLineConfigures());
	}

	public static QueryStatement buildOriginalStatementForCountAllLineConfigures() {
		return select(COL_ID).from(TABLE_ALIAS_LINE_CONFIGURE, TABLE_ALIAS_DEPARTMENT).where(
		        equal(column(TABLE_LINE_CONFIGURE, COL_DEPARTMENT_CODE), column(TABLE_TM_DEPARTMENT, COL_DEPT_CODE)));
	}

	public static void buildOptionalParameters(Map<String, String> params, QueryStatement queryStatement) {
		if (isNotBlank(params.get(COL_DEPT_CODE))) {
			params.put(COL_DEPT_ID, DepartmentCacheBiz.getDepartmentByCode(params.get(COL_DEPT_CODE)).getId().toString());
		}
		if (isNotBlank(params.get(COL_DEPT_ID))) {
			queryStatement.and(withinChild(column(TABLE_TM_DEPARTMENT, COL_DEPT_CODE), SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID));
		}
		if (isNotBlank(params.get(COL_VALID_STATUS))) {
			queryStatement.and(equal(column(TABLE_LINE_CONFIGURE, COL_VALID_STATUS), toSqlParam(COL_VALID_STATUS)));
		}
		if (isNotBlank(params.get(COL_MONTH))) {
			queryStatement.and(equal(column(TABLE_LINE_CONFIGURE, COL_MONTH), toSqlParam(COL_MONTH)));
		}
		if (isNotBlank(params.get(COL_CODE))) {
			queryStatement.and(String.format(" %s or %s or %s", like(column(TABLE_LINE_CONFIGURE, COL_CONFIGURE_CODE), toSqlParam(COL_CODE)),
					like(column(TABLE_LINE_CONFIGURE, COL_START_TIME), toSqlParam(COL_CODE)),
					like(column(TABLE_LINE_CONFIGURE, COL_SOURCE_CODE), toSqlParam(COL_CODE))));
			params.put(COL_CODE, "%" + params.get(COL_CODE) + "%");
		}
		if (isNotBlank(params.get("currentCode"))) {
			queryStatement.and(notEqual(column(TABLE_LINE_CONFIGURE, COL_CONFIGURE_CODE), toSqlParam("currentCode")));
		}
	}

	public static void buildOptionalParametersForExportAllLineConfigures(Map<String, String> params, QueryStatement queryStatement) {
		if (isNotBlank(params.get(COL_DEPT_CODE))) {
			params.put(COL_DEPT_ID, DepartmentCacheBiz.getDepartmentByCode(params.get(COL_DEPT_CODE)).getId().toString());
		}
		if (isNotBlank(params.get(COL_DEPT_ID))) {
			queryStatement.and(withinChild(column(TABLE_EXPORT_LINE_CONFIGURE, COL_DEPARTMENT_CODE), SQL_SEARCH_ALL_CHILDREN_ID_WITH_PARENT_ID));
		}
		if (isNotBlank(params.get(COL_VALID_STATUS))) {
			queryStatement.and(equal(column(TABLE_EXPORT_LINE_CONFIGURE, COL_VALID_STATUS), toSqlParam(COL_VALID_STATUS)));
		}
		if (isNotBlank(params.get(COL_MONTH))) {
			queryStatement.and(equal(column(TABLE_EXPORT_LINE_CONFIGURE, COL_MONTH), toSqlParam(COL_MONTH)));
		}
		if (isNotBlank(params.get(COL_CONFIGURE_CODE))) {
			queryStatement.and(equal(column(TABLE_EXPORT_LINE_CONFIGURE, COL_CONFIGURE_CODE), toSqlParam(COL_CONFIGURE_CODE)));
		}
		if (isNotBlank(params.get(COL_CODE))) {
			queryStatement.and(String.format(" %s or %s or %s", like(column(TABLE_EXPORT_LINE_CONFIGURE, COL_CONFIGURE_CODE), toSqlParam(COL_CODE)),
					like(column(TABLE_EXPORT_LINE_CONFIGURE, COL_START_TIME), toSqlParam(COL_CODE)),
					like(column(TABLE_EXPORT_LINE_CONFIGURE, COL_SOURCE_CODE), toSqlParam(COL_CODE))));
			params.put(COL_CODE, "%" + params.get(COL_CODE) + "%");
		}
	}

	public static QueryStatement buildOriginalStatementForExportAllLineConfigures() {
		return select(column(TABLE_EXPORT_LINE_CONFIGURE, ALL_COLUMNS), column(COL_ROW_NUM)).from(TABLE_ALIAS_EXPORT_LINE_CONFIGURE).where(equal("1", "1"));

	}

	public static QueryStatement buildOriginalStatementForLineConfigureCode() {
		return select(NVL_MAX_CONFIG_CODE_DEPARTMENT_CODE)
		        .from(TABLE_ALIAS_TT_DRIVER_LINE_CONFIGURE)
		        .where(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_DEPARTMENT_CODE), QUERY_ARG))
		        .and(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_MONTH), QUERY_ARG))
		        .orderBy(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_CODE), QueryStatement.DESCENDING);
	}

	public static QueryStatement buildOriginalStatementForLineIsConfigured() {
		return select("TT_DRIVER_LINE_CONFIGURE.DEPARTMENT_CODE||'-'||TT_DRIVER_LINE_CONFIGURE.CODE CONFIGURE_CODE")
		        .from(TABLE_ALIAS_TT_DRIVER_LINE_CONFIGURE, TABLE_ALIAS_TT_DRIVER_LINE_CONFIGURE_R)
		        .where(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_ID), column(TABLE_TT_DRIVER_LINE_CONFIGURE_R, COL_CONFIGURE_ID)))
		        .and(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_VALID_STATUS), QUERY_ARG))
		        .and(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE, COL_MONTH), QUERY_ARG))
		        .and(equal(column(TABLE_TT_DRIVER_LINE_CONFIGURE_R, COL_LINE_ID), QUERY_ARG));
	}

	public static final String SQL_DELETE_LINE_CONFIGURE_RELATION = " DELETE FROM TT_DRIVER_LINE_CONFIGURE_R T WHERE T.CONFIGURE_ID = ? ";
	
	
	public static final String SQL_QUERY_LINE_CONFIGURE_BY_CONFIGUE_CODE =""
						+ "   select  l.start_time , l.end_time , l.source_code , l.destination_code  \n "
						+ "       from tt_driver_line_configure   t, \n "
						+ "            tt_driver_line_configure_r r, \n "
						+ "            tm_driver_line             l \n "
						+ "      where r.configure_id = t.id \n "
						+ "        and r.line_id = l.id \n "
						+ "        and t.department_code = ? \n "
						+ "        and t.code = ? \n "
						+ "        and t.month = ? \n "
						+ "        and t.valid_status = '1' \n "
						+ "      order by r.sort \n ";
	
	public static final String SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE = ""
						+ "     SELECT APP.APPLY_ID APPLY_ID, \n " 
						+ "			   DECODE(APP.APPLY_EMPLOYEE_CODE, NULL, ' ', APP.APPLY_EMPLOYEE_CODE) APPLY_EMPLOYEE_CODE, \n "
						+ "            DECODE(APP.DAY_OF_MONTH, NULL, ' ', APP.DAY_OF_MONTH) DAY_OF_MONTH, \n "
						+ "            DECODE(APP.OLD_CONFIG_CODE, NULL, ' ', APP.OLD_CONFIG_CODE) OLD_CONFIG_CODE, \n "
						+ "            DECODE(APP.APPLY_INFO, NULL, ' ', APP.APPLY_INFO)  APPLY_INFO,\n "
						+ "            DECODE(APP.Apply_Type, NULL, ' ', APP.Apply_Type)  Apply_Type, \n "
						+ "            DECODE(APP.NEW_CONFIG_CODE, NULL, ' ', APP.NEW_CONFIG_CODE)  NEW_CONFIG_CODE \n "
						+ "       FROM TT_DRIVER_APPLY APP \n "
						+ "      WHERE APP.STATUS = 1  \n "
						+ "            AND APP.approver = :employeeCode \n "
						+ "            AND APP.STATUS = 1 \n "
						+ "            AND APP.APPLY_TYPE = :applyType \n "
						+ "			   AND (1=1) ";
				
	public static final String SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE_REPLACE = " APP.NEW_CONFIG_CODE LIKE :searchString OR APP.OLD_CONFIG_CODE LIKE :searchString OR  APP.APPLY_EMPLOYEE_CODE LIKE :searchString OR APP.DAY_OF_MONTH LIKE :searchString";

	public static final String SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE_ORDER_BY =  " ORDER BY APP.Apply_Id desc \n";
	
	public static final String SQL_QUERY_APPROVAL_COUNT_BY_EMP_CODE =  " select count(*) TOTALSIZE from (" +
			SQL_QUERY_APPROVAL_LIST_BY_EMP_CODE
			+ ")";
	
	public static final String SQL_QUERY_APPROVER_COUNT = "select emp.emp_code,emp.emp_name, emp.emp_duty_name,dept.dept_code,dept.dept_id,rownum rn  \n"
														+ "     from tm_oss_employee emp,  \n"
														+ "          (SELECT dept_id, dept_code  \n"
														+ "             FROM TM_DEPARTMENT  \n"
														+ "            WHERE (DELETE_FLG = 0)  \n"
														+ "            START WITH DEPT_ID in  \n"
														+ "                       (select d.dept_id  \n"
														+ "                          from TM_DEPARTMENT d  \n"
														+ "                         where d.dept_code =  \n"
														+ "                               (select dept.area_code  \n"
														+ "                                  from tm_department dept  \n"
														+ "                                 where dept.dept_code =  \n"
														+ "                                       (select dept.dept_code  \n"
														+ "                                          from tm_department dept  \n"
														+ "                                         where dept.dept_code = :departmentCode)))  \n"
														+ "           CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE) dept  \n"
														+ "    where emp.dept_id = dept.dept_id  \n"
														+ "      and emp.emp_post_type = 5  \n"
														+ "      and emp.emp_duty_name like '%司机组长%'  \n";
	public static final String SQL_QUERY_CONFIGURE_BY_CONFIGUE_CODE =""
			+        "SELECT COUNT(*)" 
			+        "  FROM TT_DRIVER_LINE_CONFIGURE TT_DRIVER_LINE_CONFIGURE" 
			+        " WHERE TT_DRIVER_LINE_CONFIGURE.DEPARTMENT_CODE = ?" 
			+        "   AND TT_DRIVER_LINE_CONFIGURE.MONTH = ?" 
			+        "   AND TT_DRIVER_LINE_CONFIGURE.CODE = ?" ;
}
