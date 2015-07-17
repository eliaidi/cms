package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.Template;
import com.wk.cms.utils.PageInfo;

public interface ITemplateService {

	PageInfo find(@NotNull PageInfo pageInfo,String query);

	void save(@NotNull Template template);

	Template findById(@NotEmpty String id);

	void delete(@NotEmpty String[] ids);

	void delete(@NotEmpty String id);

}
