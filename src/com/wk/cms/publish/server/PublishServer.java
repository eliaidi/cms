package com.wk.cms.publish.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wk.cms.cfg.SysCfg;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.IPublishServer;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.parser.TagParser;
import com.wk.cms.publish.type.PublishType;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.PublishUtils;

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
		publishTempFile(obj,getPreviewDir(PublishUtils.getSite(obj))+File.separator+ITemplateService.TEMPFILE_FOLDER);
		for (final Template template : templates) {
			publishInternal(obj, template, pDir);
			/*EXECUTOR.execute(new Runnable() {
				@Override
				public void run() {
					try {
						publishInternal(obj, template, pDir);
					} catch (PublishException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			});*/
		}
		return ITemplateService.PREVIEW_FOLDER+PublishUtils.getDir(obj)+File.separator+PublishUtils.getPubFileName(obj, templates.get(0));
	}

	private void publishTempFile(Object obj, String pDir) {
		
		ITemplateService templateService = BeanFactory.getBean(ITemplateService.class);
		List<TempFile> tempFiles = templateService.findTempFilesBySite(PublishUtils.getSite(obj));
		
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
			
			content = parse(obj,obj,content);
			content = updateContentTempFile(obj,content,pDir);
			
			FileUtils.writeFile(content,PublishUtils.getPubFileName(obj,template),pDir);
		} catch (Exception e) {
			throw new PublishException(e.getMessage(),e);
		} 

	}

	/**
	 * 解析标签
	 * @param obj 当前解析对象
	 * @param base 发布对象（根对象）
	 * @param content
	 * @return
	 * @throws ServiceException 
	 */
	public static String parse(Object obj,Object base, String content) throws ServiceException {
		
		Matcher m = null;
		StringBuffer sb = null;
		while(true){
			sb = new StringBuffer();
			m = Pattern.compile("<(wk_[a-z0-9\\$]+)\\s+([^>]*)>",Pattern.CASE_INSENSITIVE).matcher(content);
			if(!m.find()){
				break;
			}
			String header = m.group(),tagName = m.group(1),attrStr = m.group(2),endTag = "</"+tagName+">";
			String aftHtml = content.substring(m.end()), innerHtml = "";
			if(header.endsWith("/>")){
				endTag = "";
			}else{
				innerHtml = aftHtml.substring(0, aftHtml.indexOf(endTag));
			}
			int end = m.start()+header.length()+innerHtml.length()+endTag.length();
			
			TagParser parser = getParser(new HtmlTag(tagName,attrStr,innerHtml));
			String newCon = parser.parse(obj,base, content);
			
			sb.append(content.substring(0, m.start()));
			sb.append(newCon);
			sb.append(content.substring(end));
			
			content = sb.toString();
		}
		
		return content;
	}

	private static TagParser getParser(HtmlTag tag) throws PublishException {
		String tagName = tag.getName();
		tagName = tagName.split("\\$")[0];
		String pubBeanName = pubCompCfg.getProperty(tag.getName());
		if(!StringUtils.hasLength(pubBeanName)){
			throw new PublishException("未在配置文件中找到【k="+tagName+"】的项！");
		}
		TagParser parser = BeanFactory.getBean(pubBeanName);
		if(parser==null){
			throw new PublishException("未找到beanName为【"+pubBeanName+"】的发布组件，【k="+tagName+"】的项！");
		}
		parser.setTag(tag);
		return parser;
	}

	private String updateContentTempFile(Object obj,String content, String pDir) throws ServiceException {
		
		String tfPath = PublishUtils.getPath2Path(PublishUtils.getDir(obj),PublishUtils.getDir(PublishUtils.getSite(obj))+File.separator+"images");
		Map<String, String> parseTags = SysCfg.getSupportParseTags();
		String parseTagStr = CommonUtils.join(parseTags.keySet(), "|");
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile("<("+parseTagStr+")\\s+hasDone=true([^>]+)>",Pattern.CASE_INSENSITIVE).matcher(content);
		while(m.find()){
			String tagName = m.group(1);
			String attrStr = m.group(2);
			String attrName = parseTags.get(tagName);
			
			HtmlTag tag = new HtmlTag(tagName, attrStr,"");
			tag.removeAttr("hasDone");
			if(StringUtils.hasLength(tag.attr(attrName))){
				tag.attr(attrName,tfPath+tag.attr(attrName));
			}
			m.appendReplacement(sb, StringEscapeUtils.escapeJava("<"+tagName+" "+tag.attrs()+">"));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	
	private String getPreviewDir(Object obj) {
		String dir = PublishUtils.getDir(obj);
		dir = CommonUtils.getAppPath("cms") + File.separator + ITemplateService.PREVIEW_FOLDER + dir;
		
		File f = new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		return dir;
	}

}
