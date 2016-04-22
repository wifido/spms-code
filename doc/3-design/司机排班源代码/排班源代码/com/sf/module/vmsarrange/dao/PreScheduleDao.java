/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-5-29     600675        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.vmsarrange.domain.PreSchedule;
import com.sf.module.vmsarrange.domain.PreScheduleDraft;
import com.sf.module.vmsarrange.util.ArrFileUtil;
import com.sf.module.vmsarrange.util.DeptTreeDaoSql;

/**
 *
 * PreScheduleDao处理类
 *
 */
public class PreScheduleDao extends ArrBaseEntityDao<PreSchedule> implements IPreScheduleDao {
	//查询分页数据
	@SuppressWarnings("rawtypes")
	public IPage findByPage(String deptCode, String empCode, Integer classType,
			String yearMonth, int pageSize, int pageIndex,Long userId) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreSchedule.class);
		//添加权限控制
		ArrDeptTreeDaoSql.addDeptIdCriteriaByDeptCode(dc, "{alias}.DEPT_ID",deptCode, userId);
		DetachedCriteria rdc = dc.createCriteria("driver");
		if(!ArrFileUtil.isEmpty(empCode)){
			rdc.add(Restrictions.like("empCode", empCode,MatchMode.ANYWHERE));
		}
		//班次类型
		if(null != classType){
			dc.add(Restrictions.eq("classType", classType));
		}
		//年月
		if(!ArrFileUtil.isEmpty(yearMonth)){
			dc.add(Restrictions.eq("yearMonth", yearMonth));
		}
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}
	/**
	 * 获取当月最大天数
	 * @param year
	 * @param month
	 * @return
	 */
	private int maxDate(int year,int month){
		Calendar a= Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month-1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	private static void addToMap(String test, Map map) {
		map.put("value", test);
		
	}
	/**
	 * 根据网点ID，配班班次代码，查找出该用户权限网点下的所有班次，填充到下拉框中
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findArrangeNos(Long deptId,String arrangeNo,
			Long userId) {
		final String temp_arrangeNo = arrangeNo;
		final Long temp_userId = userId;
		final Long temp_deptId = deptId;
		return (Map) this.getHibernateTemplate().execute(new HibernateCallback(){
			private String sql = "SELECT SA.ARRANGE_ID,SA.ARRANGE_NO FROM TM_ARR_SCHEDULE_ARRANGE SA, TM_DEPARTMENT D WHERE SA.DEPT_ID = D.DEPT_ID";
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Map map = new java.util.HashMap();
				StringBuilder sb = new StringBuilder(sql);
				List<Object> params = new java.util.ArrayList<Object>();
				DeptTreeDaoSql.addDeptIdSql(" AND SA.DEPT_ID", temp_deptId, temp_userId, sb,params);
				if( StringUtils.isNotEmpty(temp_arrangeNo)){
					sb.append(" AND SA.ARRANGE_NO LIKE ?");
					params.add("%" + temp_arrangeNo + "%");
				}
				sb.append(" ORDER BY SA.CREATED_TM DESC");
				Query query = session.createSQLQuery(sb.toString());
				
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				int size = query.list().size();
				List<Map<String,String>> dataList = query.list();
				map.put("totalSize", size);
				map.put("root",dataList);
				return map;
			}
		});
	}
	/**
	 * 根据网点ID，驾驶员工号，查找出该用户权限网点下的所有驾驶员，填充到下拉框中
	 */
	@SuppressWarnings("unchecked")
	public Map findDrivers(Long deptId, String empCode,String yearMonth, Long userId) {
		final String temp_empCode = empCode;
		final Long temp_userId = userId;
		final Long temp_deptId = deptId;
		final String temp_yearMonth = yearMonth;
		return (Map) this.getHibernateTemplate().execute(new HibernateCallback(){
			private String sql = "SELECT VD.DRIVER_ID,VD.EMP_CODE||'/'||VD.DRIVER_NAME EMP_CODE FROM VM_ARR_DRIVER VD " +
					" LEFT JOIN TM_DEPARTMENT D ON VD.DEPT_ID=D.DEPT_ID " +
					" WHERE 1=1 ";
			@SuppressWarnings("rawtypes")
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Map map = new java.util.HashMap();
				StringBuilder sb = new StringBuilder(sql);
				List<Object> params = new java.util.ArrayList<Object>();
				DeptTreeDaoSql.addDeptIdSql(" AND VD.DEPT_ID", temp_deptId, temp_userId, sb,params);
				if( StringUtils.isNotEmpty(temp_empCode)){
					sb.append(" AND VD.EMP_CODE LIKE ?");
					params.add("%" + temp_empCode + "%");
				}
				if( StringUtils.isNotEmpty(temp_yearMonth)){
					sb.append(" AND VD.DRIVER_ID NOT IN(SELECT SF.DRIVER_ID FROM TT_ARR_SCHEDULE_DRAFT SF WHERE SF.YEAR_MONTH=?)");
					params.add(temp_yearMonth);
				}
				sb.append(" AND VD.EMP_CODE LIKE ?");
				sb.append(" ORDER BY VD.CREATED_TM DESC");
				Query query = session.createSQLQuery(sb.toString());
				
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				int size = query.list().size();
				List<Map<String,String>> dataList = query.list();
				map.put("totalSize", size);
				map.put("root",dataList);
				return map;
			}
		});
	}
	/**
	 * 调班页面获取驾驶员信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> findOptDriver(String optEmpCode, String optDate,String deptCode,Long userId) {
		final String temp_deptCode = deptCode;
		final Long temp_userId = userId;
		final String temp_optEmpCode = optEmpCode;
		final String temp_optDate = optDate;
		final List params = new ArrayList();
		String sql = "SELECT S.SCHEDULE_ID,S.DRIVER_ID,TC.DAY_DT,TC.ARRANGE_NO,D.EMP_CODE,D.DRIVER_NAME FROM TT_ARR_SCHEDULE S " +
				"JOIN TT_ARR_TRANSFER_CLASS TC ON S.SCHEDULE_ID=TC.SCHEDULE_ID LEFT JOIN VM_ARR_DRIVER D  ON S.DRIVER_ID=D.DRIVER_ID " +
				"LEFT JOIN TM_DEPARTMENT DT ON DT.DEPT_ID = S.DEPT_ID";
		
		final StringBuffer sqlBuffer = new StringBuffer(sql);
		//管理员用户权限
		if(1L == userId) {
			sqlBuffer.append(" WHERE S.DEPT_ID IN(SELECT T.DEPT_ID FROM TM_DEPARTMENT T WHERE T.DELETE_FLG=0 ");
			sqlBuffer.append(" START WITH T.DEPT_CODE=? CONNECT BY PRIOR T.DEPT_CODE=T.PARENT_DEPT_CODE ) " );
			params.add(temp_deptCode);
		} else {
			//其他用户
			sqlBuffer.append(" WHERE S.DEPT_ID IN(SELECT R.DEPT_ID FROM TS_USER_DEPT R, ");
			sqlBuffer.append("(SELECT W.DEPT_ID  FROM TM_DEPARTMENT W  WHERE W.DELETE_FLG = 0");
			sqlBuffer.append(" START WITH W.DEPT_CODE = ? ");
			sqlBuffer.append(" CONNECT BY PRIOR W.DEPT_CODE = W.PARENT_DEPT_CODE) C WHERE R.DEPT_ID = C.DEPT_ID ");
			sqlBuffer.append(" AND R.USER_ID = ? )");
			params.add(temp_deptCode);
			params.add(temp_userId);
		}

		if(temp_optDate !=null ){
			sqlBuffer.append(" AND TC.DAY_DT=to_date(?,'YYYY-MM-DD')");
			params.add(temp_optDate);
		}
		if(!ArrFileUtil.isEmpty(temp_optEmpCode)){
			sqlBuffer.append(" AND D.EMP_CODE=? ");
			params.add(temp_optEmpCode);
		}
		sqlBuffer.append(" ORDER BY S.CREATED_TM DESC");
		
		return  this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Map map = new HashMap();
				Query query = session.createSQLQuery(sqlBuffer.toString());
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,Object>> dataList = query.list();
				return dataList;
			}
		});
	}
	/**
	 * 获取调班过程日志信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> findOptInfo(Long id, String dateStr) {
		final Long temp_scheduleId = id;
		final String temp_dateStr = dateStr;
		final List params = new ArrayList();
		String sql = "SELECT S.SCHEDULE_ID,TC.ARRANGE_NO,TC.DAY_DT,TC.REMARK " +
				" FROM TT_ARR_SCHEDULE S JOIN TT_ARR_TRANSFER_CLASS TC ON S.SCHEDULE_ID=TC.SCHEDULE_ID WHERE 1=1" ;
		
		final StringBuffer sqlBuffer = new StringBuffer(sql);
		if( 0!=temp_scheduleId){
			sqlBuffer.append(" AND S.SCHEDULE_ID=?");
			params.add(temp_scheduleId);
		}
		if(temp_dateStr !=null ){
			sqlBuffer.append(" AND TC.DAY_DT=to_date(?,'YYYY-MM-DD')");
			params.add(temp_dateStr);
		}
		sqlBuffer.append(" ORDER BY S.CREATED_TM DESC");
		return  this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sqlBuffer.toString());
				for (int i = 0; i < params.size(); i++) {
					Object param = params.get(i);
					if (param instanceof String) {
						query.setString(i, (String) param);
					} else if (param instanceof Long) {
						query.setLong(i, (Long) param);
					} else if (param instanceof Integer) {
						query.setInteger(i, (Integer) param);
					}
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,Object>> dataList = query.list();
				return dataList;
			}
		});
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer listRepeat(final String empCode, final String yearMonth) {
		final String sql = "SELECT SD.DRIVER_ID FROM TT_ARR_SCHEDULE SD, VM_ARR_DRIVER VD " +
				"WHERE SD.DRIVER_ID=VD.DRIVER_ID ";
		final List params = new ArrayList();
		final StringBuilder sqlBuffer = new StringBuilder(sql);
		//驾驶员
		if( StringUtils.isNotEmpty(empCode.trim())){
			sqlBuffer.append(" AND VD.EMP_CODE=?");
			params.add(empCode.trim());
		}
		//年月
		if( StringUtils.isNotEmpty(yearMonth.trim())){
			sqlBuffer.append(" AND SD.YEAR_MONTH=?");
			params.add(yearMonth.trim());
		}
			List list = this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sqlBuffer.toString());
					for (int i = 0; i < params.size(); i++) {
						Object param = params.get(i);
						if (param instanceof String) {
							query.setString(i, (String) param);
						} else if (param instanceof Long) {
							query.setLong(i, (Long) param);
						} else if (param instanceof Integer) {
							query.setInteger(i, (Integer) param);
						}
					}
				return query.list();
				}
			});
			return list.size();
	}
	/**
	 * 根据驾驶员ID获取已经安排排班的驾驶员
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer findDriverById(String yearMonth,Long id) {
		StringBuilder sqlBuffer = new StringBuilder("SELECT COUNT(*) FROM TT_ARR_SCHEDULE SD WHERE 1=1 ");
		//年月
		if( StringUtils.isNotEmpty(yearMonth)){
			sqlBuffer.append(" AND SD.YEAR_MONTH='"+yearMonth+"' ");
		}
		//班次类型
		if(id !=0 ){
			sqlBuffer.append(" AND SD.DRIVER_ID="+id);
		}
		final String sql = sqlBuffer.toString();
		return (Integer) super.getHibernateTemplate()
		.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException {
				org.hibernate.Query query = session.createSQLQuery(sql);
				Object object = query.uniqueResult();
				if (object != null) {
					return ((BigDecimal)object).intValue();
				}
				return null;
			}
		});
	}
	/**
	 * 根据驾驶员id加载驾驶员
	 * @param empCode
	 * @return
	 */
	public PreSchedule listByDriver(String yearMonth,Long driverId){
		DetachedCriteria dc = DetachedCriteria.forClass(PreSchedule.class);
		dc.add(Restrictions.eq("yearMonth", yearMonth));
		dc.createAlias("driver", "d").add(Restrictions.eq("d.id", driverId));
		List<PreSchedule> list = super.findBy(dc);
		if(null == list || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}
