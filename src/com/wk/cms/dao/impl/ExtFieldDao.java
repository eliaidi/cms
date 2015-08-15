package com.wk.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IExtFieldDao;
import com.wk.cms.model.ExtField;
import com.wk.cms.utils.PageInfo;

@Repository
public class ExtFieldDao implements IExtFieldDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo find(String channelId, PageInfo pageInfo, String query) {
		Criteria cri = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(ExtField.class)
				.add(Restrictions.eq("channel.id", channelId));
		if (StringUtils.hasLength(query)) {
			cri.add(Restrictions.or(
					Restrictions.like("label", query, MatchMode.ANYWHERE),
					Restrictions.like("name", query, MatchMode.ANYWHERE)));
		}
		long count = (Long) cri.setProjection(Projections.rowCount())
				.uniqueResult();
		List<ExtField> list = cri.setProjection(null)
				.setFirstResult(pageInfo.getStart())
				.setMaxResults(pageInfo.getLimit()).list();
		pageInfo.setList(list);
		pageInfo.setTotalCount(count);
		return pageInfo;
	}

}
