package com.wk.cms.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IDocumentDao;
import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Service
public class DocumentService implements IDocumentService {

	@Autowired
	private IDocumentDao documentDao;
	
	@Autowired
	private IChannelService channelService;
	@Override
	public PageInfo find(String channelId, PageInfo pageInfo, String query) throws ServiceException {
		
		if(!StringUtils.hasLength(channelId)){
			throw new ServiceException("参数错误！channelId不能为空");
		}
		return documentDao.find(channelId,pageInfo,query);
	}
	@Override
	public void save(Document document, String channelId) throws ServiceException {
		
		if(!StringUtils.hasLength(document.getTitle())||!StringUtils.hasLength(document.getContent())){
			throw new ServiceException("参数错误！title和content不能为空");
		}
		
		if(!StringUtils.hasLength(document.getId())){
			
			if(!StringUtils.hasLength(channelId)){
				throw new ServiceException("参数错误！docId和channelId至少穿入一个");
			}
			
			Channel channel = channelService.findById(channelId);
			if(channel==null){
				throw new ServiceException("参数错误！channelId为【"+channelId+"】的栏目不存在");
			}
			document.setStatus(1);
			document.setChannel(channel);
			document.setSite(channel.getSite());
			document.setCrTime(new Date());
			document.setCrUser(null);
			
			documentDao.save(document);
		}else{
			
			Document persistDoc = findById(document.getId());
			if(persistDoc == null){
				throw new ServiceException("未找到id为【"+document.getId()+"】的文档！");
			}
			
			BeanUtils.copyProperties(document, persistDoc, new String[]{"id","channel","site","crUser","crTime"});
			
			persistDoc.setStatus(2);
			documentDao.save(persistDoc);
		}
		
	}
	
	@Override
	public Document findById(String id) throws ServiceException {
		
		if(!StringUtils.hasLength(id)){
			throw new ServiceException("id不能为空！");
		}
		Document document = documentDao.findById(id);
		if(document==null){
			throw new ServiceException("未找到ID为【"+id+"】的文档对象！！！");
		}
		return document;
	}
	@Override
	public void deleteById(String id) throws ServiceException {
		
		if(!StringUtils.hasLength(id)){
			throw new ServiceException("参数错误！docId必须穿入");
		}
		documentDao.deleteById(id);
	}
	@Override
	public void deleteByIds(String ids) throws ServiceException {
		
		if(!StringUtils.hasLength(ids)){
			throw new ServiceException("参数错误！ids必须穿入");
		}
		
		String[] idArr = ids.split(",");
		for(String id : idArr){
			deleteById(id);
		}
	}
	
	@Override
	public List<Document> findByIds(String ids) throws ServiceException {
		
		if(!StringUtils.hasLength(ids)){
			throw new ServiceException("参数错误，ids必须穿入");
		}
		return documentDao.findByIds(ids);
	}
	@Override
	public Document loadRemoteDoc(String url) throws  ParseException, ServiceException {
		
		if(!StringUtils.hasLength(url)){
			throw new ServiceException("参数错误！url必须传入");
		}
		return CommonUtils.loadRemoteDoc(url);
		
	}
	
	/*public static void main(String[] args) {
		
		try {
			new DocumentService().loadRemoteDoc("http://news.163.com/15/0703/10/ATJEVLGV000146BE.html");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
