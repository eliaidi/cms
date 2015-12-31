package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IRoleDao;
import com.wk.cms.model.Resource;
import com.wk.cms.model.Role;
import com.wk.cms.utils.PageInfo;

@Repository
public class RoleDao implements IRoleDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public PageInfo find(PageInfo info, String query,String userId) {
		
		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Role.class);
		if(StringUtils.hasLength(query)){
			c.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
		}
		
		if(StringUtils.hasLength(userId)){
			c.createAlias("users", "us")
			.add(Restrictions.eq("us.id", userId));
//			c.createCriteria("users")
//			.add(Restrictions.eq("id", userId));
			//c.add(Restrictions.eq("users.id", userId));
		}
		
		long count = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		List<Role> roles = c.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		//c.setFe tchMode("users", FetchMode.JOIN);
		info.setList(roles);
		info.setTotalCount(count);
		return info;
	}
	@Override
	public void save(Role role) {
		
		if(StringUtils.hasLength(role.getId())){
			hibernateTemplate.merge(role);
		}else{
			role.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(role);
		}
	}
	@Override
	public Role findById(String id) {
		return hibernateTemplate.get(Role.class, id);
	}
	@Override
	public void delete(String[] ids) {
		for(String id:ids){
			delete(id);
		}
	}
	@Override
	public void delete(String id) {
		Role r = findById(id);
		
		List<Resource> resources = r.getResources();
		for(Resource rs : resources){
			rs.getRoles().remove(r);
		}
		r.setResources(null);
		hibernateTemplate.delete(r);
	}
	@Override
	public List<Role> find(String[] roleIds) {
		
		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(Role.class);
		
		c.add(Restrictions.in("id", roleIds));
		return c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	@Override
	public List<String> findNameByUserName(String username) {
		return (List<String>) hibernateTemplate.find("select r.name from Role r join r.users us where us.username=?", username);
	}
	
}
