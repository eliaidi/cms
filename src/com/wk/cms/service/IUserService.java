package com.wk.cms.service;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.User;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IUserService {

	User findByUserName(@NotEmpty String username);

	Set<String> findRoles(@NotEmpty String username);

	Set<String> findPermissions(@NotEmpty String username);

	User save(@NotNull User user) throws ServiceException;

	PageInfo list(@NotNull PageInfo info, String query);

	User findById(@NotEmpty String id);

	void delete(@NotEmpty String id);

	void delete(@NotEmpty String[] id);

	void assign(@NotEmpty String userId, @NotEmpty String[] roleIds) throws ServiceException;

}
