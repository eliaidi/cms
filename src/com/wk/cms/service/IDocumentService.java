package com.wk.cms.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IDocumentService {

	PageInfo find(@NotEmpty String channelId,@NotNull PageInfo pageInfo, String query) throws ServiceException;

	void save(Document document,@NotEmpty String channelId) throws ServiceException;

	Document findById(String id) throws ServiceException;

	void deleteById(String id) throws ServiceException;

	void deleteByIds(String ids) throws ServiceException;

	List<Document> findByIds(String ids) throws ServiceException;

	Document loadRemoteDoc(String url) throws  ParseException, ServiceException;

}
