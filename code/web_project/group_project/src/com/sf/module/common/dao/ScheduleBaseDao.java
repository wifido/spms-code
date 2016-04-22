package com.sf.module.common.dao;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import com.sf.framework.base.domain.IEntity;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.common.domain.sql.QueryStatement;

public abstract class ScheduleBaseDao<E extends IEntity> extends BaseEntityDao {
	private static final String LIMIT = "limit";
	private static final String START = "start";

	@Transactional(readOnly = true)
	public <Result> Result query(QueryDelegate queryDelegate) {
		Query query = constructQuery(queryDelegate);
		return (Result) queryDelegate.onQuery(query);
	}

	@Transactional(readOnly = true)
	public <Result> Result queryWithPagination(QueryDelegate queryDelegate) {
		Query query = constructQuery(queryDelegate);
		if (queryDelegate.parametersMap.isEmpty()) {
			query.setFirstResult(queryDelegate.start);
			query.setMaxResults(queryDelegate.limit);
			return (Result) queryDelegate.onQuery(query);
		}
		query.setFirstResult(Integer.parseInt(String.valueOf(queryDelegate.parametersMap.get(START))));
		query.setMaxResults(Integer.parseInt(String.valueOf(queryDelegate.parametersMap.get(LIMIT))));
		return (Result) queryDelegate.onQuery(query);
	}

	private Query constructQuery(QueryDelegate queryDelegate) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if (queryDelegate.parametersMap.isEmpty()) {
			return this.initQuery(queryDelegate.parameters, queryDelegate.queryStatement, session);
		}
		return this.initQuery(queryDelegate.parametersMap, queryDelegate.queryStatement, session);
	}

	private Query initQuery(List<?> parameters, QueryStatement specifiedQueryStatement, Session session) {
		Query query = session.createSQLQuery(specifiedQueryStatement.toQuery().toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (int index = 0; index < parameters.size(); index++) {
			query.setParameter(index, parameters.get(index));
		}
		return query;
	}

	private Query initQuery(Map<String, String> parameters, QueryStatement specifiedQueryStatement, Session session) {
		Query query = session.createSQLQuery(specifiedQueryStatement.toQuery().toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		String[] params = query.getNamedParameters();
		for (String param : params) {
			query.setParameter(param, parameters.get(param));
		}
		return query;
	}

	public static abstract class QueryDelegate<Result> {
		public final QueryStatement queryStatement;
		public List<?> parameters = newArrayList();
		public Map<String, String> parametersMap = newHashMap();
		public int start = 0;
		public int limit = 0;

		public QueryDelegate(QueryStatement queryStatement, List<?> parameters, int start, int limit) {
			this.queryStatement = queryStatement;
			this.parameters = parameters;
			this.start = start;
			this.limit = limit;
		}

		public QueryDelegate(QueryStatement queryStatement, List<?> parameters) {
			this(queryStatement, parameters, 0, 100000);
		}

		public QueryDelegate(QueryStatement queryStatement, Map<String, String> parametersMap) {
			this.queryStatement = queryStatement;
			this.parametersMap = parametersMap;
		}

		public abstract Result onQuery(Query query);
	}
}
