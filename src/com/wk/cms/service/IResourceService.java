package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.Resource;
import com.wk.cms.model.Role;
import com.wk.cms.utils.PageInfo;

public interface IResourceService {

	PageInfo list(@NotNull PageInfo info, String query);

	Resource save(@NotNull Resource resource);

	Resource findById(@NotEmpty String id);

	void delete(@NotEmpty String[] ids);

	List<Resource> findByIds(@NotEmpty String[] ids);

	List<Resource> findAllWithRoles();

	List<Role> findRoles(@NotNull Resource r);

	List<String> findRoleNames(@NotNull Resource r);

	void initFilterChain();

}
