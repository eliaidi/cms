package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Resource;
import com.wk.cms.model.Role;
import com.wk.cms.utils.PageInfo;

public interface IResourceDao {

	PageInfo find(PageInfo info, String query);

	Resource findById(String id);

	void save(Resource r);

	void delete(String[] ids);

	void delete(String id);

	List<Resource> find(String[] ids);

	List<Resource> findAllWithRoles();

	List<Role> findRoles(Resource r);

	List<String> findRoleNames(Resource r);

}
