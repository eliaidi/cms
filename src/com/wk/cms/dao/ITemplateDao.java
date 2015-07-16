package com.wk.cms.dao;

import com.wk.cms.utils.PageInfo;

public interface ITemplateDao {

	PageInfo find(PageInfo pageInfo,String query);

}
