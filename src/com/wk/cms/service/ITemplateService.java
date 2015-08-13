package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface ITemplateService {

	String TEMPFILE_FOLDER = "images";
	String PREVIEW_FOLDER = "preview";

	PageInfo find(@NotEmpty String siteId, String show,
			@NotNull PageInfo pageInfo, String query);

	void save(@NotNull Template template) throws ServiceException;

	Template findById(@NotEmpty String id);

	void delete(@NotEmpty String[] ids);

	void delete(@NotEmpty String id);

	Template loadRemoteDoc(@NotEmpty String url, @NotEmpty String siteId)
			throws FileParseException;

	List<TempFile> findTempFilesByFileNames(
			@NotEmpty List<String> tempFileNames, @NotNull Site site);

	List<TempFile> findTempFilesBySite(@NotNull Site site);

	void imp(@NotNull MultipartFile f, @NotEmpty String siteId, String encode)
			throws ServiceException;

	Template findByName(@NotEmpty String tName);

	PageInfo findFiles(@NotEmpty String siteId, @NotNull PageInfo pageInfo,
			String query);

	TempFile uploadFile(@NotNull MultipartFile f, @NotEmpty String siteId,
			String id, @NotEmpty String encode) throws ServiceException;

	TempFile saveFile(@NotNull TempFile tempFile);

	void deleteFiles(@NotEmpty String[] ids);

}
