package com.wk.cms.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

public interface IDocumentService {

	int MAX_FETCH_SIZE = 500;

	PageInfo find(@NotEmpty String channelId,@NotNull PageInfo pageInfo, String query) throws ServiceException;

	void save(@NotNull Document document, String channelId) throws ServiceException;

	Document findById(@NotEmpty String id) throws ServiceException;

	void deleteById(@NotEmpty String id) throws ServiceException;

	void deleteByIds(@NotEmpty String ids) throws ServiceException;

	List<Document> findByIds(@NotEmpty String ids) throws ServiceException;

	Document loadRemoteDoc(@NotEmpty String url) throws  ParseException, ServiceException;

	void copy(@NotNull Channel channel, @NotNull Channel newChannel) throws ServiceException;

	List<Document> findAll(@NotNull Channel channel);

	/***
	 * 
	 * @param document 
	 * @param newChannel
	 * @throws ServiceException 
	 */
	void copy(@NotNull Document document,@NotNull Channel newChannel) throws ServiceException;

	void save(@NotNull Document newDoc,@NotNull Channel newChannel);

	/**
	 * 更新栏目下所有文档的所属站点，保持和栏目的所属站点一致
	 * @param channel
	 */
	void refreshBy(@NotNull Channel channel);

	void copy(@NotEmpty String[] objIds, @NotEmpty String channelId) throws ServiceException;

	void cut(@NotEmpty String[] objIds,@NotEmpty String channelId) throws ServiceException;

	List<Document> findByIds(@NotEmpty String[] objIds);

	void cut(@NotNull Document document, @NotNull Channel channel);

	List<Document> findCanPub(@NotNull Channel currChnl, int pageSize, String where, String order,Object[] params);

	PageInfo findByMap(@NotNull Channel currChnl, Map<String, String> params);

	void move(@NotEmpty String currId, @NotEmpty String targetId) throws ServiceException;

	void removeFieldsFrom(@NotNull Document persistDoc);

	Object getDocProperty(@NotNull Document currDoc, @NotEmpty String field,  Integer index) throws ServiceException;

	PageInfo findRollDocuments(@NotNull Site site, @NotNull Map<String, String> params) throws ServiceException;

	String preview(@NotEmpty String id) throws ServiceException;

}
