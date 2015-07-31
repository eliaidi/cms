package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IDocumentDao;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.annotations.ShowArea;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.HibernateUtils;
import com.wk.cms.utils.PageInfo;

@Repository
public class DocumentDao implements IDocumentDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings({ "unchecked" })
	@Override
	public PageInfo find(String channelId, PageInfo pageInfo, String query) {

		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();

		Criteria c = s.createCriteria(Document.class);
		c.add(Restrictions.eq("channel.id", channelId));

		if (StringUtils.hasLength(query)) {
			c.add(Restrictions.or(
					Restrictions.like("title", query, MatchMode.ANYWHERE),
					Restrictions.like("abst", query, MatchMode.ANYWHERE),
					Restrictions.like("content", query, MatchMode.ANYWHERE)));
		}
		Long count = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();

		List<Document> documents = c
				.addOrder(Order.desc("crTime"))
				.setProjection(
						HibernateUtils.getProjections(Document.class,
								ShowArea.LIST))
				.setResultTransformer(Transformers.aliasToBean(Document.class))
				.setFirstResult(pageInfo.getStart())
				.setMaxResults(pageInfo.getLimit()).list();

		return new PageInfo(documents, count);
	}

	@Override
	public void save(Document document) {

		if (!StringUtils.hasLength(document.getId())) {
			document.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(document);
		} else {
			hibernateTemplate.update(document);
		}
	}

	@Override
	public Document findById(String id) {
		return hibernateTemplate.get(Document.class, id);
	}

	@Override
	public void deleteById(String id) {

		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();

		s.delete(findById(id));
	}

	@Override
	public void refresh(Channel channel) {

		hibernateTemplate.bulkUpdate(
				"update Document d set d.site=? where d.channel=?",
				channel.getSite(), channel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findByIds(String[] objIds) {
		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Document.class)
				.add(Restrictions.in("id", objIds)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findAll(Channel channel, int pageSize,
			DetachedCriteria dc) {
		return (List<Document>) hibernateTemplate.findByCriteria(
				dc.add(Restrictions.eq("channel", channel)), 0, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> find(String hql, Object[] params) {

		return (List<Document>) hibernateTemplate.find(hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> find(String hql, Object[] params, PageInfo pageInfo) {

		Query q = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql);
		if(!CommonUtils.isEmpty(params)){
			for(int i=0;i<params.length;i++){
				q.setParameter(i, params[i]);
			}
		}
		q.setFirstResult(pageInfo.getStart());
		q.setMaxResults(pageInfo.getLimit());
		return q.list();
	}

}
