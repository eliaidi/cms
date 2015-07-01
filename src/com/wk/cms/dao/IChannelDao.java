package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Channel;

public interface IChannelDao  {

	List<Channel> findBySiteId(String siteId);

	Channel findById(String id);

	void save(Channel channel);

	List<Channel> findByParentId(String parentId);

	void deleteById(String channelId);

	Channel findByName(String name);
}
