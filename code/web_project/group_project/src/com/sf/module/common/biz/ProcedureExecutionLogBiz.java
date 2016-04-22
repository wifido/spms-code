package com.sf.module.common.biz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.ProcedureExecutionLogDao;
import com.sf.module.common.domain.ProcedureExecutionLog;
import com.sf.module.common.util.DateFormatType;
import com.sf.module.common.util.DateUtil;
import com.sf.module.common.util.StringUtil;

public class ProcedureExecutionLogBiz extends BaseBiz {
	private ProcedureExecutionLogDao procedureExecutionLogDao;
	
	public HashMap<String,Object> query(HashMap<String, String> queryParameter) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(ProcedureExecutionLog.class);
			if (StringUtil.isNotBlank(queryParameter.get("packageName")))
				dc.add(Restrictions.like("packageName", queryParameter.get("packageName"),
						MatchMode.ANYWHERE));
			if (StringUtil.isNotBlank(queryParameter.get("procedureName")))
				dc.add(Restrictions.like("procedureName", queryParameter.get("procedureName"),
						MatchMode.ANYWHERE));
			if (StringUtil.isNotBlank(queryParameter.get("bigenTime"))
					&& StringUtil.isNotBlank(queryParameter.get("endTime")))
				dc.add(Restrictions.between(
						"exceptionTm",
						DateUtil.parseDate(queryParameter.get("bigenTime"),DateFormatType.yyyyMMdd),
						DateUtil.parseDate(queryParameter.get("endTime"),DateFormatType.yyyyMMdd)));
			dc.addOrder(Order.desc("exceptionTm"));
			IPage<ProcedureExecutionLog> maintenanceMailForDispatch = procedureExecutionLogDao
					.findPageBy(
							dc,
							Integer.parseInt(queryParameter.get("limit")),
							Integer.parseInt(queryParameter.get("start"))
									/ Integer.parseInt(queryParameter.get("limit")));
			map.put("root", maintenanceMailForDispatch.getData());
			map.put("totalSize", maintenanceMailForDispatch.getTotalSize());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return map;
	}

	public ProcedureExecutionLogDao getProcedureExecutionLogDao() {
		return procedureExecutionLogDao;
	}

	public void setProcedureExecutionLogDao(ProcedureExecutionLogDao procedureExecutionLogDao) {
		this.procedureExecutionLogDao = procedureExecutionLogDao;
	}

}
