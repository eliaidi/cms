package com.wk.cms.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
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

		StringBuilder hql = new StringBuilder(
				" from Document where channel.id=?");
		if (StringUtils.hasLength(query)) {
			hql.append(" and ( title like ? or abst like ? or content like ?)");
		}
		hql.append(" order by sort desc");
		Query cq = s.createQuery("select count(*) " + hql.toString())
				.setParameter(0, channelId);
		Query lq = s.createQuery(hql.toString()).setParameter(0, channelId);
		if (StringUtils.hasLength(query)) {
			cq.setParameter(1, "%"+query+"%").setParameter(2, "%"+query+"%")
					.setParameter(3, "%"+query+"%");
			lq.setParameter(1, "%"+query+"%").setParameter(2, "%"+query+"%")
					.setParameter(3, "%"+query+"%");
		}

		Long count = (Long) cq.uniqueResult();
		List<Document> documents = lq.list();
		// List<Document> documents = s.createQuery("from Document")
		// .setFirstResult(pageInfo.getStart())
		// .setMaxResults(pageInfo.getLimit())
		// .list();

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

		Query q = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		if (!CommonUtils.isEmpty(params)) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		q.setFirstResult(pageInfo.getStart());
		q.setMaxResults(pageInfo.getLimit());
		return q.list();
	}

	@Override
	public List<Document> findByMap(Channel currChnl, Map<String, String> params) {
		String where = params.get("where");
		String order = params.get("order");
		String num = params.get("num");
		String startpos = params.get("startpos");
		String hql = "from Document where channel=?";
		if (StringUtils.hasLength(currChnl.getSite().getCanPubSta())) {
			hql += " and status in (" + currChnl.getSite().getCanPubSta() + ")";
		} else {
			hql += " and status <> 5";
		}

		if (StringUtils.hasLength(where)) {
			hql += " and " + where;
		}
		if (StringUtils.hasLength(order)) {
			hql += " order by " + order;
		}

		Query q = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		q.setParameter(0, currChnl);

		if (StringUtils.hasLength(startpos)) {
			q.setFirstResult(Integer.parseInt(startpos));
		}
		if (StringUtils.hasLength(num)) {
			q.setMaxResults(Integer.parseInt(num));
		}
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer findMaxSortOf(Channel channel) {

		List<Object> list = (List<Object>) hibernateTemplate.find(
				"select max(sort) from Document where channel=?", channel);
		if (CommonUtils.isEmpty(list)) {
			return 0;
		}
		return list.get(0) == null ? 0 : Integer.parseInt(list.get(0)
				.toString());
	}

	@Override
	public void move(String currId, String targetId) {

		Document currDoc = findById(currId);
		Document targetDoc = findById(targetId);

		move(currDoc, targetDoc);
	}

	@Override
	public void move(Document currDoc, Document targetDoc) {

		if (!currDoc.getChannel().equals(targetDoc.getChannel())) {
			currDoc.setChannel(targetDoc.getChannel());
			currDoc.setSite(targetDoc.getSite());
		}
		currDoc.setSort(targetDoc.getSort());

		hibernateTemplate
				.bulkUpdate(
						"update Document set sort=sort-1 where channel.id=? and sort<=?",
						targetDoc.getChannel().getId(), targetDoc.getSort());

		hibernateTemplate.update(currDoc);
	}

}
