package com.wk.cms.dao;

import com.wk.cms.utils.PageInfo;

public interface IExtFieldDao {

	PageInfo find(String channelId,PageInfo pageInfo,String query);

}
