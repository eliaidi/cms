package com.wk.cms.dao;

import com.wk.cms.model.Role;
import com.wk.cms.utils.PageInfo;

public interface IRoleDao {

	PageInfo find(PageInfo info, String query);

	void save(Role role);

	Role findById(String id);

	void delete(String[] ids);

	void delete(String id);

}
