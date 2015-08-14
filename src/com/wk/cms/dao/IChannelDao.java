package com.wk.cms.dao;

import java.util.List;
import java.util.Map;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;

public interface IChannelDao  {

	List<Channel> findBySiteId(String siteId);

	Channel findById(String id);

	void save(Channel channel);

	List<Channel> findByParentId(String parentId);

	void deleteById(String channelId);

	List<Channel> findByIds(String[] objIds);

	Channel findByName(String name, Site currSite);

	List<Channel> findByMap(Channel pChannel, Map<String, String> params);

	List<Channel> findByMap(Site obj, Map<String, String> params);

	void move(String currId, String targetId);

	void move2Site(Channel currChnl, Site site);

	Integer findMaxSortOf(Channel parent, Site site);

	List<Template> findTemps(Channel channel, Integer type);
}
