package com.sf.module.driver.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sf.module.common.util.StringUtil;

public class BasicQueryProcessUtil extends QueryProcessUtil {
	private Map<String, String> allConditions = new HashMap<String, String>();
	private Set<String> optionalConditions = new HashSet<String>();

	public BasicQueryProcessUtil addNecessaryCondition(String columnName, String value) {
		this.allConditions.put(columnName, value);
		return this;
	}

	public static BasicQueryProcessUtil buildInstance() {
		return new BasicQueryProcessUtil();
	}
	
	public BasicQueryProcessUtil addOptionalCondition(String columnName, String value) {
		if (StringUtil.isNotBlank(value)) {
			this.allConditions.put(columnName, value);
			this.optionalConditions.add(columnName);
		}
		
		return this;
	}

	@Override
	protected Map<String, String> buildCondition() {
		return allConditions;
	}

	private Set<String> extractOptionalConditions() {
		this.optionalConditions.retainAll(this.allConditions.keySet());
		return this.optionalConditions;
	}

	public String buildSql(String originalSql) {
		Set<String> optionalConditionAsKey = extractOptionalConditions();
		return String.format(originalSql, buildSqlSegment(optionalConditionAsKey));
	}

	private String buildSqlSegment(Set<String> keys) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String key : keys) {
			stringBuilder.append(String.format(" and t.%s = :%s", key, key));
		}

		return stringBuilder.toString();
	}
}