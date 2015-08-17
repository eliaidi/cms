package com.wk.cms.dao;

import com.wk.cms.model.ExtField;
import com.wk.cms.utils.PageInfo;

public interface IExtFieldDao {

	PageInfo find(String channelId,PageInfo pageInfo,String query);

	void save(ExtField extField);

	ExtField find(String name, String channelId);

	ExtField findByName(String name);

	ExtField findById(String id);

}
