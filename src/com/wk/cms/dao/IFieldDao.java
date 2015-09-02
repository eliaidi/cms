package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Field;
import com.wk.cms.utils.PageInfo;

public interface IFieldDao {

	PageInfo find(String siteId, PageInfo pageInfo, String query);

	void save(Field field);

	Field findById(String id);

	void delete(Field f);

	List<Field> find(String[] ids);

}
