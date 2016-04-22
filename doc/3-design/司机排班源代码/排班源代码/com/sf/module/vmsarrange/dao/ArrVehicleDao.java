/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2014-05-30     方芳                        创建
 **********************************************/
package com.sf.module.vmsarrange.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.module.vmsarrange.domain.ArrVehicle;
import com.sf.module.vmsarrange.util.ArrFileUtil;

/**
 * 车辆视图dao实现类
 * 
 * @author 方芳 (350614) 2014-5-30
 */
public class ArrVehicleDao extends BaseEntityDao<ArrVehicle> implements
		IArrVehicleDao {
	// 查找车型
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> listModelBase(String modelBase) {
		String sqlbak = "select distinct brandModel||wheelbase as modelBase from com.sf.module.vmsarrange.domain.ArrVehicle " +
				" where brandModel is not null and vehicleState in (1,3) and usage = 2 ";
		if (!ArrFileUtil.isEmpty(modelBase)) {
			sqlbak += " and brandModel||wheelbase like '%" + modelBase + "%' ";
		}
		final String sql = sqlbak;
		return (List<Map>) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	// 根据车牌号查找车辆
	public ArrVehicle listByCode(String vehicleCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrVehicle.class);
		dc.add(Restrictions.eq("vehicleCode", vehicleCode));
		dc.add(Restrictions.eq("usage", 2));
		dc.add(Restrictions.in("vehicleState", (new Integer[] { 1, 3 })));
		List<ArrVehicle> list = super.findBy(dc);
		if (null == list || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	// 查找所有正常停用车辆
	public List<ArrVehicle> listAllVehicle() {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrVehicle.class);
		dc.add(Restrictions.eq("usage", 2));
		dc.add(Restrictions.in("vehicleState", (new Integer[] { 1, 3 })));
		return super.findBy(dc);
	}

	// 分页查找车辆
	public IPage<ArrVehicle> listVehiclePage(String vehicleCode,
			String modelBase, int pageSize, int pageIndex) {
		DetachedCriteria dc = DetachedCriteria.forClass(ArrVehicle.class);
		// 车牌号
		if (!ArrFileUtil.isEmpty(vehicleCode)) {
			dc.add(Restrictions.like("vehicleCode", vehicleCode,
					MatchMode.ANYWHERE));
		}
		// 车型
		if (!ArrFileUtil.isEmpty(modelBase)) {
			dc.add(Restrictions.sqlRestriction(" {alias}.brand_model||{alias}.wheelbase = ? ",
					modelBase, StandardBasicTypes.STRING));
		}
		dc.add(Restrictions.eq("usage", 2));
		// 只取正常停用车辆
		dc.add(Restrictions.in("vehicleState", (new Integer[] { 1, 3 })));
		dc.addOrder(Order.desc("id"));
		return super.findPageBy(dc, pageSize, pageIndex);
	}

}
