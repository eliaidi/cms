package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;

public interface ISiteService {

	List<Site> findAll();

	void save(@NotNull Site site) throws ServiceException;

	Site findByName(String name);

	/**
	 * 
	 * @param siteId 
	 * @return
	 * @throws ServiceException 未找到站点
	 */
	Site findById(@NotEmpty String siteId) throws ServiceException ;

	void deleteById(String siteId) throws ServiceException;

	void imp(@NotEmpty MultipartFile file, @NotEmpty String encode) throws ServiceException;

	String previewById(@NotEmpty String siteId) throws ServiceException;

	List<Template> findTemplatesBySite(@NotNull Site obj);

	void publish(@NotEmpty String id, @NotEmpty String type) throws ServiceException;
}
