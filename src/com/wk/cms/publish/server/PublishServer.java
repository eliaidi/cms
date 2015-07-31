package com.wk.cms.publish.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
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
import com.wk.cms.publish.parser.TagParser;
import com.wk.cms.publish.type.PublishType;
import com.wk.cms.publish.utils.ParseUtils;
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
	private static final String PUBLISH_COMP_FILE_NAME = "publish-comp-cfg.properties";
	private static final Properties pubCompCfg;
	
	static{
		pubCompCfg = new Properties();
		try {
			pubCompCfg.load(new FileInputStream(new File(PublishServer.class.getResource("/").getPath()+PUBLISH_COMP_FILE_NAME)));
		} catch (Exception e) {
			LOGGER.error("加载发布组件失败，文件【"+PUBLISH_COMP_FILE_NAME+"】",e);
		} 
	}

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
		return ITemplateService.PREVIEW_FOLDER+getDir(obj)+File.separator+getPubFileName(obj, templates.get(0));
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
			
			content = updateContentTempFile(obj,content,pDir);
			
			content = parse(obj,content);
			
			FileUtils.writeFile(content,getPubFileName(obj,template),pDir);
		} catch (Exception e) {
			throw new PublishException(e.getMessage(),e);
		} 

	}

	private String getPubFileName(Object obj, Template template) {
		
		if(obj instanceof Document){
			return template.getPrefix()+"-"+((Document)obj).getId()+"."+template.getExt();
		}
		return template.getPrefix()+"."+template.getExt();
	}

	public static String parse(Object obj, String content) throws PublishException {
		
		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		Elements es = CommonUtils.getElementsByTagNamePrefix(doc,"wk_");
		if(es.size()==0){
			return ParseUtils.parse(obj,content);
		}
		for(Element e : es){
			TagParser parser = getParser(e);
			parser.parse( obj,content);
		}
		
		return content;
	}

	private static TagParser getParser(Element e) throws PublishException {
		
		String pubBeanName = pubCompCfg.getProperty(e.tagName());
		if(!StringUtils.hasLength(pubBeanName)){
			throw new PublishException("未在配置文件中找到【k="+e.tagName()+"】的项！");
		}
		TagParser parser = BeanFactory.getBean(pubBeanName);
		if(parser==null){
			throw new PublishException("未找到beanName为【"+pubBeanName+"】的发布组件，【k="+e.tagName()+"】的项！");
		}
		parser.setElement(e);
		return parser;
	}

	private String updateContentTempFile(Object obj,String content, String pDir) throws ServiceException {
		
		org.jsoup.nodes.Document document = Jsoup.parse(content);
		Elements tfEs = document.getElementsByAttributeValue("hasdone", "true");
		String tfPath = getPath2Path(getDir(obj),getDir(getSite(obj))+File.separator+"images");
		for(Element el : tfEs){
			String attrName = CommonUtils.getRemoteAttrNameByTagName(el.tagName());
			String attrVal = el.attr(attrName);
			el.attr(attrName, tfPath+attrVal).removeAttr("hasdone");
		}
		
		return document.html();
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
		dir = CommonUtils.getAppPath("cms") + File.separator + ITemplateService.PREVIEW_FOLDER + dir;
		
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
