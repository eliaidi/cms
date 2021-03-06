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

import com.wk.cms.dao.IExtFieldDao;
import com.wk.cms.model.ExtField;
import com.wk.cms.model.Field;
import com.wk.cms.utils.CommonUtils;
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
				.add(Restrictions.eq("channel.id", channelId)).addOrder(Order.asc("crTime"));
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

	@Override
	public void save(ExtField extField) {
		
		if(StringUtils.hasLength(extField.getId())){
			hibernateTemplate.update(extField);
		}else{
			extField.setId(UUID.randomUUID().toString());
			extField.getField().setId(UUID.randomUUID().toString());
			hibernateTemplate.save(extField);
		}
//		hibernateTemplate.saveOrUpdate(extField);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExtField find(String name, String channelId) {
		List<ExtField> extFields = (List<ExtField>) hibernateTemplate.find(
				"from ExtField where name=? and channel.id=?", name, channelId);
		if (CommonUtils.isEmpty(extFields)) {
			return null;
		}
		return extFields.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExtField findByName(String name) {
		List<ExtField> extFields = (List<ExtField>) hibernateTemplate.find(
				"from ExtField where name=? ", name);
		if (CommonUtils.isEmpty(extFields)) {
			return null;
		}
		return extFields.get(0);
	}

	@Override
	public ExtField findById(String id) {
		return hibernateTemplate.get(ExtField.class, id);
	}


	@Override
	public void delete(ExtField ef) {
		ef.getChannel().getExtFields().remove(ef);
		ef.setChannel(null);
		hibernateTemplate.delete(ef);
	}

	@Override
	public void delete(String id) {
		delete(findById(id));
		
	}
}
