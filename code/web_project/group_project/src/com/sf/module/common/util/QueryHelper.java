package com.sf.module.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.sf.framework.server.base.dao.BaseDao;

public class QueryHelper extends BaseDao {

	private static final String IN = "in_";

	public String getQueryConditions(HashMap<String, String> queryParameter,
			HashMap<String, String> conditionsMap) {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : queryParameter.entrySet()) {
			String parameter = ":" + entry.getKey();

			if (conditionsMap.containsKey(entry.getKey().toUpperCase())
					&& StringUtil.isNotBlank(entry.getValue())) {

				if (conditionsMap.get(entry.getKey()).contains("IN")) {
					sb.append(conditionsMap.get(entry.getKey().toUpperCase())
							.replace("$$", queryParameter.get(entry.getKey())));
				} else {
					sb.append(conditionsMap.get(entry.getKey().toUpperCase())
							.replace("$$", parameter));
				}
			}
		}
		return sb.toString();
	}

	public List getQuery(HashMap<String, String> queryParameter, String hql) {
		Session session = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(hql).setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP);
			setQueryParameterValue(queryParameter, query);
			return query.list();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void setQueryParameterValue(HashMap<String, String> queryParameter,
			Query query) {
		String[] parameters = query.getNamedParameters();
		for (String param : parameters) {
			if (param.startsWith(IN)) {
				query.setParameterList(param, queryParameter.get(param).split(","));
				continue;
			}
			query.setParameter(param, queryParameter.get(param));
		}
	}
}
