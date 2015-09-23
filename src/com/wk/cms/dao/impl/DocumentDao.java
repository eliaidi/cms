package com.wk.cms.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IDocumentDao;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.utils.CommonUtils;
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
		hql.append(" order by sort desc ");
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

		hibernateTemplate.delete(findById(id));
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
				.add(Restrictions.in("id", objIds))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
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
	public PageInfo findByMap(Channel currChnl, Map<String, String> params) {
		String where = params.get("where");
		String order = params.get("order");
		String num = params.get("num");
		String startpos = params.get("startpos");
		String hql = "from Document where channel=?";
		
		if(!StringUtils.hasLength(startpos)){
			startpos = "0";
		}
		
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
		}else{
			hql += " order by sort desc " ;
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
		
		long count = (Long) hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createQuery("select count(*) "+hql)
				.setParameter(0, currChnl)
				.uniqueResult();
		
		int stpos = Integer.parseInt(startpos);
		int lmt = (int) (StringUtils.hasLength(num)?Integer.parseInt(num):IDocumentService.MAX_FETCH_SIZE);
		return new PageInfo(stpos, lmt, q.list(), count);
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
		Integer tSort = targetDoc.getSort();

		hibernateTemplate
				.bulkUpdate(
						"update Document set sort=sort-1 where channel.id=? and sort<=?",
						targetDoc.getChannel().getId(), targetDoc.getSort());

		currDoc.setSort(tSort);
		hibernateTemplate.update(currDoc);
	}

	@Override
	public void removeFields(Document persistDoc) {
		
		hibernateTemplate.bulkUpdate("delete from FieldValue where document=?", persistDoc);
	}


	@SuppressWarnings({ "unchecked" })
	@Override
	public PageInfo findByChnlNames(Site site, Map<String, String> params) {
		String cNames = params.get("channels");
		String num = params.get("num");
		String order = params.get("order");
		String where = params.get("where");
		String startpos = params.get("startpos");
		StringBuilder hql = new StringBuilder("from Document where ");
		
		String[] cNameArr = cNames.split(",");
		hql.append("site.id='"+site.getId()+"' and channel.name in ('"+CommonUtils.join(cNameArr, "','")+"')");
		if(StringUtils.hasLength(site.getCanPubSta())){
			hql.append(" and status in ("+site.getCanPubSta()+")");
		}else{
			hql.append(" and status<> 5 ");
		}
		if(StringUtils.hasLength(where)){
			hql.append(" and "+where);
		}
		if(StringUtils.hasLength(order)){
			hql.append(" order by " + order);
		}else{
			hql.append(" order by sort desc ");
		}
		
		List<Document> list = hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createQuery(hql.toString())
				.setFirstResult(StringUtils.hasLength(startpos)?Integer.parseInt(startpos):0)
				.setMaxResults(StringUtils.hasLength(num)?Integer.parseInt(num):IDocumentService.MAX_FETCH_SIZE)
				.list();
		
		long count = (Long) hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createQuery("select count(*) "+hql.toString())
				.uniqueResult();
		
		int stpos = StringUtils.hasLength(startpos)?Integer.parseInt(startpos):0;
		int lmt = (int) (StringUtils.hasLength(num)?Integer.parseInt(num):IDocumentService.MAX_FETCH_SIZE);
		return new PageInfo(stpos, lmt, list, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo findByChannels(Site site, List<Channel> channels,
			Map<String, String> params) {
		
		String num = params.get("num");
		String order = params.get("order");
		String where = params.get("where");
		String startpos = params.get("startpos");
		StringBuilder hql = new StringBuilder("from Document where site.id='"+site.getId()+"'");
		StringBuilder ids = new StringBuilder();
		for(Channel c :channels){
			ids.append(",'"+c.getId()+"'");
		}
		if(ids.length()>0){
			ids.delete(0, 1);
		}
		hql.append(" and channel.id in ("+ids+")");
		
		if(StringUtils.hasLength(site.getCanPubSta())){
			hql.append(" and status in ("+site.getCanPubSta()+")");
		}else{
			hql.append(" and status<> 5 ");
		}
		if(StringUtils.hasLength(where)){
			hql.append(" and "+where);
		}
		if(StringUtils.hasLength(order)){
			hql.append(" order by " + order);
		}else{
			hql.append(" order by sort desc");
		}
		List<Document> list = hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createQuery(hql.toString())
				.setFirstResult(StringUtils.hasLength(startpos)?Integer.parseInt(startpos):0)
				.setMaxResults(StringUtils.hasLength(num)?Integer.parseInt(num):IDocumentService.MAX_FETCH_SIZE)
				.list();
		
		long count = (Long) hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createQuery("select count(*) "+hql.toString())
				.uniqueResult();
		int stpos = StringUtils.hasLength(startpos)?Integer.parseInt(startpos):0;
		int lmt = StringUtils.hasLength(num)?Integer.parseInt(num):IDocumentService.MAX_FETCH_SIZE;
		return new PageInfo(stpos, lmt, list, count);
	}

}
