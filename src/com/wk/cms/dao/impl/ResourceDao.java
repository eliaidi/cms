package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IResourceDao;
import com.wk.cms.model.Resource;
import com.wk.cms.utils.PageInfo;

@Repository
public class ResourceDao implements IResourceDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public PageInfo find(PageInfo info, String query) {
		
		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Resource.class);
		
		if(StringUtils.hasLength(query)){
			c.add(Restrictions.or(Restrictions.like("name", query, MatchMode.ANYWHERE),
					Restrictions.like("value", query, MatchMode.ANYWHERE)));
		}
		c.addOrder(Order.desc("crTime"));
		long count = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		List<Resource> resources = c.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setFirstResult(info.getStart())
				.setMaxResults(info.getLimit())
				.list();
		
		info.setList(resources);
		info.setTotalCount(count);
		return info;
	}
	@Override
	public Resource findById(String id) {
		return hibernateTemplate.get(Resource.class, id);
	}
	@Override
	public void save(Resource r) {
		
		if(StringUtils.hasLength(r.getId())){
			hibernateTemplate.update(r);
		}else{
			r.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(r);
		}
	}
	@Override
	public void delete(String[] ids) {

		for(String id : ids){
			delete(id);
		}
	}
	@Override
	public void delete(String id) {
		hibernateTemplate.delete(findById(id));
	}

	@Override
	public List<Resource> find(String[] ids) {

		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Resource.class);

		return c.add(Restrictions.in("id", ids))
				.setFetchSize(20)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

}
