/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-06-30     632898 李鹏       创建
 **********************************************/

package com.sf.module.operation.dao;

import static com.sf.module.common.domain.Constants.FIELD_FLAG;
import static com.sf.module.common.domain.Constants.LIMIT;
import static com.sf.module.common.domain.Constants.ROOT;
import static com.sf.module.common.domain.Constants.START;
import static com.sf.module.common.domain.Constants.TOTAL_SIZE;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_DEPT_ID;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_EXPORT_PROCESS;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_PROCESS_CODE;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_PROCESS_NAME;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_STATUS;
import static com.sf.module.operation.biz.ProcessBiz.FIELD_USER_ID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.operation.domain.Process;
import com.sf.module.operation.domain.ProcessDetail;
import com.sf.module.operation.util.CommonUtil;

/**
 *
 * 工序管理的Dao实现类
 * @author 632898 李鹏  2014-06-30
 *
 */
public class ProcessDao extends BaseEntityDao<Process> implements IProcessDao {

    /**
	 * 查询工序信息 
	 * @author 632898 李鹏
	 * @date 2014-06-21
	 * @return dataMap
	 */
    @Transactional(readOnly = true)
	public HashMap queryProcess(HashMap paramsMap) {
		HashMap dataMap = new HashMap();

        // 查询参数
		List<Object> queryParameters = new ArrayList<Object>();

        // 构造查询
        String sql = builderQueryProcessSqlAndSetQueryParameter(paramsMap, queryParameters);

		//创建新的session
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 设置查询参数
        setQueryParameter(queryParameters, query);
    	dataMap.put(TOTAL_SIZE, query.list().size());
        // 设置分页参数
        setPagingParameter(paramsMap, query);

		//取查询结果
		List list = query.list();

		//组装数据格式
	
		dataMap.put(ROOT, list);

		return dataMap;
	}

    private void setPagingParameter(HashMap paramsMap, Query query) {
        if (paramsMap.get(START) != null)
            query.setFirstResult(Integer.parseInt(paramsMap.get(START).toString()));
        if (paramsMap.get(LIMIT) != null)
            query.setMaxResults(Integer.parseInt(paramsMap.get(LIMIT).toString()));
    }

    private void setQueryParameter(List<Object> params, Query query) {
        // 设置查询参数
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
    }

