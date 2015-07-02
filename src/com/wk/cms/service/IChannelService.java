package com.wk.cms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.Channel;
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
	Channel findById(String id) throws ServiceException;

	/**
	 * 根据父栏目ID查询所有子栏目
	 * @param parentId
	 * @return
	 * @throws ServiceException 
	 */
	List<Channel> findByParentId(String parentId) throws ServiceException;

	/**
	 * 删除栏目
	 * @param channelId
	 * @throws ServiceException 
	 */
	void deleteById(String channelId) throws ServiceException;

	/**
	 * 根据栏目名称查询栏目
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	Channel findByName(String name) throws ServiceException;

	void imp(MultipartFile file, String parentId, String siteId) throws ServiceException;

	void deleteMulti(String ids) throws ServiceException;
}
