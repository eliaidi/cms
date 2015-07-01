package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IChannelDao;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Site;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.exception.ServiceException;

@Service
public class ChannelServiceImpl implements IChannelService {

	@Autowired
	private IChannelDao channelDao;
	@Autowired 
	private ISiteService siteService;
	
	@Override
	public List<Channel> findBySiteId(String siteId) throws ServiceException {
		
		if(!StringUtils.hasLength(siteId)){
			throw new ServiceException("参数错误！");
		}
		return channelDao.findBySiteId(siteId);
	}
	@Override
	public void save(Channel channel, String parentId, String siteId) throws ServiceException {
		
		if(!StringUtils.hasLength(channel.getId())){
			
			if(findByName(channel.getName())!=null){
				throw new ServiceException("名称是【"+channel.getName()+"】的栏目已经存在！");
			}
			
			if(!StringUtils.hasLength(parentId)&&!StringUtils.hasLength(siteId)){
				throw new ServiceException("参数错误！parentId和siteId必须至少传入一个");
			}
			if(StringUtils.hasLength(parentId)){
				Channel parent = findById(parentId);
				if(parent==null){
					throw new ServiceException("参数错误！parentId为【"+parentId+"】的栏目不存在");
				}
				channel.setParent(parent);
				channel.setSite(parent.getSite());
			}else{
				Site site = siteService.findById(siteId);
				if(site==null){
					throw new ServiceException("参数错误！siteId为【"+siteId+"】的站点不存在");
				}
				channel.setParent(null);
				channel.setSite(site);
			}
			channel.setCrTime(new Date());
			channel.setCrUser(null);
			
			channelDao.save(channel);
		}else{
			
			Channel sameNameChnl = findByName(channel.getName());
			if(sameNameChnl!=null&&!channel.getId().equals(sameNameChnl.getId())){
				throw new ServiceException("name为【"+channel.getName()+"】的栏目已经存在！");
			}
			
			Channel persistChannel = findById(channel.getId());
			if(persistChannel==null){
				throw new ServiceException("参数错误！id为【"+channel.getId()+"】的栏目不存在");
			}
			BeanUtils.copyProperties(channel, persistChannel, new String[]{"id","parent","site","crUser","crTime"});
			
			channelDao.save(persistChannel);
		}
		
	}
	
	@Override
	public Channel findByName(String name) throws ServiceException {
		
		if(!StringUtils.hasLength(name)){
			throw new ServiceException("name必须传入");
		}
		return channelDao.findByName(name);
	}
	@Override
	public Channel findById(String id) throws ServiceException {
		
		if(!StringUtils.hasLength(id)){
			throw new ServiceException("参数错误！id必须传入");
		}
		return channelDao.findById(id);
	}
	@Override
	public List<Channel> findByParentId(String parentId) throws ServiceException {
		
		if(!StringUtils.hasLength(parentId)){
			throw new ServiceException("参数错误！parentId必须传入");
		}
		return channelDao.findByParentId(parentId);
	}
	@Override
	public void deleteById(String channelId) throws ServiceException {
		
		if(!StringUtils.hasLength(channelId)){
			throw new ServiceException("参数错误！channelId必须传入");
		}
		
		channelDao.deleteById(channelId);
	}

}
