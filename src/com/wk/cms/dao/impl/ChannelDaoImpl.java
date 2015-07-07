package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IChannelDao;
import com.wk.cms.model.Channel;
import com.wk.cms.utils.CommonUtils;

@Repository
public class ChannelDaoImpl implements IChannelDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public List<Channel> findBySiteId(String siteId) {
		
		return (List<Channel>) hibernateTemplate.find("select c from Channel c where c.site.id=? and c.parent is null", siteId);
	}
	@Override
	public Channel findById(String id) {
		return hibernateTemplate.get(Channel.class, id);
	}
	@Override
	public void save(Channel channel) {
		
		if(!StringUtils.hasLength(channel.getId())){
			channel.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(channel);
		}else{
			hibernateTemplate.update(channel);
		}
	}
	@Override
	public List<Channel> findByParentId(String parentId) {
		
		return (List<Channel>) hibernateTemplate.find("select c from Channel c where c.parent.id=?", parentId);
	}
	@Override
	public void deleteById(String channelId) {
		
		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();
		s.delete(findById(channelId));
//		hibernateTemplate.bulkUpdate("delete from Channel c where c.id=? ", channelId);
	}
	@Override
	public Channel findByName(String name) {
		
		List<Channel> channels = (List<Channel>) hibernateTemplate.find("select c from Channel c where c.name=?", name);
		
		if(CommonUtils.isEmpty(channels)){
			return null;
		}
		return channels.get(0);
	}

}
