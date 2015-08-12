package com.wk.cms.controller;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/template")
@MultipartConfig(maxFileSize=50000000)
public class TemplateController {
	

	@Autowired
	private ITemplateService templateService;

	@RequestMapping("/list")
	public @ResponseBody
	PageInfo list(String siteId ,String show,PageInfo pageInfo, String query) {

		return templateService.find(siteId,show,pageInfo, query);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(Template template) throws ServiceException{
		
		templateService.save(template);
		return new Message(true, "保存成功！！", template);
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(@RequestParam("ids") String[] ids){
		
		templateService.delete(ids);
		return new Message(true, "删除成功！！", null);
	}
	
	@RequestMapping("/remote")
	public @ResponseBody Message loadRemote(String url,String siteId) throws FileParseException{
		
		Template template = templateService.loadRemoteDoc(url, siteId);
		return new Message(true, "加载成功！！", template);
	}
	
	@RequestMapping("/imp")
	public @ResponseBody Message imp(@RequestParam("file")  MultipartFile f,String siteId,String encode) throws ServiceException{
		
		templateService.imp(f,siteId,encode);
		return new Message(true, "导入成功！！", null);
	}
	
	@RequestMapping("/file/list")
	@ResponseBody
	public PageInfo fileList(String siteId,String query,PageInfo pageInfo){
		
		return templateService.findFiles(siteId,pageInfo,query);
	}
	
	@RequestMapping("/file/upload")
	@ResponseBody
	public Message upload(@RequestParam("file") MultipartFile f,String siteId,String id,String encode) throws ServiceException{
		
		TempFile tf = templateService.uploadFile(f,siteId,id,encode);
		return new Message(true, "上传成功！", tf);
	}
}
