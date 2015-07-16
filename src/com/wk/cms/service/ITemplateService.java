package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import com.wk.cms.utils.PageInfo;

public interface ITemplateService {

	PageInfo find(@NotNull PageInfo pageInfo,String query);

}
