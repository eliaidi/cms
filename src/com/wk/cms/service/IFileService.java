package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.File;
import com.wk.cms.service.exception.ServiceException;

public interface IFileService {

	File findById(@NotNull String id);

	void save(@NotNull File f) throws ServiceException;

	File copy(@NotNull File file) throws ServiceException;

	java.io.File zipFilesByIds(@NotEmpty String[] ids) throws ServiceException;

	java.io.File save2Local(@NotEmpty List<File> files) throws ServiceException;

	File upload(@NotEmpty MultipartFile file, @NotEmpty String type) throws ServiceException;

}
