package com.wk.cms.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.tools.zip.ZipFile;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
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
	public PageInfo find(String siteId ,PageInfo pageInfo, String query) {
		return templateDao.find(siteId,pageInfo, query);
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
			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 5000);
			Elements titleEs = document.select("title");
			
			Template template = new Template();
			template.setName(titleEs.size()>0?titleEs.get(0).html():"");
			template.setFile(new File(new MyBlob(document.html())));
			template.setRemoteUrl(url);
			template.setSite(siteService.findById(siteId));
			
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

}
