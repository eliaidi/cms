package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.ExtField;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IExtFieldService {

	PageInfo find(@NotEmpty String channelId,@NotNull PageInfo pageInfo,String query);

	ExtField save(@NotNull ExtField extField) throws ServiceException;

	ExtField find(@NotNull String name, @NotNull String channelId);

	ExtField findByName(@NotNull String name);

	ExtField findById(@NotNull String id);

	void delete(String[] ids);

}
