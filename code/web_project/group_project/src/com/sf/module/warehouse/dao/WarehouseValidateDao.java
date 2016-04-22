package com.sf.module.warehouse.dao;

import static com.sf.module.common.domain.Constants.TOTALSIZE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sf.module.common.util.QueryHelper;

public class WarehouseValidateDao extends QueryHelper {
	public int validateAuthority(HashMap<String, String> queryParameter) {
		List<Map<String, Object>> list = getQuery(queryParameter, WarehouseSchedulingHql.SQL_CHECK_USER_PERMISSIONS);
		return Integer.parseInt(list.get(0).get(TOTALSIZE).toString());
	}
}
