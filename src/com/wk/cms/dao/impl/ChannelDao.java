package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IChannelDao;
import com.wk.cms.model.Channel;
import com.wk.cms.utils.CommonUtils;

@Repository
public class ChannelDao implements IChannelDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> findBySiteId(String siteId) {

		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Channel.class).createAlias("site", "s")
				.add(Restrictions.eq("s.id", siteId))
				.add(Restrictions.isNull("parent"))
				.addOrder(Order.desc("crTime")).list();
	}

	@Override
	public Channel findById(String id) {
		return hibernateTemplate.get(Channel.class, id);
	}

	@Override
	public void save(Channel channel) {

		if (!StringUtils.hasLength(channel.getId())) {
			channel.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(channel);
		} else {
			hibernateTemplate.update(channel);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> findByParentId(String parentId) {

		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Channel.class)
				.add(Restrictions.eq("parent.id", parentId))
				.addOrder(Order.desc("crTime")).list();
	}

	@Override
	public void deleteById(String channelId) {

		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();
		s.delete(findById(channelId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Channel findByName(String name) {

		List<Channel> channels = hibernateTemplate.getSessionFactory()
				.getCurrentSession().createCriteria(Channel.class)
				.add(Restrictions.eq("name", name)).list();
		if (CommonUtils.isEmpty(channels)) {
			return null;
		}
		return channels.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> findByIds(String[] objIds) {

		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Channel.class)
				.add(Restrictions.in("id", objIds)).list();
	}

}
