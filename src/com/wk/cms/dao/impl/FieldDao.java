package com.wk.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IFieldDao;
import com.wk.cms.model.Field;
import com.wk.cms.utils.PageInfo;

@Repository
public class FieldDao implements IFieldDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo find(String siteId, PageInfo pageInfo, String query) {

		Criteria cri = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(Field.class);
		cri.add(Restrictions.eq("site.id", siteId))
		.add(Restrictions.isNull("parent")).addOrder(
				Order.desc("crTime"));
		if (StringUtils.hasLength(query)) {
			cri.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
		
		long count = (Long) cri.setProjection(Projections.rowCount()).uniqueResult();
		List<Field> list = cri.setProjection(null).setFirstResult(pageInfo.getStart())
				.setMaxResults(pageInfo.getLimit())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		pageInfo.setTotalCount(count);
		pageInfo.setList(list);
		return pageInfo;
	}

	@Override
	public void save(Field field) {
		
		hibernateTemplate.saveOrUpdate(field);
	}

	@Override
	public Field findById(String id) {
		return hibernateTemplate.get(Field.class, id);
	}

	@Override
	public void delete(Field f) {
		hibernateTemplate.delete(f);
	}

	@Override
	public List<Field> find(String[] ids) {

		Criteria cri = hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(Field.class);
		cri.add(Restrictions.in("id", ids));
		
		return cri.list();
	}

}
