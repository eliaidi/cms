package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface ITemplateDao {

	PageInfo find(String siteId, String show, PageInfo pageInfo,String query);

	void save(Template template) throws ServiceException;

	Template findById(String id);

	void delete(String id);

	TempFile findBySiteIdAndFileName(String siteId, String fileName);

	List<TempFile> findAllTempFileBySiteId(String id);

	List<TempFile> findTempFiles(List<String> tempFileNames, Site site);

	List<TempFile> findTempFiles(Site site);

	void search2Add(java.io.File tempfolder, Site site,String encode) throws ServiceException;

	Template findByName(String tName);

	PageInfo findFiles(String siteId, PageInfo pageInfo, String query);

	TempFile findFileByFId(String id);

	void saveFile(TempFile tf);

	void deleteFiles(String[] ids);

	void deleteFile(String id);

}
