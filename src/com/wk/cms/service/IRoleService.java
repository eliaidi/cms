package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.Role;
import com.wk.cms.utils.PageInfo;

public interface IRoleService {

	PageInfo list(@NotNull PageInfo info, String query);

	Role save(@NotNull Role role);

	Role findById(@NotEmpty String id);

	void delete(@NotEmpty String[] ids);

}
