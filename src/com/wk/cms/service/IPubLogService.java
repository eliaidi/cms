package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.PubLog;
import com.wk.cms.utils.PageInfo;

public interface IPubLogService {

	PubLog save(@NotNull PubLog log);

	PageInfo find(@NotEmpty String type, @NotNull PageInfo pageInfo);

	PubLog noTransSave(@NotNull PubLog log);


}
