package com.wk.cms.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

}
