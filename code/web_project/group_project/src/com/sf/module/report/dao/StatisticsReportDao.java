package com.sf.module.report.dao;

import static com.sf.module.report.dao.StatisticsReportRepository.EXPORT_STATISTICS_REPORT_SQL;
import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.server.base.dao.BaseDao;

@Repository
public class StatisticsReportDao extends BaseDao {
	private static final String IN = "in_";

	@Transactional
	public int queryStatisticsReportCount(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(StatisticsReportRepository.SQL_QUERY_STATISTICS_REPORT_COUNT);
		setQueryParameterValue(queryParameter, query);
		return Integer.parseInt(query.list().get(0).toString());
	}

	@Transactional
	public List queryStatisticsReportList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session
		        .createSQLQuery(StatisticsReportRepository.SQL_QUERY_STATISTICS_REPORT_LIST)
		        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}
	
	@Transactional
	public List exportReportList(HashMap<String, String> queryParameter) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(EXPORT_STATISTICS_REPORT_SQL)
				.setResultTransformer(ALIAS_TO_ENTITY_MAP);
		setQueryParameterValue(queryParameter, query);
		return query.list();
	}

	private void setQueryParameterValue(HashMap<String, String> queryParameter, Query query) {
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
