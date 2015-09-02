package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.Field;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IFieldService {

	PageInfo find(@NotEmpty String siteId, @NotNull PageInfo pageInfo, String query);

	List<Field> findTypes(String siteId) throws ServiceException;

	Field save(@NotNull Field field);

	Field findById(@NotEmpty String id);

	void delete(@NotNull List<Field> children);

	void delete(@NotEmpty String[] ids);

	List<Field> find(@NotEmpty String[] ids);

}