    private String builderQueryProcessSqlAndSetQueryParameter(HashMap paramsMap, List<Object> params) {
    	String flag = paramsMap.get(FIELD_FLAG).toString();
        Long userId = Long.parseLong(paramsMap.get(FIELD_USER_ID).toString());
        StringBuilder sql = new StringBuilder();
        // 查询语句主体
        sql.append(" SELECT P.*, D.AREA_CODE ,D.DEPT_NAME ,D.DEPT_CODE   FROM TM_PB_PROCESS_INFO P,TM_DEPARTMENT D WHERE P.DEPT_ID = D.DEPT_ID  ");
        // 网点ID
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_DEPT_ID))) {
            long deptId = Long.parseLong(paramsMap.get(FIELD_DEPT_ID).toString());
            //查询有权限的子网点
            if (CommonUtil.isAdmin(userId)) {
                if (deptId != 1  && flag.equals(FIELD_EXPORT_PROCESS)) {
                    sql.append(" AND   P.DEPT_ID  IN ( SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0  START WITH DEPT_ID=?  CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) ");
                    params.add(paramsMap.get(FIELD_DEPT_ID));
                } else {
                    sql.append(" AND   P.DEPT_ID  IN ( SELECT DEPT_ID FROM TM_DEPARTMENT WHERE DELETE_FLG=0  and  DEPT_ID=?   ) ");
                    params.add(paramsMap.get(FIELD_DEPT_ID));
                }
            } else {
                if (deptId != 1 && flag.equals(FIELD_EXPORT_PROCESS)) {
                    sql.append(" AND   P.DEPT_ID  IN ( SELECT A.DEPT_ID FROM  TS_USER_DEPT A,(SELECT  T.DEPT_ID  FROM TM_DEPARTMENT  T  WHERE DELETE_FLG=0  START WITH DEPT_ID=? CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE ) B ");
                    sql.append("  WHERE A.USER_ID = ?  AND B.DEPT_ID = A.DEPT_ID)  ");
                    params.add(paramsMap.get(FIELD_DEPT_ID));
                    params.add(userId);
                } else {
                    sql.append(" AND   P.DEPT_ID  IN ( SELECT A.DEPT_ID FROM  TS_USER_DEPT A,(SELECT  T.DEPT_ID  FROM TM_DEPARTMENT  T  WHERE DELETE_FLG=0  AND DEPT_ID=? ) B ");
                    sql.append("  WHERE A.USER_ID = ?  AND B.DEPT_ID = A.DEPT_ID)  ");
                    params.add(paramsMap.get(FIELD_DEPT_ID));
                    params.add(userId);
                }
            }

        }
        // 工序名称
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_PROCESS_NAME))) {
            sql.append("AND  P.PROCESS_NAME  LIKE ? ");
            params.add("%" + paramsMap.get(FIELD_PROCESS_NAME).toString() + "%");
        }
        // 工序代码
        if (!StringUtils.isEmpty(paramsMap.get(FIELD_PROCESS_CODE))) {
            sql.append("AND  P.PROCESS_CODE  = ? ");
            params.add(paramsMap.get(FIELD_PROCESS_CODE).toString());
        }

        // 按照主键排序
        sql.append(" ORDER  BY   P.PROCESS_AREA,D.AREA_CODE  DESC ");
        return sql.toString();
    }

    /**
	 * 查询总部同步的工序信息 
	 * @author 664525 莫航
	 * @date 2014-07-23
	 * @return dataMap
	 */
    @Transactional(readOnly = true)
	public HashMap tbQueryProcess(HashMap paramsMap) {
		HashMap dataMap = new HashMap();
		Long userId =  Long.parseLong(paramsMap.get(FIELD_USER_ID).toString());
		List<Object> parameters = new ArrayList<Object>();

        // 构造查询
        String sql = builderHQProcessQuerySqlAndSetQueryParameter(paramsMap, parameters);
        // 获取Session
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 设置查询参数
        setQueryParameter(parameters, query);

		//取查询结果
		List list = query.list();		
		dataMap.put(ROOT, list);

		return dataMap;
	}

    private String builderHQProcessQuerySqlAndSetQueryParameter(HashMap paramsMap, List<Object> parameters) {
        StringBuilder stringBuffer = new StringBuilder();
        // 查询语句主体
        stringBuffer.append(" SELECT T.PROCESS_ID, T.PROCESS_CODE,T.PROCESS_NAME,T.PROCESS_AREA,T.PROCESS_TOOL,T.ESTIMATE_VALUE,T.INTENSITY_VALUE,T.SKILL_VALUE,T.DIFFICULTY_VALUE,T.DIFFICULTY_MODIFY_VALUE,P2.PROCESS_CODE P2PROCESS FROM( ");
        stringBuffer.append(" SELECT P.PROCESS_ID, P.PROCESS_CODE,P.PROCESS_NAME,P.PROCESS_AREA,P.PROCESS_TOOL,P.ESTIMATE_VALUE,P.INTENSITY_VALUE,P.SKILL_VALUE,P.DIFFICULTY_VALUE,P.DIFFICULTY_MODIFY_VALUE  FROM TM_PB_PROCESS_INFO P WHERE  P.DEPT_ID = 1 )T ");
        stringBuffer.append(" LEFT JOIN TM_PB_PROCESS_INFO P2 ON T.PROCESS_CODE = P2.PROCESS_CODE AND P2.DEPT_ID = ? ");
        parameters.add(paramsMap.get(FIELD_DEPT_ID).toString());
        return stringBuffer.toString();
    }

    public Process findBy(Long departmentId,String processCode){
		DetachedCriteria dc = DetachedCriteria.forClass(Process.class);
		dc.add(Restrictions.eq(FIELD_PROCESS_CODE, processCode));
		dc.add(Restrictions.eq(FIELD_DEPT_ID, departmentId));
		dc.add(Restrictions.eq(FIELD_STATUS, 1));
		List<Process> list = this.findBy(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    public List<Process> findByDeptId(Long departmentId) {
        DetachedCriteria dc = DetachedCriteria.forClass(Process.class);
        dc.add(Restrictions.eq(FIELD_DEPT_ID, departmentId));
        dc.add(Restrictions.eq(FIELD_STATUS, 1));
        return this.findBy(dc);
    }

    public List<Process> pushMsg(long deptId) {
        DetachedCriteria dc = DetachedCriteria.forClass(Process.class);
        String hql = " From  " + Process.class.getName() + "  where  (createTm >= sysdate - 7  or  modifiedTm >= sysdate - 7)  and   deptId = " + deptId;
        return this.find(hql);
    }

	/**
	 * 查询网点下该工序是否在使用
	 * @author 632898 李鹏
	 * @date 2014-07-26
	 */
    public boolean isUsedProcess(Long deptId, String processCode) {
        DetachedCriteria dc = DetachedCriteria.forClass(ProcessDetail.class);
        dc.add(Restrictions.eq(FIELD_DEPT_ID, deptId));
        dc.add(Restrictions.eq(FIELD_PROCESS_CODE, processCode));
        List list = this.findBy(dc);
        return list.size() > 0;
    }

	/**
	 * 通过网点及工序代码查询此工序是否存在 不考虑工序状态
	 * @author 632898 李鹏
	 * @date 2014-07-26
	 */
    public List<Process> loadProcessByCode(Long deptId, String processCode) {
        DetachedCriteria dc = DetachedCriteria.forClass(Process.class);
        dc.add(Restrictions.eq(FIELD_DEPT_ID, deptId));
        dc.add(Restrictions.eq(FIELD_PROCESS_CODE, processCode));
        List<Process> list = this.findBy(dc);
        return list;
    }
}