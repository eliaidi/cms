package com.wk.cms.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.dao.IAppendixDao;
import com.wk.cms.model.Appendix;
import com.wk.cms.model.Document;
import com.wk.cms.model.File;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.IFileService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.MyBlob;
import com.wk.cms.utils.PageInfo;

@Service
public class AppendixService implements IAppendixService{

	@Autowired
	private IAppendixDao appendixDao;
	
	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private IFileService fileService;
	@Override
	public PageInfo list(String documentId, Integer type, PageInfo pageInfo) throws ServiceException {
		
		if(!StringUtils.hasLength(documentId)||type==null){
			throw new ServiceException("参数错误！documentId和type必须传入");
		}
		return appendixDao.find(documentId,type,pageInfo);
	}
	@Override
	public void save(MultipartFile file, Appendix appendix) throws ServiceException {
		
		if(!StringUtils.hasLength(appendix.getId())){
			if(file.isEmpty()){
				throw new ServiceException("上传失败！文件为空");
			}
			
			String fileName = file.getOriginalFilename();
			
			File f;
			try {
				f = new File(fileName,file.getSize(),fileName.indexOf(".")>0?fileName.substring(fileName.lastIndexOf(".")+1):null,new MyBlob(file.getBytes()),null);
			} catch (IOException e) {
				throw new ServiceException("实例化文件对象失败！！");
			}
			fileService.save(f);
			
			appendix.setFile(f);
			appendix.setName(fileName);
			
			appendixDao.save(appendix);
			
		}else{
			
			Appendix persistApp = findById(appendix.getId());
			BeanUtils.copyProperties(appendix, persistApp, new String[]{"id","document","type"});
			appendixDao.save(persistApp);
		}
		
	}
	@Override
	public Appendix findById(String id) throws ServiceException {
		
		if(!StringUtils.hasLength(id)){
			throw new ServiceException("参数错误！id不能为空");
		}
		return appendixDao.findById(id);
	}
	@Override
	public void attachTo(String appIds, String id) throws ServiceException {
		
		if(!StringUtils.hasLength(appIds)||!StringUtils.hasLength(id)){
			throw new ServiceException("参数错误！appIds和id必须传入");
		}
		
		String[] idArr = appIds.split(",");
		
		for(String appId : idArr){
			attachTo(findById(appId),documentService.findById(id));
		}
		
	}
	
	@Override
	public void attachTo(Appendix appendix, Document document) {
		
		appendix.setDocument(document);
		appendixDao.save(appendix);
	}
	@Override
	public List<Appendix> findByDocId(String documentId) throws ServiceException {
		
		if(!StringUtils.hasLength(documentId)){
			throw new ServiceException("参数错误！documentId必须传入！");
		}
		return appendixDao.findByDocId(documentId);
	}
	@Override
	public void delete(String id) throws ServiceException {
		
		if(!StringUtils.hasLength(id)){
			throw new ServiceException("参数错误！id必须传入");
		}
		appendixDao.delete(id);
	}
	@Override
	public void copy(Document document, Document newDoc) throws ServiceException {
		
		List<Appendix> appendixs = findByDocId(document.getId());
		if(!CommonUtils.isEmpty(appendixs)){
			for(Appendix appendix : appendixs){
				copy(appendix,newDoc);
			}
		}
	}
	
	@Override
	public void copy(Appendix appendix, Document newDoc) throws ServiceException {
		
		//复制文件
		File newFile = fileService.copy(appendix.getFile());
		
		//复制附件
		Appendix newApp = new Appendix();
		BeanUtils.copyProperties(appendix, newApp, new String[]{"id","file","document"});
		
		newApp.setDocument(newDoc);
		newApp.setFile(newFile);
		appendixDao.save(newApp);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Appendix> find(Document doc, Integer type) {
		return (List<Appendix>) appendixDao.find(doc.getId(), type, new PageInfo(0,IAppendixService.MAX_FETCH_NUM)).getList();
	}

}
