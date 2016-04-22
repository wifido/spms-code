package com.sf.module.dispatch.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.biz.BaseBiz;
import com.sf.module.common.dao.IOssDepartmentDao;
import com.sf.module.common.domain.OssDepartment;
import com.sf.module.common.domain.SysConfig;
import com.sf.module.common.util.StringUtil;
import com.sf.module.dispatch.dao.MaintenanceMailDao;
import com.sf.module.dispatch.domain.MaintenanceMail;

@Component
public class MaintenanceMailBiz extends BaseBiz {
	@Resource
	private MaintenanceMailDao maintenanceMailDao;
	
	@Resource
	private IOssDepartmentDao ossDepartmentDao;

	public HashMap<String, Object> queryMaintenanceMailByDeptCode(Map<String, String> params) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		DetachedCriteria dc = DetachedCriteria.forClass(MaintenanceMail.class);
		dc.add(Restrictions
				.sqlRestriction(
						"{alias}.department_Code in ( SELECT dept_code FROM TM_DEPARTMENT  WHERE DELETE_FLG = 0 START WITH dept_code = ?    CONNECT BY PRIOR DEPT_CODE = PARENT_DEPT_CODE )",
						params.get("DEPARTMENT_CODE"), Hibernate.STRING));
		if (StringUtil.isNotBlank(params.get("EMAIL_ACCOUNT")))
			dc.add(Restrictions.like("emailAccount", params.get("EMAIL_ACCOUNT"),
					MatchMode.ANYWHERE));
		IPage<MaintenanceMail> maintenanceMailForDispatch = maintenanceMailDao.findPageBy(dc,
				Integer.parseInt(params.get("limit")), Integer.parseInt(params.get("start"))
						/ Integer.parseInt(params.get("limit")));
		map.put("root", maintenanceMailForDispatch.getData());
		map.put("totalSize", maintenanceMailForDispatch.getTotalSize());
		return map;
	}
	
	public void saveOrUpdate(MaintenanceMail maintenanceMail) {
		OssDepartment ossDepartment = ossDepartmentDao.getDepartmentByDeptCode(maintenanceMail.getDepartmentCode());
		maintenanceMail.setAreaCode(ossDepartment.getAreaCode());
		maintenanceMail.setDivisionCode(ossDepartment.getDivisionCode());
		if (null != maintenanceMail.getId()) {
			maintenanceMailDao.update(maintenanceMail);
			return;
		}
		maintenanceMailDao.save(maintenanceMail);
	}
	
	public boolean validExists(Map<String, String> params) {
		DetachedCriteria dc = DetachedCriteria.forClass(MaintenanceMail.class);
		dc.add(Restrictions.eq("departmentCode", params.get("DEPARTMENT_CODE")));
		List<MaintenanceMail> list =  maintenanceMailDao.findBy(dc);
		return list!=null&&list.size()>0;
	}
	
	public void remove(Map<String, String> params) {
		MaintenanceMail entity = maintenanceMailDao.load(Long.parseLong(params.get("id")));
		if (entity != null) {
			maintenanceMailDao.remove(Long.parseLong(params.get("id")));
		}
	}
}
