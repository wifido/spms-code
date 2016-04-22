/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-20     zhaochangjin       创建
 **********************************************/

package com.sf.module.operation.dao;

import static com.sf.module.common.domain.Constants.KEY_EMP_CODES;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sf.framework.core.exception.BizException;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;
import com.sf.module.dispatch.domain.SchedulingRepository;
import com.sf.module.operation.domain.GroupOutEmployee;
import com.sf.module.operation.domain.OperationEmployeeAttribute;
import com.sf.module.operation.domain.OutEmployee;
import com.sf.module.operation.util.CommonUtil;

/**
 * 外包人员实体的Dao实现类
 * 
 * @author zhaochangjin 2014-06-20
 */
public class OutEmployeeDao extends BaseEntityDao<OutEmployee> implements
		IOutEmployeeDao {
	
	private static final String DEPT_CODE = "deptCode";

	private static final String GROUP_CODE = "groupCode";

	private static final String ENABLE_TIME = "enableTime";

	private static final String EMPLOYEE_CODE = "employeeCode";
	
	private static final String USER_NAME = "userName";

	private static final String ENABLE_STATE = "enableState";

	private static final String SQL_MODIFY_EMPLOYEE_GROUP = "  update tm_oss_employee e \n "
			+"        set e.group_id = \n "
			+"            (select t.group_id \n "
			+"               from  tm_oss_employee e, tm_pb_group_info t \n "
			+"              where e.dept_id = t.dept_id \n "
			+"                and t.group_code = :groupCode \n "
			+"                and e.emp_code = :employeeCode) \n "
			+"      where e.dept_id = (select t.dept_id \n "
			+"                           from tm_department t \n "
			+"                          where t.dept_code = :deptCode) \n "
			+"        and e.emp_post_type = 1 \n "
			+"        and e.emp_code = :employeeCode \n ";

    private static final String SQL_INSERT_RECORD_OF_MODIFY_EMPLOYEE_GROUP = "insert into op_emp_group_modify_record"
            + "(EMP_CODE,GROUP_ID,ENABLE_TM,PREV_GROUP_ID,MODIFY_TM,MODIFY_EMP_CODE,ENABLE_STATE,ID,DEPARTMENT_ID) \n "
            + "values \n "
            + "  (:employeeCode, \n "
            + "   (select t.group_id from tm_pb_group_info t, \n "
            + "   tm_oss_employee e where \n "
            + "   t.dept_id = e.dept_id and t.group_code = :groupCode and \n "
            + "   e.emp_code = :employeeCode), \n "
            + "   :enableTime, \n "
            + "   (select t.group_id from tm_oss_employee t, tm_department d "
            + "	where t.dept_id = d.dept_id "
            + "	and t.emp_code = :employeeCode  "
            + "	and d.dept_code = :deptCode ), \n  "
            + "   sysdate, \n "
            + "   :userName, \n"
            + "   :enableState, \n"
            + " seq_op_emp_group_modify_record.nextval, \n"
            + " (select t.dept_id from tm_department t where t.dept_code = :deptCode)) \n ";

	private static final String SQL_QUERY_RECORD_OF_MODIFY_EMPLOYEE_GROUP = "  " +
			" select g.group_name, \n "
					+"          g.group_code, \n "
					+"          g.Group_Name, \n "
					+"          prevg.Group_Name prev_groupName, \n "
					+"          prevg.group_code prev_groupCode, \n "
					+"          t.* \n "
					+"     from op_emp_group_modify_record t, \n "
					+"          tm_pb_group_info           prevg, \n "
					+"          tm_pb_group_info           g \n "
					+"    where t.group_id = g.group_id(+) \n "
					+"      and t.prev_group_id = prevg.Group_Id(+) \n "
					+"      and t.emp_code = ? \n "
					+"    order by t.modify_tm desc \n ";

    private static final String GROUP_ID2 = "GROUP_ID";

	private static final String EMP_CODE = "EMP_CODE";

	private static final String EMPLOYEES = "employees";

	private static final String SQL_UPDATE_EMPOLYEE_GROUP = " UPDATE TM_OSS_EMPLOYEE E set E.GROUP_ID = ?,E.MODIFIED_TM = SYSDATE WHERE E.EMP_CODE = ?";

	private static final String SQL_ADD_RECORD_OF_MODIFY_EMPLOYEE_GROUP = "insert into op_emp_group_modify_record \n "
																		+ "  (id, \n "
																		+ "   emp_code, \n "
																		+ "   group_id, \n "
																		+ "   enable_tm, \n "
																		+ "   prev_group_id, \n "
																		+ "   modify_tm, \n "
																		+ "   modify_emp_code, \n "
																		+ "   enable_state, \n "
																		+ "   department_id) \n "
																		+ "values \n "
																		+ "  (seq_op_emp_group_modify_record.nextval, ?, ?, ?, ?, Sysdate, ?, ?, ?) \n ";

	private static final String ENABLE_DATE = "enableDate";

	private static final String GROUP_ID = "groupId";

	private static final String EMP_CODES = "empCodes";
	private static final String FIELD_SITE_CODE = "SITE_CODE";
	private static final String FIELD_DEPT_ID = "deptId";
    private static final String FIELD_EMP_CODE = "empcode";
    private static final String FIELD_EMP_NAME = "empname";
    private static final String FIELD_EMP_STATUS = "empStatus";
    private static final String FIELD_USER_ID = "userId";
    private static final String FIELD_FLAG = "flag";
    private static final String FIELD_QUERY_OUT_EMPLOYEE = "queryOutEmployee";
    private static final String FIELD_GROUP_NAME = "groupName";
    
    private static final String DELETE_OPERATION_EMPLOYEE_ATTRIBUTE_SQL = "delete operation_employee_attribute t where t.emp_code in (:empCodes)";
	
	private static final String SEQ_OPERATION_EMPLOYEE_ATTRIBUTE_SQL = "select seq_operation_emp_attribute.nextval id from dual";
	
	private static final String SQL_QUERY_GROUP_INFO_BY_DEPT_ID = "SELECT T.* FROM TM_PB_GROUP_INFO T WHERE T.DEPT_ID = ?";

	private static final String SQL_QUERY_GROUP_INFO_BY_GROUP_CODE = " SELECT T.* FROM TM_PB_GROUP_INFO T WHERE T.GROUP_CODE = ? ";
    
	private static final String KEY_GROUP_ID = "GROUP_ID";

    public Map findEmpForPage(final String empcode, final Long deptId,
			final String empname, final Long groupid, final int pageSize,
			final int pageIndex) {
		String sqlstr = "SELECT E.EMP_ID,D.AREA_CODE,D.DEPT_NAME,E.EMP_CODE,E.EMP_NAME "
				+ " FROM TM_OSS_EMPLOYEE E, TM_PB_GROUP_INFO G,TM_DEPARTMENT D "
				+ " WHERE E.GROUP_ID = G.GROUP_ID "
				+ " AND E.DEPT_ID = G.DEPT_ID   AND E.EMP_POST_TYPE ='1' "
				+ " AND  G.DEPT_ID = D.DEPT_ID " + " AND E.DEPT_ID = :deptid ";
		if (!StringUtils.isEmpty(empcode))
			sqlstr += " AND E.EMP_CODE LIKE :empcode ";
		if (!StringUtils.isEmpty(empname))
			sqlstr += " AND E.EMP_NAME LIKE :empname ";
		if (groupid != null)
			sqlstr += " AND G.GROUP_ID = :groupid ";
		final String sql = sqlstr;
		// 存放参数
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptid", deptId);
		map.put(FIELD_EMP_CODE, "%" + empcode + "%");
		map.put(FIELD_EMP_NAME, "%" + empname + "%");
		map.put("groupid", groupid);
		return (Map) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Map m = new HashMap();
						SQLQuery query = session.createSQLQuery(sql.toString());
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						query.setProperties(map);
						int totalSize = query.list().size();
						m.put(TOTAL_SIZE, totalSize);
						query.setFirstResult(pageIndex);
						query.setMaxResults(pageSize);
						List record = query.list();
						m.put(ROOT, record);
						return m;
					}
				});
	}

	/**
	 * 查询人员信息
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
    @Transactional(readOnly = true)
    public HashMap queryOutEmployee(HashMap paramsMap) {
        HashMap dataMap = new HashMap();
     		// 构造查询SQL并且设置查询参数
        try {
        	List<Object> params = new ArrayList<Object>();
            String queryCountSql = getQueryEmployeeSqlAndSetQueryParameter(paramsMap, params, " SELECT count(*) count");
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
            Query query = session.createSQLQuery(queryCountSql);
			// 设置查询参数
            setQueryParameter(params, query);            
            List count = query.list();
			// 总行数
            int size = Integer.parseInt(count.get(0).toString());
            params = new ArrayList<Object>();
            String queryListSql = getListSQL(paramsMap, params);
            query = session.createSQLQuery(queryListSql)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置分页参数
            setPagingParameter(paramsMap, query);
            setQueryParameter(params, query);
			// 组装数据格式
            dataMap.put(TOTAL_SIZE, size);
            dataMap.put(ROOT, query.list());
        } catch (Exception e) {
			e.printStackTrace();
        }
        return dataMap;
    }

	private String getListSQL(HashMap paramsMap, List<Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT E.*, decode_emp_worktype(work_type) work_Type_Str, D.AREA_CODE ,D.DEPT_NAME ,D.DEPT_CODE, G.GROUP_NAME,G.GROUP_CODE,OPERATION_EMPLOYEE_ATTRIBUTE.SITE_CODE,  ");
        sql.append(" OPERATION_EMPLOYEE_ATTRIBUTE.id OPERATION_EMP_ATTRIBUTE_ID,");
        sql.append(" ts.emp_code tmonthId,");
		// 员工状态字段 离职日期为空则视为有效
        sql.append(" CASE WHEN E.DIMISSION_DT IS NULL THEN 1  ");
		// 员工状态字段 离职日期大于当前日期则为在职
        sql.append(" WHEN E.DIMISSION_DT > SYSDATE THEN 1  ");
		// 员工状态字段 离职日期小于等于当前日期则为离职
        sql.append(" WHEN E.DIMISSION_DT <= SYSDATE THEN 0 END EMP_STATUS,");
        sql.append(" decode(t.enable_tm,null,null,to_char(t.enable_tm,'yyyy-mm-dd')) ENABLE_TM,");
        sql.append(" decode(reg.group_code,null,null,reg.group_code) CHANGE_GROUP_CODE ");
		String listSql = getQueryEmployeeSqlAndSetQueryParameter(paramsMap, params,sql.toString());
		return listSql;
	}

    private String getQueryEmployeeSqlAndSetQueryParameter(HashMap paramsMap, List<Object> params, String sqlStart) {
        StringBuilder sql = new StringBuilder();
        Long userId = Long.parseLong(paramsMap.get(FIELD_USER_ID).toString());
        String flag = paramsMap.get(FIELD_FLAG).toString();
        sql.append(sqlStart);
		// 查询语句主体
        sql.append(" FROM  TM_OSS_EMPLOYEE  E,TM_DEPARTMENT D,TM_PB_GROUP_INFO G,OPERATION_EMPLOYEE_ATTRIBUTE OPERATION_EMPLOYEE_ATTRIBUTE,");
        sql.append(" op_emp_group_modify_record t, tm_pb_group_info  reg, ");
        sql.append("  (select e.emp_code ");
        sql.append("           from TM_OSS_EMPLOYEE e ");
        sql.append("          where e.emp_code in ");
        sql.append("                (select emp_code from tt_pb_shedule_by_month tm where dept_id = e.dept_id ");
        sql.append("                and tm.ym = to_char(sysdate,'yyyy-mm')) ");
        sql.append("            and dept_id in  (SELECT DEPT_ID ");
        sql.append("           FROM TM_DEPARTMENT ");
        sql.append("          WHERE DELETE_FLG = 0 ");
        sql.append("          START WITH DEPT_ID = ? ");
        sql.append("         CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE)) ts ");
        params.add(paramsMap.get(FIELD_DEPT_ID));
        sql.append(" WHERE E.DEPT_ID = D.DEPT_ID  ");
        sql.append(" AND ts.emp_code(+) = e.emp_code ");
        sql.append(" AND E.GROUP_ID = G.GROUP_ID(+)  and  G.DEPT_ID(+) = E.DEPT_ID  ");
        sql.append(" AND e.emp_code = OPERATION_EMPLOYEE_ATTRIBUTE.emp_code(+) ");
        sql.append(" AND E.EMP_POST_TYPE = '1' ");
        sql.append(" and t.group_id = reg.group_id(+) ");
        sql.append(" and (t.id =  (select max(id)  from op_emp_group_modify_record t  where t.emp_code(+) =e.emp_code");
        sql.append(" group by t.emp_code) or t.id is null)");
        sql.append(" and t.emp_code(+) = e.emp_code ");
		// 网点ID
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_DEPT_ID))) {
			// 查询有权限的子网点
            addSqlByDeptId(paramsMap, sql, userId, flag, params);
        }
		// 工号
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_EMP_CODE))) {
            sql.append("AND  E.EMP_CODE  LIKE ? ");
            params.add("%" + paramsMap.get(FIELD_EMP_CODE).toString() + "%");
        }
		// 姓名
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_EMP_NAME))) {
            sql.append("AND  E.EMP_NAME  LIKE ? ");
            params.add("%" + paramsMap.get(FIELD_EMP_NAME).toString() + "%");
        }
        
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_GROUP_NAME))) {
			if ("未分组".equals(paramsMap.get(FIELD_GROUP_NAME).toString())) {
        		sql.append(" AND  E.GROUP_ID is null");
        	} else {
        		sql.append("AND  E.GROUP_ID  IN ( ");
        		sql.append(" select t_group.group_id from tm_pb_group_info t_group where t_group.group_name like '%" + paramsMap.get(FIELD_GROUP_NAME).toString() + "%' )" );
        	}
        }
		// 在职状态
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_EMP_STATUS))) {
            if (paramsMap.get(FIELD_EMP_STATUS).equals("1")) {
                sql.append(" AND  (E.DIMISSION_DT  IS NULL OR  E.DIMISSION_DT>SYSDATE)  ");
            } else if (paramsMap.get(FIELD_EMP_STATUS).equals("0")) {
                sql.append(" AND  E.DIMISSION_DT<=SYSDATE  ");
            }
        }
        String siteCodeValue = (String) paramsMap.get(FIELD_SITE_CODE);
        if (StringUtil.isNotBlank(siteCodeValue)) {
            sql.append(" and operation_employee_attribute.site_code = ?");
            params.add(siteCodeValue);
        }
		// 按照主键排序
        sql.append(" ORDER  BY  E.EMP_ID  DESC  ");
        return sql.toString();
    }
    
    private void setQueryParameter(List<Object> params, Query query) {
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
    }

    private void addSqlByDeptId(HashMap paramsMap, StringBuilder sql,
                                Long userId, String flag, List<Object> params) {
		if (CommonUtil.isAdmin(userId)) {
			sql.append(" AND   E.DEPT_ID  IN ( SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0  START WITH DEPT_ID=?  CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) ");
			params.add(paramsMap.get(FIELD_DEPT_ID));
		} else {
			sql.append(" AND   E.DEPT_ID  IN ( SELECT A.DEPT_ID FROM  TS_USER_DEPT A,(SELECT  T.DEPT_ID  FROM TM_DEPARTMENT  T  WHERE DELETE_FLG=0  START WITH DEPT_ID=? CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) B ");
			sql.append("  WHERE A.USER_ID = ?  AND B.DEPT_ID = A.DEPT_ID)  ");
			params.add(paramsMap.get(FIELD_DEPT_ID));
			params.add(userId);
		}
    }

	/**
	 * 获取外包人员自动生成的工号
	 * 
	 * @return dataMap
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
    @Transactional(readOnly = true)
	public HashMap getInsertEmpCode() {
		HashMap dataMap = new HashMap();
		// 查询语句主体
		String sql = "SELECT SEQ_OUT_ENPLOYEE_BASE.NEXTVAL FROM DUAL  ";
		try {
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql.toString())
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 获取数据
			List list = query.list();
			dataMap.put(ROOT, list);
		} catch (Exception e) {
			throw new BizException("获取外包人员工号失败！");
		}
		return dataMap;
	}

	/**
	 * 获取小组信息
	 * 
	 * @return dataList
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 */
    @Transactional(readOnly = true)
	public List getGroupByDeptId(HashMap paramsMap) {
		try {
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(SQL_QUERY_GROUP_INFO_BY_DEPT_ID)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
			query.setParameter(0, paramsMap.get(FIELD_DEPT_ID));
			// 获取数据
			return query.list();
		} catch (Exception e) {
			throw new BizException("获取小组信息失败！");
		}
	}

    /**
	 * 查询需要同步的数据
	 * 
	 * @return
	 * @author 632898 李鹏
	 * @date 2014-07-10
	 */
    @Transactional(readOnly = true)
	public HashMap queryHrEmp(HashMap paramsMap) {
		StringBuilder sql = new StringBuilder();
		List params = new ArrayList();
		HashMap dataMap = new HashMap();
		// 查询语句主体
		sql.append("  SELECT E.*, D.AREA_CODE ,D.DEPT_NAME    FROM  TI_OSS_HR_EMP_NEW_CHANGEDEPT  E,TM_DEPARTMENT D  WHERE  E.DEPT_CODE = D.DEPT_CODE   ");

		// 查询未处理的数据
		sql.append(" AND  E.DEAL_FLAG = 0  ");

		// 查询到转网生效时间或新增的数据
		sql.append("  AND (E.EMP_STUS='1'  OR  (E.EMP_STUS='2' AND E.CHANGE_ZONE_TM<=SYSDATE))   ");

		// 网点代码
		if (!StringUtils.isEmpty(paramsMap.get(DEPT_CODE))) {
			sql.append("AND  E.DEPT_CODE  =  ? ");
			params.add(paramsMap.get(DEPT_CODE));
		}
		// 姓名
		if (!StringUtils.isEmpty(paramsMap.get("empName"))) {
			sql.append("AND  E.EMP_NAME  =  ? ");
			params.add(paramsMap.get("empName"));
		}
		// 工号
		if (!StringUtils.isEmpty(paramsMap.get("empCode"))) {
			sql.append("AND  E.EMP_CODE  =  ? ");
			params.add(paramsMap.get("empCode"));
		}
		try {
			// 创建session
            Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql.toString())
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
            setQueryParameter(params, query);
			// 总行数
			int size = query.list().size();
			// 设置分页查询参数
            setPagingParameter(paramsMap, query);
			// 取查询结果
			List list = query.list();
			// 组装数据格式
			dataMap.put(TOTAL_SIZE, size);
			dataMap.put(ROOT, list);
		} catch (Exception e) {
			throw new BizException("查询需要同步的数据失败！");
		}
		return dataMap;
	}

    private void setPagingParameter(HashMap paramsMap, Query query) {
        if (paramsMap.get(START) != null)
            query.setFirstResult(Integer.parseInt(paramsMap.get(START)
                    .toString()));
        if (paramsMap.get(LIMIT) != null)
            query.setMaxResults(Integer.parseInt(paramsMap.get(LIMIT)
                    .toString()));
    }

    /**
	 * 更新同步人员状态
	 * 
	 * @author 632898 李鹏
	 * @date 2014-07-14
	 */
	@Transactional
	public boolean updateSynHrEmp(HashMap paramsMap) {
		StringBuilder sql = new StringBuilder();
		// 更新语句主体
		sql.append("  UPDATE TI_OSS_HR_EMP_NEW_CHANGEDEPT E set E.DEAL_FLAG = 1 WHERE E.EMP_CODE = ?   ");

		// 创建session
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();

		Query query = session.createSQLQuery(sql.toString());

		if (StringUtils.isEmpty(paramsMap.get("empCode")))
			return false;
		query.setParameter(0, paramsMap.get("empCode").toString());

		query.executeUpdate();

		return true;
	}

	/**
	 * 查询需要同步的人员信息
	 * 
	 * @return List
	 * @author 632898 李鹏
	 * @date 2014-07-15
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List queryEmployeeToEmail() {
		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					final String namedSql = "synEmployeeEmail.sql";

					public Object doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						Query query = arg0.getNamedQuery(namedSql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 查询需要同步的数据
	 * 
	 * @return List
	 * @author 632898 李鹏
	 * @date 2014-07-15
	 */
	public List querySynEmployee() {
		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					final String namedSql = "synEmployee.sql";

					public Object doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						Query query = arg0.getNamedQuery(namedSql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	@Transactional
	public void batchUpdateEmployeeGroup(String username, Date enableDate, 
			Map<String, Object>[] employees, String groupName, String departmentId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query;

		for (Map<String, Object> employee : employees) {
			String employeeCode = String.valueOf(employee.get(EMP_CODE));

            deleteNotTakeEffect(employeeCode, enableDate);
            int enableState = 0;
            if (enableDate.before(new Date())) {
                enableState = 1;
                query = session.createSQLQuery(SQL_UPDATE_EMPOLYEE_GROUP);
                query.setParameter(0, getGroupName(groupName));
                query.setParameter(1, employeeCode);
                query.executeUpdate();
            }
            query = session.createSQLQuery(SQL_ADD_RECORD_OF_MODIFY_EMPLOYEE_GROUP);
            setEmployeeGroupInformationToQuery(username, enableDate, groupName, query, employee, employeeCode, enableState, departmentId);
			query.executeUpdate();
		}

	}

    private void setEmployeeGroupInformationToQuery(String username, Date enableDate, String groupName,
    		Query query, Map<String, Object> employee, String employeeCode, int enableState, String departmentId) {
        query.setParameter(0, employeeCode);
        query.setParameter(1, getGroupName(groupName));
        query.setParameter(2, enableDate);
        query.setParameter(3, employee.get(KEY_GROUP_ID));
        query.setParameter(4, username);
        query.setParameter(5, enableState);
        query.setParameter(6, departmentId);
    }

    public void deleteNotTakeEffect(String employeeCode, Date enableDate) {
        String sql = "delete op_emp_group_modify_record record \n" +
                    " where record.emp_code = ? \n" +
                    "   and (record.enable_tm >= to_date(?, 'yyyy/MM/dd HH24:mi:ss') \n" +
                    "    or record.enable_state = 0)";

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(sql);
        query.setParameter(0, employeeCode);
        query.setParameter(1, DateUtil.formatDate(enableDate));

        query.executeUpdate();
    }

    private String getGroupName(String groupName) {
		return groupName.equals("删除小组") ? "" : groupName;
    }

    @Transactional(readOnly = true)
	public boolean queryEmployeeValidity(String empCode, String deptCode) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(getQueryEmployeeValiditySql())
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, deptCode);
		query.setParameter(1, empCode);

		List<Map<String, Object>> searchSchedulingCountPage = query.list();
        return((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue() > 0;
    }

    private String getQueryEmployeeValiditySql() {
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT  COUNT(*)  TOTALSIZE  FROM  TM_OSS_EMPLOYEE  e  WHERE  ");
        sql.append(" E.DEPT_ID =(select t.dept_id from tm_department t where t.dept_code = ? ) ");
        sql.append(" AND  E.EMP_CODE = ?  ");
        return sql.toString();
    }

    @Transactional
    public boolean queryGroupValidity(String groupCode,String departmentCode) {
        Session session = getHibernateTemplate().getSessionFactory()
                .getCurrentSession();
        Query query = session.createSQLQuery("SELECT  COUNT(*) TOTALSIZE FROM  TM_PB_GROUP_INFO  t ," +
        		" tm_department d where t.dept_id=d.dept_id and t.group_code = ? and d.dept_code = ?  ")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter(0, groupCode);
        query.setParameter(1, departmentCode);
        List<Map<String, Object>> searchSchedulingCountPage = query.list();
        return ((BigDecimal) searchSchedulingCountPage.get(0).get(SchedulingRepository.COL_TOTAL_SIZE)).intValue() > 0;
    }

	@Transactional
	public void updateEmployeeGroup(String groupCode, String empCode,
			String deptCode, Date enableTime, String userName) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		int enableState = 0;
		deleteNotTakeEffect(empCode, enableTime);
		
		if(enableTime.before(new Date())){
			enableState = 1;
		}
		Query query = session
				.createSQLQuery(SQL_INSERT_RECORD_OF_MODIFY_EMPLOYEE_GROUP);
		query.setParameter(EMPLOYEE_CODE, empCode);
		query.setParameter(ENABLE_TIME, enableTime);
		query.setParameter(GROUP_CODE, groupCode);
		query.setParameter(DEPT_CODE, deptCode);
		query.setParameter(USER_NAME, userName);
		query.setParameter(ENABLE_STATE, enableState);
		query.executeUpdate();
		
		if(enableTime.before(new Date())){
			query = session.createSQLQuery(SQL_MODIFY_EMPLOYEE_GROUP);
			query.setParameter(EMPLOYEE_CODE, empCode);
			query.setParameter(DEPT_CODE, deptCode);
			query.setParameter(GROUP_CODE, groupCode);
			query.executeUpdate();
		}
	}

    @Transactional()
    public boolean updateEmployeeSiteCode(OperationEmployeeAttribute operationEmployeeAttribute) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("update operation_employee_attribute t");
        stringBuilder.append(" set t.Site_Code = ?");
        stringBuilder.append(" ,t.modified_tm = sysdate");
        stringBuilder.append(" where t.id = ?");
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(stringBuilder.toString());
        query.setParameter(0, operationEmployeeAttribute.getSiteCode());
        query.setParameter(1, operationEmployeeAttribute.getId());

        return query.executeUpdate() > 0;
    }

    @Transactional(readOnly = true)
    public  Long buildOperationEmployeeAttributeId() {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(SEQ_OPERATION_EMPLOYEE_ATTRIBUTE_SQL);
        List list = query.list();
        return ((BigDecimal)list.get(0)).longValue();
    }

    @Transactional
    public boolean insertOperationEmployeeAttribute(OperationEmployeeAttribute operationEmployeeAttribute) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(getInsertOperationEmployeeAttributeSql());
        query.setParameter(0, operationEmployeeAttribute.getId());
        query.setParameter(1, operationEmployeeAttribute.getEmpCode());
        query.setParameter(2, operationEmployeeAttribute.getSiteCode());

        return query.executeUpdate() > 0;
    }

    private String getInsertOperationEmployeeAttributeSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into operation_employee_attribute ");
        stringBuilder.append("(id, emp_code, site_code, modified_tm) ");
        stringBuilder.append("values(?, ?, ?, sysdate)");
        return stringBuilder.toString();
    }
    
    @Transactional
    public boolean deleteOperationEmployeeAttributeById(String[] employeeCodes) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(DELETE_OPERATION_EMPLOYEE_ATTRIBUTE_SQL);
        query.setParameterList(KEY_EMP_CODES, employeeCodes);
        return query.executeUpdate() > 0;
    }
    
    @Transactional
	public Map<String, Object> queryRecordOfModifyEmployeeGroup(
			String employeeCode) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = session.createSQLQuery(
				SQL_QUERY_RECORD_OF_MODIFY_EMPLOYEE_GROUP)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter(0, employeeCode);
		List<Map<String, Object>> list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
    
    @Transactional(readOnly = true)
  	public List getGroupByGroupCode(String groupCode) {
  		try {
              Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
  			Query query = session.createSQLQuery(SQL_QUERY_GROUP_INFO_BY_GROUP_CODE)
  					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
  			query.setParameter(0, groupCode);
			// 获取数据
  			return query.list();
  		} catch (Exception e) {
			throw new BizException("获取小组信息失败！");
  		}
  	}

    @Transactional(readOnly = true)
    public boolean employeeAttributeExistByParameter(GroupOutEmployee groupOutEmployee) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(queryEmployeeAttributeSql());

        query.setParameter(0, groupOutEmployee.getEmpCode());
        query.setParameter(1, groupOutEmployee.getDeptCode());
        query.setParameter(2, groupOutEmployee.getGroupCode());
        return Integer.valueOf(query.list().get(0).toString()) > 0;
    }

    private String queryEmployeeAttributeSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select count(*) total");
        stringBuilder.append(" from tm_oss_employee emp, tm_department dept, tm_pb_group_info groupInfo");
        stringBuilder.append(" where emp.dept_id = dept.dept_id");
        stringBuilder.append(" and emp.group_id = groupInfo.Group_Id");
        stringBuilder.append(" and emp.emp_code = ?");
        stringBuilder.append(" and dept.dept_code = ?");
        stringBuilder.append(" and groupInfo.Group_Code = ?");
        return stringBuilder.toString();
    }

    @Transactional
    public void updateEmployeeAttributeSiteCodeByParameter(GroupOutEmployee groupOutEmployee) {
        String sql = "update operation_employee_attribute attr set attr.site_code = ? where attr.emp_code = ?";
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(sql);
        query.setParameter(0, groupOutEmployee.getSiteCode());
        query.setParameter(1, groupOutEmployee.getEmpCode());
        query.executeUpdate();
    }

    public OutEmployee queryEmployee(String empCode, Long deptId) {
		DetachedCriteria dc = DetachedCriteria.forClass(OutEmployee.class);
		dc.add(Restrictions.eq("empCode", empCode));
		dc.add(Restrictions.eq("deptId", deptId));
		List<OutEmployee> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public HashMap exportOutEmployee(HashMap paramsMap) {
		HashMap dataMap = new HashMap();
		// 构造查询SQL并且设置查询参数
		try {
			List<Object> params = new ArrayList<Object>();
			String queryListSql = getListSQL(paramsMap, params);
			Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(queryListSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			// 设置查询参数
			setQueryParameter(params, query);
			dataMap.put(ROOT, query.list());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}
}