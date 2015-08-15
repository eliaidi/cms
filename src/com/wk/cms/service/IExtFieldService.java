package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.utils.PageInfo;

public interface IExtFieldService {

	PageInfo find(@NotEmpty String channelId,@NotNull PageInfo pageInfo,String query);

}
