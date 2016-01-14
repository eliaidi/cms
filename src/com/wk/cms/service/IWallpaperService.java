package com.wk.cms.service;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.WallPaper;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IWallpaperService {

	WallPaper save(@NotEmpty MultipartFile file) throws ServiceException;

	PageInfo findByUser();

	void deleteById(@NotEmpty String id);

	void assign(@NotEmpty String id);

	WallPaper findById(@NotEmpty String id);

	WallPaper findCurrent();

}
