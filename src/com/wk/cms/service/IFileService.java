package com.wk.cms.service;

import javax.validation.constraints.NotNull;

import com.wk.cms.model.File;
import com.wk.cms.service.exception.ServiceException;

public interface IFileService {

	File findById(@NotNull String id);

	void save(@NotNull File f) throws ServiceException;

	File copy(@NotNull File file) throws ServiceException;

}
