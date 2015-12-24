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

import com.wk.cms.dao.IUserDao;
import com.wk.cms.model.User;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Repository
public class UserDao implements IUserDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public User findByUserName(String username) {
		
		List<User> users = (List<User>) hibernateTemplate.find("select u from User u where u.username=?", username);
		return CommonUtils.isEmpty(users)?null:users.get(0);
	}
	@Override
	public void save(User user) {
		
		if(!StringUtils.hasLength(user.getId())){
			user.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(user);
		}else{
			hibernateTemplate.update(user);
		}
	}
	@Override
	public PageInfo find(PageInfo info, String query) {
		
		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(User.class);
		if(StringUtils.hasLength(query)){
			c.add(Restrictions.or(Restrictions.like("username", query, MatchMode.ANYWHERE),Restrictions.like("truename", query, MatchMode.ANYWHERE)));
		}
		
		long count = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		List<User> users = c.setProjection(null).setFirstResult(info.getStart())
				.setMaxResults(info.getLimit())
				.addOrder(Order.desc("crTime"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		
		info.setList(users);
		info.setTotalCount(count);
		return info;
	}
	@Override
	public User findById(String id) {
		return hibernateTemplate.get(User.class, id);
	}
	@Override
	public void delete(String id) {
		hibernateTemplate.bulkUpdate("delete from User u where u.id=?", id);
	}
	@Override
	public void delete(String[] ids) {
		
		for(String id : ids){
			delete(id);
		}
	}

}
