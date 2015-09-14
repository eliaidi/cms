package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.dao.IChannelDao;
import com.wk.cms.model.Channel;
import com.wk.cms.model.ExtField;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;

@Service
public class ChannelService implements IChannelService {

	@Autowired
	private IChannelDao channelDao;
	@Autowired 
	private ISiteService siteService;
	@Autowired
	private IDocumentService documentService;
	
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
			
			if(StringUtils.hasLength(parentId)){
				Channel parent = findById(parentId);
				if(parent==null){
					throw new ServiceException("参数错误！parentId为【"+parentId+"】的栏目不存在");
				}
				channel.setParent(parent);
				channel.setSite(parent.getSite());
			}else if(StringUtils.hasLength(siteId)){
				Site site = siteService.findById(siteId);
				if(site==null){
					throw new ServiceException("参数错误！siteId为【"+siteId+"】的站点不存在");
				}
				channel.setParent(null);
				channel.setSite(site);
			}else{
				throw new ServiceException("参数错误！parentId和siteId必须至少传入一个");
			}
			channel.setCrTime(new Date());
			channel.setCrUser(null);
			channel.setSort(channelDao.findMaxSortOf(channel.getParent(),channel.getSite())+1);
			
			channelDao.save(channel);
		}else{
			
			Channel sameNameChnl = findByName(channel.getName(),channel.getSite());
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
	public Channel findById(String id) {
		
		return channelDao.findById(id);
	}
	@Override
	public List<Channel> findByParentId(String parentId) throws ServiceException {
		
		return channelDao.findByParentId(parentId);
	}
	@Override
	public void deleteById(String channelId) throws ServiceException {
		
		if(!StringUtils.hasLength(channelId)){
			throw new ServiceException("参数错误！channelId必须传入");
		}
		
		channelDao.deleteById(channelId);
	}
	@Override
	public void imp(MultipartFile file, String parentId, String siteId,String encode) throws ServiceException {
		
		try {
			String[] chnlNames = FileUtils.parseTxt2Arr(file,encode);
			
			for(String chnlName : chnlNames){
				if(StringUtils.hasLength(chnlName)){
					save(new Channel(null, chnlName, chnlName,CommonUtils.getFirstWordOf(chnlName),null, null, null, null, null, null, null), parentId, siteId);
				}
			}
		} catch (FileParseException e) {
			throw new ServiceException("解析文件失败！", e);
		}
			
	}
	@Override
	public void deleteMulti(String ids) throws ServiceException {
		
		if(!StringUtils.hasLength(ids)){
			throw new ServiceException("参数错误！ids必须传入！");
		}
		
		String[] idArr = ids.split(",");
		for(String id : idArr){
			deleteById(id);
		}
	}
	@Override
	public void copy(String[] objIds, String parentId, String siteId) throws ServiceException {

		List<Channel> channels = findByIdArray(objIds);
		if(CommonUtils.isEmpty(channels)){
			throw new ServiceException("未找到栏目对象！！");
		}
		for(Channel channel :channels){
			copy(channel,parentId,siteId);
		}
		
	}
	
	@Override
	public void copy(Channel channel, String parentId, String siteId) throws ServiceException {
		//复制本栏目
		Channel newChannel = new Channel();
		BeanUtils.copyProperties(channel, newChannel, new String[]{"id","crTime","crUser","children","documents"});
		
		for(ExtField ef : newChannel.getExtFields()){
			ef.getField().setId(UUID.randomUUID().toString());
			ef.setId(UUID.randomUUID().toString());
		}
		save(newChannel, parentId, siteId);
		//复制栏目下的文档
		documentService.copy(channel,newChannel);
		
		//复制所有子栏目
		List<Channel> channels = findByParentId(channel.getId());
		if(!CommonUtils.isEmpty(channels)){
			for(Channel subChannel : channels){
				copy(subChannel, newChannel.getId(), newChannel.getSite().getId());
			}
		}
		
	}
	@Override
	public List<Channel> findByIdArray(String[] objIds) {
		return channelDao.findByIds(objIds);
	}
	@Override
	public void cut(String[] objIds, String parentId, String siteId) throws ServiceException {
		
		List<Channel> channels = findByIdArray(objIds);
		if(!CommonUtils.isEmpty(channels)){
			for(Channel channel : channels){
				cut(channel,parentId,siteId);
			}
		}
	}
	@Override
	public void cut(Channel channel, String parentId, String siteId) throws ServiceException {
		
		Site targetSite = null;
		Channel parent = null;
		if(StringUtils.hasLength(parentId)){
			parent = findById(parentId);
			channel.setParent(parent);
			channel.setSite(parent.getSite());
			
			targetSite = parent.getSite();
		}else if(StringUtils.hasLength(siteId)){
			
			targetSite = siteService.findById(siteId);
			channel.setParent(null);
			channel.setSite(targetSite);
		}else{
			throw new ServiceException("参数错误！parentId和siteId必须传入一个！！");
		}
		channel.setSort(channelDao.findMaxSortOf(parent, targetSite)+1);
		//更新栏目
		channelDao.save(channel);
		
		//如果栏目剪切到其他站点，则更新栏目下文档的所属站点
		if(!channel.getSite().getId().equals(targetSite.getId())){
//			documentService.refreshBy(channel);
			channelDao.move2Site(channel, targetSite);
		}
	}
	@Override
	public Channel findByName(String name, Site currSite) {
		return channelDao.findByName(name,currSite);
	}
	@Override
	public List<Channel> findByMap(Channel pChannel, Map<String, String> params) {
		
		return channelDao.findByMap(pChannel,params);
	}
	@Override
	public List<Channel> findByMap(Site obj, Map<String, String> params) {
		return channelDao.findByMap(obj,params);
	}
	@Override
	public void move(String currId, String targetId) {
		
		channelDao.move(currId,targetId);
	}
	@Override
	public List<Template> findTemps(Channel channel, Integer type) {
		return channelDao.findTemps(channel,type);
	}
}
