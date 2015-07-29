package com.wk.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.items.SiteItem;
import com.wk.cms.model.Site;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.exception.ServiceException;

@Controller
@RequestMapping("/site")
public class SiteController {

	@Autowired
	private ISiteService siteService;
	
	@RequestMapping("/index")
	public String index(){
		
		return "site/index";
	}
	
	@RequestMapping("/list")
	public @ResponseBody Map<String, Object> listAll(){
		
		List<Site> sites = siteService.findAll();
		List<SiteItem> results = new ArrayList<SiteItem>();
		
		for(Site s:sites){
			
			results.add(new SiteItem(s.getId(),s.getName(),"grid-shortcut","site-module"));
		}
		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("sites", results);
		return rs;
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(Site site) throws ServiceException{
		
		siteService.save(site);
		
		return new Message(true, "保存成功！", site);
	}
	
	@RequestMapping("/detail")
	public @ResponseBody Message detail(String siteId) throws ServiceException{
		
		return new Message(true,"",siteService.findById(siteId));
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String siteId) throws ServiceException{
		
		siteService.deleteById(siteId);
		
		return new Message(true, "删除成功！", null);
	}
	
	@RequestMapping("/imp")
	public @ResponseBody Message imp(@RequestParam("file") MultipartFile file) throws ServiceException{
		
		siteService.imp(file);
		return new Message(true, "导入成功！", null);
	}
	
	@RequestMapping("/preview")
	public @ResponseBody Message preview(String siteId) throws ServiceException{
		
		return new Message(true, null, siteService.previewById(siteId));
	}
}
