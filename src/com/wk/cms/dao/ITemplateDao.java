package com.wk.cms.dao;

import com.wk.cms.model.Template;
import com.wk.cms.utils.PageInfo;

public interface ITemplateDao {

	PageInfo find(PageInfo pageInfo,String query);

	void save(Template template);

	Template findById(String id);

	void delete(String id);

}
