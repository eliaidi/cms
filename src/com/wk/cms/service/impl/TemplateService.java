package com.wk.cms.service.impl;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.zip.ZipFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.model.File;
import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.model.Template.Type;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.MyBlob;
import com.wk.cms.utils.PageInfo;

@Service
public class TemplateService implements ITemplateService {

	@Autowired
	private ITemplateDao templateDao;
	
	@Autowired
	private ISiteService siteService;

	@Override
	public PageInfo find(String siteId,String show ,PageInfo pageInfo, String query) {
		return templateDao.find(siteId,show,pageInfo, query);
	}

	@Override
	public void save(Template template) throws ServiceException {

		if (!StringUtils.hasLength(template.getId())) {

			template.getFile().setCrTime(new Date());
			template.getFile().setCrUser(null);
			
			if(template.getSite()!=null&&StringUtils.hasLength(template.getSite().getId())){
				template.setSite(siteService.findById(template.getSite().getId()));
			}

			templateDao.save(template);
		} else {

			Template pt = findById(template.getId());

			BeanUtils.copyProperties(template.getFile(), pt.getFile(),
					new String[] { "id", "crTime", "crUser" });
			BeanUtils.copyProperties(template, pt,
					new String[] { "id", "file" });

			templateDao.save(pt);
		}
	}

	@Override
	public Template findById(String id) {
		return templateDao.findById(id);
	}

	@Override
	public void delete(String[] ids) {

		for (String id : ids) {
			delete(id);
		}
	}

	@Override
	public void delete(String id) {

		templateDao.delete(id);
	}

	@Override
	public Template loadRemoteDoc(String url,String siteId) throws FileParseException {
		
		try {
			String fileCon = FileUtils.getFileCon(new URL(url),5000,"UTF-8");
			String title = url.substring(url.lastIndexOf("/")+1);
			Matcher tm = Pattern.compile("<title[^>]*>(.*?)</title>",Pattern.CASE_INSENSITIVE).matcher(fileCon);
			if(tm.find()) title = tm.group(1);
			Template template = new Template();
			template.setName(title);
			template.setFile(new File(new MyBlob(fileCon)));
			template.setRemoteUrl(url);
			template.setSite(siteService.findById(siteId));
			template.setType(Type.OUTLINE);
			
			save(template);
			return template;
		} catch (Exception e) {
			throw new FileParseException("获取远程文档失败！！",e);
		} 
		
	}

	@Override
	public List<TempFile> findTempFilesByFileNames(List<String> tempFileNames,
			Site site) {
		return templateDao.findTempFiles(tempFileNames,site);
	}

	@Override
	public List<TempFile> findTempFilesBySite(Site site) {
		
		return templateDao.findTempFiles( site);
	}

	@Override
	public void imp(MultipartFile f, String siteId,String encode) throws ServiceException {
		
		if(f.isEmpty()) throw new ServiceException("导入文件为空！");
		String fExt = FileUtils.getFileExt(f.getOriginalFilename());
		if(!"zip".equalsIgnoreCase(fExt)){
			throw new ServiceException("导入文件必须是ZIP文件！");
		}
		java.io.File tempFile = FileUtils.saveMultiFileToTemp(f);
		java.io.File tempfolder = new java.io.File(tempFile.getParentFile(), tempFile.getName().substring(0, tempFile.getName().lastIndexOf(".")));
		try {
			ZipFile zf = new ZipFile(tempFile,System.getProperty("sun.jnu.encoding"));
			FileUtils.unZip(zf,tempfolder);
			
			templateDao.search2Add(tempfolder,siteService.findById(siteId),StringUtils.hasLength(encode)?encode:"UTF-8");
		} catch (Exception e) {
			throw new ServiceException("导入模板发生错误！", e);
		}finally{
			FileUtils.delete(tempFile);
			FileUtils.delete(tempfolder);
		}
	}

	@Override
	public Template findByName(String tName) {

		return templateDao.findByName(tName);
	}

	@Override
	public PageInfo findFiles(String siteId, PageInfo pageInfo,String query) {
		return templateDao.findFiles(siteId,pageInfo,query);
	}

	@Override
	public TempFile uploadFile(MultipartFile f, String siteId, String id,String encode) throws ServiceException {
		
		if(f.isEmpty()){
			throw new ServiceException("上传文件不能为空！"+f);
		}
		TempFile tf = null;
		if(StringUtils.hasLength(id)){
			tf = templateDao.findFileByFId(id);
			File file = tf.getFile();
			file.setContent(new MyBlob(f));
			tf.setFile(file);
			
			templateDao.saveFile(tf);
		}else{
			tf = new TempFile(null, null, new File(UUID.randomUUID().toString(),f,encode), siteService.findById(siteId));
			templateDao.saveFile(tf);
		}
		return tf;
	}

}
