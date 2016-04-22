package com.sf.module.report.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sf.framework.server.base.dao.BaseDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP;

@Repository
public class SchedulingInputStatisticalDao extends BaseDao{

	@Transactional
    public int queryTotalSize(String departmentCode, String yearMonth) {
        return querySchedulingInput(departmentCode, yearMonth).size();
    }

    @Transactional
    public List querySchedulingInput(String departmentCode, String yearMonth) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(ScheduledInputRepository.getQueryCountSql()).setResultTransformer(ALIAS_TO_ENTITY_MAP);

        setQueryParameter(departmentCode, yearMonth, query);
        return query.list();
    }

    @Transactional
    public List query(String departmentCode, String yearMonth, String start, String limit) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(ScheduledInputRepository.getQueryPageSql()).setResultTransformer(ALIAS_TO_ENTITY_MAP);

        setQueryParameter(departmentCode, yearMonth, start, limit, query);
        return query.list();
    }

    public void setQueryParameter(String departmentCode, String yearMonth, Query query) {
        query.setParameter(0, departmentCode);
        query.setParameter(1, yearMonth);
    }

    public void setQueryParameter(String departmentCode, String yearMonth,String start, String limit, Query query) {
        query.setParameter(0, departmentCode);
        query.setParameter(1, yearMonth);
        query.setParameter(2, start);
        query.setParameter(3, Integer.parseInt(start) + Integer.parseInt(limit));
    }
}
