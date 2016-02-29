package com.wk.cms.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;

public interface IChannelService {

	/**
	 * 查询站点下所有的栏目信息
	 * @param siteId 站点ID
	 * @return
	 * @throws ServiceException 如果siteId未传入
	 */
	List<Channel> findBySiteId(String siteId) throws ServiceException;

	/**
	 * 保存栏目，包括新增和修改
	 * @param channel 
	 * @param parentId
	 * @param siteId
	 * @throws ServiceException
	 */
	void save(Channel channel, String parentId, String siteId) throws ServiceException;

	/**
	 * 根据栏目ID查询栏目
	 * @param id 栏目ID
	 * @return
	 * @throws ServiceException 
	 */
	Channel findById(@NotEmpty String id) ;

	/**
	 * 根据父栏目ID查询所有子栏目
	 * @param parentId
	 * @return
	 * @throws ServiceException 
	 */
	List<Channel> findByParentId(@NotEmpty String parentId) throws ServiceException;

	/**
	 * 删除栏目
	 * @param channelId
	 * @throws ServiceException 
	 */
	void deleteById(String channelId) throws ServiceException;


	void imp(MultipartFile file, String parentId, String siteId, @NotEmpty String encode) throws ServiceException;

	void deleteMulti(String ids) throws ServiceException;

	void copy(@NotNull String[] objIds, String parentId, String siteId) throws ServiceException;

	void cut(@NotNull String[] objIds, String parentId, String siteId) throws ServiceException;

	List<Channel> findByIdArray(@NotNull String[] objIds);

	void copy(@NotNull Channel channel, String parentId, String siteId) throws ServiceException;

	void cut(@NotNull Channel channel, String parentId, String siteId) throws ServiceException;

	Channel findByName(@NotEmpty String name, @NotNull Site currSite);

	List<Channel> findByMap(@NotNull Channel pChannel, @NotNull Map<String, String> params);

	List<Channel> findByMap(@NotNull Site obj, @NotNull Map<String, String> params);

	void move(@NotEmpty String currId, @NotEmpty String targetId);

	List<Template> findTemps(@NotNull Channel channel, Integer type);

	List<Channel> findSubChannels(@NotNull Site site, @NotEmpty String pName, String level) throws ServiceException;

	String preview(@NotEmpty String id) throws ServiceException;

	String publish(@NotEmpty String id, int type) throws ServiceException;
}
