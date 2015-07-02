package com.wk.cms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.Document;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IAppendixService appendixService;
	
	@RequestMapping("/view")
	public ModelAndView view( String documentId){
		
		ModelAndView mav = new ModelAndView();
		try {
			Document document = documentService.findById(documentId);
			document.setAppendixs(CommonUtils.list2Set(appendixService.findByDocId(documentId)));
			mav.addObject("obj", document);
		} catch (ServiceException e) {
			e.printStackTrace();
			mav.addObject("error", e);
			mav.setViewName("error");
			return mav;
		}
		mav.setViewName("document/document-view");
		return mav;
	}
	@RequestMapping("/list")
	public @ResponseBody PageInfo list(String channelId, PageInfo pageInfo,String query) throws ServiceException{
		
		return documentService.find(channelId,pageInfo,query);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save( Document document,String channelId,String appIds) throws ServiceException{
		documentService.save(document,channelId);
		
		if(StringUtils.hasLength(appIds)){
			appendixService.attachTo(appIds,document.getId());
		}
		return new Message(true, "保存成功！", document);
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String id) throws ServiceException{
		
		documentService.deleteById(id);
		
		return new Message(true, "删除成功！", null);
	}
	
	@RequestMapping("/deleteMuti")
	public @ResponseBody Message deleteByIds(String ids) throws ServiceException{
		
		documentService.deleteByIds(ids);
		
		return new Message(true, "删除成功！", null);
	}
}

