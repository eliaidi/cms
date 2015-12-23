package com.wk.cms.service;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.User;

public interface IUserService {

	User findByUserName(@NotEmpty String username);

	Set<String> findRoles(@NotEmpty String username);

	Set<String> findPermissions(@NotEmpty String username);

	void save(@NotNull User user);

}
