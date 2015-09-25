package com.wk.cms.dao;

import com.wk.cms.model.PubLog;
import com.wk.cms.utils.PageInfo;

public interface IPubLogDao {

	PubLog save(PubLog log);

	PageInfo find(String type, PageInfo pageInfo);

}
