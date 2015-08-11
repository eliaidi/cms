package com.wk.cms.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.utils.PageInfo;

public interface IDocumentDao {

	PageInfo find(String channelId, PageInfo pageInfo, String query);

	void save(Document document);

	Document findById(String id);

	void deleteById(String id);

	List<Document> findAll( Channel channel,int pageSize,DetachedCriteria dc);

	void refresh(Channel channel);

	List<Document> findByIds(String[] objIds);

	List<Document> find(String hql, Object[] params);

	List<Document> find(@NotEmpty String hql, Object[] params, @NotNull PageInfo pageInfo);

	List<Document> findByMap(Channel currChnl, Map<String, String> params);

}
