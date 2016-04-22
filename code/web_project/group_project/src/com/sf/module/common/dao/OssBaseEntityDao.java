/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2010-11-4      谢年兵        创建
 **********************************************/
package com.sf.module.common.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sf.framework.base.IPage;
import com.sf.framework.base.domain.IEntity;
import com.sf.framework.core.Page;
import com.sf.framework.core.exception.DaoException;
import com.sf.framework.server.base.dao.BaseEntityDao;
import com.sf.framework.server.base.dao.IEntityDao;
import com.sf.framework.util.StringUtils;


/**
 *
 * 运价维护的Dao基类，主要是用来修正BaseEntityDao里的一些问题
 * @author 谢年兵  2010-11-4
 *
 */
public class OssBaseEntityDao<E extends IEntity> extends BaseEntityDao<E> implements IEntityDao<E> {

	@SuppressWarnings("unchecked")
	@Override
	public IPage<E> findPageBy(final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageIndex) throws DaoException {
		return (IPage<E>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //去重复记录
				criteria.setFirstResult(pageSize * (pageIndex));
				criteria.setMaxResults(pageSize);
				return new Page<E>(criteria.list(), total, pageSize, pageIndex);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findBy(final DetachedCriteria detachedCriteria)
			throws DaoException {
		return (List<E>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //去重复记录
				return criteria.list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPage<E> findPageBy(final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageIndex, final String sortField, final boolean isAsc) throws DaoException {
		return (IPage<E>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				if (StringUtils.isNotEmpty(sortField)) {
					criteria.addOrder(isAsc ? Order.asc(sortField) : Order.desc(sortField));
				}
				Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); //去重复记录
				criteria.setFirstResult(pageSize * (pageIndex));
				criteria.setMaxResults(pageSize);
				return new Page<E>(criteria.list(), total, pageSize, pageIndex);
			}
		});
	}
	
}
