package com.wk.cms.service;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.Appendix;
import com.wk.cms.model.Document;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IAppendixService {

	PageInfo list(String documentId, Integer type, PageInfo pageInfo) throws ServiceException;

	void save(MultipartFile file, Appendix appendix) throws ServiceException, IOException;

	Appendix findById(String id) throws ServiceException;

	void attachTo(String appIds, String id) throws ServiceException;

	void attachTo(Appendix appendix, Document document);

	List<Appendix> findByDocId(String documentId) throws ServiceException;

	void delete(String id) throws ServiceException;

	/**
	 * 将文档里面的所有附件拷贝一份，放置到另一个文档里面
	 * @param document	from
	 * @param newDoc	to
	 * @throws ServiceException
	 */
	void copy(@NotNull Document document, @NotNull Document newDoc) throws ServiceException;

	void copy(@NotNull Appendix appendix,@NotNull Document newDoc) throws ServiceException;

}
