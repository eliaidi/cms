package com.wk.cms.service.impl;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IUserDao;
import com.wk.cms.model.User;
import com.wk.cms.service.IUserService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;
import com.wk.cms.utils.PasswordHash;

@Service("UserService")
public class UserService implements IUserService {

	private static final String USER_DEFAULT_PASSWORD = "111111";
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
	public User save(User user) throws ServiceException {
		
		if(!StringUtils.hasLength(user.getId())){
			User u = findByUserName(user.getUsername());
			if(u!=null){
				throw new ServiceException("username["+user.getUsername()+"] exists!");
			}
			if(!StringUtils.hasLength(user.getPassword())){
				user.setPassword(USER_DEFAULT_PASSWORD);
			}
			
			user.setCrTime(new Date());
			PasswordHash.hash(user);
			userDao.save(user);
			return user;
		}else{
			User u = findById(user.getId());
			if(u==null){
				throw new ServiceException("user[id="+user.getId()+"] exists!");
			}
			
			u.setTruename(user.getTruename());
			u.setEmail(user.getEmail());
			
			userDao.save(u);
			return u;
		}
		
	}

	@Override
	public User findById(String id) {
		return userDao.findById(id);
	}

	@Override
	public PageInfo list(PageInfo info, String query) {
		return userDao.find(info,query);
	}

	@Override
	public void delete(String id) {
		userDao.delete(id);
	}

	@Override
	public void delete(String[] id) {
		userDao.delete(id);
	}

}
