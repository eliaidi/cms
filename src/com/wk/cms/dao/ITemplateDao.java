package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface ITemplateDao {

	PageInfo find(String siteId, PageInfo pageInfo,String query);

	void save(Template template) throws ServiceException;

	Template findById(String id);

	void delete(String id);

	TempFile findBySiteIdAndFileName(String siteId, String fileName);

	List<TempFile> findAllTempFileBySiteId(String id);

}
