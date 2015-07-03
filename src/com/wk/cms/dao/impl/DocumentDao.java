package com.wk.cms.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IDocumentDao;
import com.wk.cms.model.Document;
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
		
		if(StringUtils.hasLength(query)){
			System.err.println(query);
			try {
				System.err.println(URLDecoder.decode(query, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				System.err.println(new String(query.getBytes("UTF-8"),"GBK"));
				System.err.println(new String(query.getBytes("GBK"),"UTF-8"));
				System.err.println(new String(query.getBytes("ISO-8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.add(Restrictions.or(Restrictions.like("title", query,MatchMode.ANYWHERE),
					Restrictions.like("abst", query,MatchMode.ANYWHERE),
					Restrictions.like("content", query,MatchMode.ANYWHERE)));
		}
		Long count = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		c.setProjection(null);
		c.addOrder(Order.desc("crTime"));
		List<Document> documents = c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
				setFirstResult(pageInfo.getStart()).
				setMaxResults(pageInfo.getLimit()).list();
		
		return new PageInfo(documents,count);
	}
	@Override
	public void save(Document document) {
		
		if(!StringUtils.hasLength(document.getId())){
			document.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(document);
		}else{
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
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findByIds(String ids) {
		
		return (List<Document>) hibernateTemplate.find("select d from Document d where d.id in ("+ids+")" );
	}

}
