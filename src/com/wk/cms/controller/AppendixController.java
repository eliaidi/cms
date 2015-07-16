package com.wk.cms.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Appendix;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/appendix")
public class AppendixController {

	@Autowired
	private IAppendixService appendixService;
	@RequestMapping("/list")
	public @ResponseBody PageInfo list(String documentId,PageInfo pageInfo,Integer type) throws ServiceException{
		
		return appendixService.list(documentId,type,pageInfo);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(@RequestParam("f") MultipartFile file,Appendix appendix ) throws ServiceException, IOException{
		
		appendixService.save(file,appendix);
		
		return new Message(true, "上传成功！", appendix);
	}
	
	@RequestMapping("/update")
	public @ResponseBody Message update(Appendix appendix) throws ServiceException, IOException{
		
		appendixService.save(null, appendix);
		
		return new Message(true, "保存成功！", appendix);
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String id) throws ServiceException{
		
		appendixService.delete(id);
		return new Message(true, "删除成功！", null);
	}
}
