package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IUserDao;
import com.wk.cms.model.User;
import com.wk.cms.utils.CommonUtils;

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

}
