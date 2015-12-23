package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.dao.IPubLogDao;
import com.wk.cms.model.PubLog;
import com.wk.cms.utils.PageInfo;

@Repository
public class PubLogDao implements IPubLogDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public PubLog save(PubLog log) {
		
		if(!StringUtils.hasLength(log.getId())){
			log.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(log);
		}else{
			hibernateTemplate.update(log);
		}
		hibernateTemplate.flush();
		return log;
	}
	@Override
	public PageInfo find(String type, PageInfo pageInfo) {
		
		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(PubLog.class);
		
		if("done".equalsIgnoreCase(type)){
			c.add(Restrictions.eq("success", true));
		}else if("fail".equalsIgnoreCase(type)){
			c.add(Restrictions.eq("success", false));
		}else if("doing".equalsIgnoreCase(type)){
			c.add(Restrictions.isNull("success"));
		}
		
		c.addOrder(Order.desc("startTime"));
		long count = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		List<PubLog> pubLogs = c.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		
		pageInfo.setList(pubLogs);
		pageInfo.setTotalCount(count);
		return pageInfo;
	}
	@Override
	public void delete(String ids) {
		
		hibernateTemplate.bulkUpdate("delete from PubLog where id in ("+ids+")");
	}

}
