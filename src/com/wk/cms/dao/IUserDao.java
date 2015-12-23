package com.wk.cms.dao;

import com.wk.cms.model.User;

public interface IUserDao {

	User findByUserName(String username);

	void save(User user);

}
