package com.wk.cms.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.service.exception.ValidationException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IAppendixService appendixService;
	
	@RequestMapping("/view/{docId}")
	public ModelAndView view(@PathVariable("docId") String documentId){
		
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
	public @ResponseBody PageInfo list( String channelId, PageInfo pageInfo, String query) throws ServiceException{
		
		return documentService.find(channelId,pageInfo,query);
	}
	
	@RequestMapping("/detail")
	public @ResponseBody Message detail(String docId) throws ServiceException{
		
		Document document = documentService.findById(docId);
		
		return new Message(true, "加载成功！", document);
	}
	
	@RequestMapping("/save")
	public @ResponseBody Message save(@Valid Document document,String channelId,String appIds,BindingResult result ) throws ServiceException{
		if(result.hasErrors()){
			throw new ValidationException(result);
		}
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
	
	@RequestMapping("/remote")
	public @ResponseBody Message remote(String url) throws ParseException, ServiceException{
		
		Document document = documentService.loadRemoteDoc(url);
		return new Message(true, "加载成功！", document);
	}
	
	@RequestMapping("/copy")
	public @ResponseBody Message copy(@RequestParam("objIds") String[] objIds,String channelId) throws ServiceException{
		
		documentService.copy(objIds, channelId);
		return new Message(true, "拷贝成功！", null);
	}
	@RequestMapping("/cut")
	public @ResponseBody Message cut(@RequestParam("objIds") String[] objIds,String channelId) throws ServiceException{
		
		documentService.cut(objIds, channelId);
		return new Message(true, "剪切成功！", null);
	}
	
	@RequestMapping("/move")
	public @ResponseBody Message move(String currId,String targetId) throws ServiceException{
		
		documentService.move(currId,targetId);
		return new Message(true, "移动成功！", null);
	}
}

