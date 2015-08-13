package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.dao.ISiteDao;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.publish.IPublishServer;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;

@Service
public class SiteService implements ISiteService {

	@Autowired
	private ISiteDao siteDao;
	
	@Autowired
	private IPublishServer publishServer;
	@Override
	public List<Site> findAll() {
		return siteDao.findAll();
	}
	@Override
	public void save(Site site) throws ServiceException {
		
		if(!StringUtils.hasLength(site.getId())){
			site.setCrTime(new Date());
			site.setCrUser(null);
			
			if(findByName(site.getName())!=null){
				throw new ServiceException("名称为【"+site.getName()+"】的站点已存在！");
			}
			siteDao.save(site);
		}else{
			Site persistSite = findById(site.getId());
			BeanUtils.copyProperties(site, persistSite, new String[]{"id","crUser","crTime"});
			
			siteDao.save(persistSite);
		}
		
	}
	
	@Override
	public Site findByName(String name) {
		
		return siteDao.findByName(name);
	}
	@Override
	public Site findById(String siteId) throws ServiceException {
		
		Site site = siteDao.findById(siteId);
		if(site==null){
			throw new ServiceException("未发现编号为【"+siteId+"】的站点！");
		}
		return site;
	}
	@Override
	public void deleteById(String siteId) throws ServiceException {
		
		if(!StringUtils.hasLength(siteId)){
			throw new ServiceException("参数传递错误！");
		}
		
		siteDao.deleteById(siteId);
		
	}
	@Override
	public void imp(MultipartFile file,String encode) throws ServiceException {
		
		if(file==null||file.isEmpty()){
			throw new ServiceException("文件上传失败！！");
		}
		
		try {
			String[] siteNames = FileUtils.parseTxt2Arr(file,encode);
			
			if(!CommonUtils.isEmpty(siteNames)){
				for(String siteName : siteNames){
					if(StringUtils.hasLength(siteName)){
						save(new Site(null,siteName,siteName,null,null,new Date(),null,null));
					}
					
				}
			}
		} catch (FileParseException e) {
			throw new ServiceException("解析文件失败！",e);
		}
	}
	@Override
	public String previewById(String siteId) throws ServiceException {
		
		Site site = findById(siteId);
		
		return publishServer.publish(site, true,null);
	}
	@Override
	public List<Template> findTemplatesBySite(Site obj) {
		return siteDao.findTemplatesBySite(obj);
	}

}
