package com.wk.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IChannelDao;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.model.Template.Type;
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
				.add(Restrictions.isNull("parent")).addOrder(Order.asc("sort"))
				.list();
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
				.addOrder(Order.asc("sort")).list();
	}

	@Override
	public void deleteById(String channelId) {

		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();
		s.delete(findById(channelId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> findByIds(String[] objIds) {

		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Channel.class)
				.add(Restrictions.in("id", objIds)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Channel findByName(String name, Site currSite) {

		List<Channel> channels = (List<Channel>) hibernateTemplate.find(
				"from Channel where name=? and site=?", name, currSite);
		if (CommonUtils.isEmpty(channels))
			return null;
		return channels.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Channel> findByMap(Channel pChannel, Map<String, String> params) {

		if (params == null)
			return findByParentId(pChannel.getId());

		String num = params.get("num");
		String where = params.get("where");
		String order = params.get("order");
		String startpos = params.get("startpos");

		if (!StringUtils.hasLength(num))
			num = "500";
		if (!StringUtils.hasLength(startpos))
			startpos = "0";

		StringBuilder hql = new StringBuilder("from Channel where parent=? ");
		if (StringUtils.hasLength(where)) {
			hql.append(" and " + where);
		}
		if (StringUtils.hasLength(order)) {
			hql.append(" order by " + order);
		}

		Query q = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(hql.toString());
		q.setParameter(0, pChannel);
		q.setFirstResult(Integer.parseInt(startpos));
		q.setMaxResults(Integer.parseInt(num));
		return q.list();
	}

	@Override
	public List<Channel> findByMap(Site obj, Map<String, String> params) {
		if (params == null)
			return findBySiteId(obj.getId());

		String num = params.get("num");
		String where = params.get("where");
		String order = params.get("order");
		String startpos = params.get("startpos");

		if (!StringUtils.hasLength(num))
			num = "500";
		if (!StringUtils.hasLength(startpos))
			startpos = "0";

		StringBuilder hql = new StringBuilder("from Channel where site=? ");
		if (StringUtils.hasLength(where)) {
			hql.append(" and " + where);
		}
		if (StringUtils.hasLength(order)) {
			hql.append(" order by " + order);
		}

		Query q = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery(hql.toString());
		q.setParameter(0, obj);
		q.setFirstResult(Integer.parseInt(startpos));
		q.setMaxResults(Integer.parseInt(num));
		return q.list();
	}

	@Override
	public void move(String currId, String targetId) {

		Channel currChnl = findById(currId);
		Channel targetChnl = findById(targetId);

		if (!currChnl.getSite().equals(targetChnl.getSite())) {
			// set channel's templates to null
			currChnl.setOtempIds(null);
			currChnl.setDtempIds(null);
			// modify channel's site, children and documents
			move2Site(currChnl, targetChnl.getSite());

		}
		currChnl.setParent(targetChnl.getParent());

		hibernateTemplate
				.bulkUpdate(
						"update Channel set sort = sort+1 where site=? and sort>=? and "
								+ (targetChnl.getParent() == null ? " parent is null"
										: "parent.id='"
												+ targetChnl.getParent()
														.getId() + "'"),
						targetChnl.getSite(), targetChnl.getSort());
		currChnl.setSort(targetChnl.getSort());
		hibernateTemplate.update(currChnl);
	}

	@Override
	public void move2Site(Channel currChnl, Site site) {

		currChnl.setSite(site);
		hibernateTemplate.bulkUpdate(
				"update Document set site=? where channel=?", site, currChnl);

		List<Channel> children = findByParentId(currChnl.getId());
		for (Channel c : children) {
			move2Site(c, site);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer findMaxSortOf(Channel parent, Site site) {

		List<Object> vals = (List<Object>) hibernateTemplate.find(
				"select max(sort) from Channel where site=? and "
						+ (parent == null ? "parent is null " : "parent.id='"
								+ parent.getId() + "'"), site);
		if(CommonUtils.isEmpty(vals)){
			return 0;
		}
		
		return vals.get(0)==null?0:Integer.parseInt(vals.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Template> findTemps(Channel channel, Integer type) {
		
		List<Template> templates = null;
		List<String> tidArr = new ArrayList<String>();
		if(type==null||type.equals(Type.DETAIL)){
			String tids = channel.getDtempIds();
			if(StringUtils.hasLength(tids)){
				CommonUtils.push(tidArr,tids.split(","));
			}
		}
		if(type==null||type.equals(Type.OUTLINE)){
			String tids = channel.getOtempIds();
			if(StringUtils.hasLength(tids)){
				CommonUtils.push(tidArr,tids.split(","));
			}
		}
		templates = hibernateTemplate.getSessionFactory()
				.getCurrentSession()
				.createCriteria(Template.class)
				.add(Restrictions.in("id", tidArr))
				.list();
		
		return templates;
	}

}
