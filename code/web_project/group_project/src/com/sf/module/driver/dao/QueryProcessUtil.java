package com.sf.module.driver.dao;

import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;

public abstract class QueryProcessUtil {
	private String sql;

	protected abstract Map<String, String> buildCondition();

	protected abstract String buildSql(String originalSql);

	protected List<Map<String, Object>> query(Session session, String originalSql) {
		this.sql = buildSql(originalSql);
		Query query = session.createSQLQuery(sql).setResultTransformer(ALIAS_TO_ENTITY_MAP);
		this.injectParameter(query);
		
		return query.list();
	}

	private void injectParameter(Query query) {
		Map<String, String> parameter = this.buildCondition();
		for (String cell : query.getNamedParameters()) {
			query.setParameter(cell, parameter.get(cell));
		}
	}
}