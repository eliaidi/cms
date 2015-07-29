package com.wk.cms.publish.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.publish.IPublishServer;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.type.PublishType;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;

@Component
@Transactional(readOnly = true)
public class PublishServer implements IPublishServer {

	private static final int MAX_POOL_SIZE = 10;
	private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
			MAX_POOL_SIZE);
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishServer.class);

	@Override
	public String publish(Object obj, boolean isPreview, PublishType type)
			throws PublishException {

		if (isPreview)
			return preview(obj);
		return null;
	}

	private String preview(Object obj) throws PublishException {

		if (obj instanceof Site)
			return previewSite((Site) obj);
		if (obj instanceof Channel)
			return previewChannel((Channel) obj);
		if (obj instanceof Document)
			return previewDocument((Document) obj);

		throw new PublishException("不支持预览此类型对象【" + obj + "】！！");
	}

	private String previewDocument(Document obj) {
		// TODO Auto-generated method stub
		return null;
	}

	private String previewChannel(Channel obj) {
		// TODO Auto-generated method stub
		return null;
	}

	private String previewSite(final Site obj) throws PublishException {

		ISiteService siteService = BeanFactory.getBean(ISiteService.class);

		List<Template> templates = siteService.findTemplatesBySite(obj);
		if (CommonUtils.isEmpty(templates))
			throw new PublishException("站点【" + obj.getName() + "】未配置模板！！");
		
		final String pDir = getPreviewDir(obj);
		publishTempFile(obj,getPreviewDir(getSite(obj))+File.separator+ITemplateService.TEMPFILE_FOLDER);
		for (final Template template : templates) {
			EXECUTOR.execute(new Runnable() {

				@Override
				public void run() {
					try {
						publishInternal(obj, template, pDir);
					} catch (PublishException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			});
		}
		return getDir(obj);
	}

	private void publishTempFile(Object obj, String pDir) {
		
		ITemplateService templateService = BeanFactory.getBean(ITemplateService.class);
		List<TempFile> tempFiles = templateService.findTempFilesBySite(getSite(obj));
		
		for(TempFile tf : tempFiles){
			try {
				FileUtils.witeFile(tf,pDir);
			} catch (ServiceException e) {
				LOGGER.error("模板附件生成失败！",e);
			}
		}
	}

	private void publishInternal(Object obj, Template template, String pDir) throws PublishException {
		
		try {
			
			String content = CommonUtils.getContent(template.getFile().getContent().getBinaryStream());
			updateContentTempFile(obj,content,pDir);
		} catch (Exception e) {
			throw new PublishException(e.getMessage(),e);
		} 

	}

	private void updateContentTempFile(Object obj,String content, String pDir) throws ServiceException {
		
		org.jsoup.nodes.Document document = Jsoup.parse(content);
		Elements tfEs = document.getElementsByAttributeValue("hasdone", "true");
		String tfPath = getPath2Path(getDir(obj),getDir(getSite(obj))+File.separator+"images");
		List<String> tempFileNames = new ArrayList<String>();
		for(Element el : tfEs){
			String attrName = CommonUtils.getRemoteAttrNameByTagName(el.tagName());
			String attrVal = el.attr(attrName);
			el.attr(attrName, tfPath+attrVal);
			tempFileNames.add(attrVal);
		}
		
		content = document.html();
	}

	private String getPath2Path(String f, String t) {
		
		String[] fArr = f.split("\\"+File.separator);
		String[] tArr = t.split("\\"+File.separator);
		
		String p = "";
		for(int i=0;i<fArr.length;i++){
			if(!StringUtils.hasLength(fArr[i])) continue;
			if(tArr.length>=(i+1)&&fArr[i].equalsIgnoreCase(tArr[i])) continue;
			p += ".."+File.separator;
		}
		for(int i=0;i<tArr.length;i++){
			if(!StringUtils.hasLength(tArr[i])) continue;
			if(fArr.length>=(i+1)&&fArr[i].equalsIgnoreCase(tArr[i])) continue;
			
			p = p+tArr[i]+File.separator;
		}
		return p;
	}
	
	private String getPreviewDir(Object obj) {
		String dir = getDir(obj);
		dir = CommonUtils.getAppPath("cms") + File.separator + "preivew" + dir;
		
		File f = new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		return dir;
	}

	private String getDir(Object obj) {
		Site currSite = getSite(obj);
		String dir = "";
		String sep = File.separator;
		if (obj instanceof Site) {
		} else if (obj instanceof Channel) {
			Channel chnl = (Channel) obj;
			while (chnl != null) {
				dir = sep + chnl.getFolder() + dir;
				chnl = chnl.getParent();
			}
		} else if (obj instanceof Document) {
			Document doc = (Document) obj;
			Channel currChnl = doc.getChannel();
			dir = getPreviewDir(currChnl);
		}
		dir = sep + currSite.getFolder() + dir;
		return dir;
	}

	private Site getSite(Object obj) {
		
		if(obj instanceof Site) return (Site)obj;
		if(obj instanceof Channel) return ((Channel)obj).getSite();
		if(obj instanceof Document) return ((Document) obj).getSite();
		return null;
	}

}
