package com.wk.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.model.Template;
import com.wk.cms.model.annotations.ShowArea;
import com.wk.cms.utils.HibernateUtils;
import com.wk.cms.utils.PageInfo;

@Repository
public class TemplateDao implements ITemplateDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo find(PageInfo pageInfo, String query) {

		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Template.class);

		if (StringUtils.hasLength(query)) {
			c.createAlias("file", "f").add(
					Restrictions.or(Restrictions.like("name", query,
							MatchMode.ANYWHERE)));
		}
		long count = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		List<Template> list = c
				.setProjection(
						HibernateUtils.getProjections(Template.class,
								ShowArea.LIST))
				.setResultTransformer(Transformers.aliasToBean(Template.class))
				.setFirstResult(pageInfo.getStart())
				.setMaxResults(pageInfo.getLimit()).list();

		return new PageInfo(list, count);
	}

}
