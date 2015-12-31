package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Role;
import com.wk.cms.model.User;
import com.wk.cms.utils.PageInfo;

public interface IUserDao {

	User findByUserName(String username);

	void save(User user);

	PageInfo find(PageInfo info, String query);

	User findById(String id);

	void delete(String id);

	void delete(String[] id);

	void assign(User user, List<Role> roles);

	void assign(String userId, String[] roleIds);

}
