package com.wk.cms.service.impl;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IUserDao;
import com.wk.cms.model.User;
import com.wk.cms.service.IUserService;
import com.wk.cms.utils.PasswordHash;

@Service("UserService")
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public Set<String> findRoles(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> findPermissions(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(User user) {
		
		if(!StringUtils.hasLength(user.getId())){
			user.setCrTime(new Date());
			PasswordHash.hash(user);
		}
		
		userDao.save(user);
	}

}
