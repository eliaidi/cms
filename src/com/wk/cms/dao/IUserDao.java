package com.wk.cms.dao;

import com.wk.cms.model.User;
import com.wk.cms.utils.PageInfo;

public interface IUserDao {

	User findByUserName(String username);

	void save(User user);

	PageInfo find(PageInfo info, String query);

	User findById(String id);

	void delete(String id);

	void delete(String[] id);

}
