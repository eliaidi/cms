package com.wk.cms.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.utils.PageInfo;

public interface IDocumentDao {

	PageInfo find(String channelId, PageInfo pageInfo, String query);

	void save(Document document);

	Document findById(String id);

	void deleteById(String id);

	List<Document> findByIds(String ids);

	List<Document> findAll( Channel channel);

	void refresh(Channel channel);

}
