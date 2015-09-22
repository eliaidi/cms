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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
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
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.ISiteService;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.PublishUtils;

@Component
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PublishServer implements IPublishServer {

	private static final int MAX_POOL_SIZE = 10;
	private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
			MAX_POOL_SIZE);
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PublishServer.class);
	private static final String PUBLISH_COMP_FILE_NAME = "publish-comp-cfg.properties";
	private static final Properties pubCompCfg;
	private boolean isPreview ;
	
	
	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}

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

		this.isPreview = isPreview;
		if (isPreview)
			return preview(obj);
		return publish(obj);
	}

	private String publish(Object obj) {
		// TODO Auto-generated method stub
		LOGGER.debug(obj+"---"+isPreview);
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

	private String previewChannel(Channel obj) throws PublishException {
		
		if(!StringUtils.hasLength(obj.getOtempIds())){
			throw new PublishException("该栏目【"+obj.getId()+"】没有配置概览模板！");
		}
		IChannelService channelService = BeanFactory.getBean(IChannelService.class);
		List<Template> otemps = channelService.findTemps(obj, Template.Type.OUTLINE);
		if(CommonUtils.isEmpty(otemps)){
			throw new PublishException("未找到栏目【"+obj.getId()+"】的概览模板[otempids::"+obj.getOtempIds()+"]！");
		}
		Template tpl = otemps.get(0);
		publishInternal(obj, tpl);
		
		return ITemplateService.PREVIEW_FOLDER+PublishUtils.getDir(obj)+File.separator+PublishUtils.getPubFileName(obj, tpl);
	}

	private String previewSite(final Site obj) throws PublishException {

		ISiteService siteService = BeanFactory.getBean(ISiteService.class);

		List<Template> templates = siteService.findTemplatesBySite(obj);
		if (CommonUtils.isEmpty(templates))
			throw new PublishException("站点【" + obj.getName() + "】未配置模板！！");
		
		for (final Template template : templates) {
			publishInternal(obj, template);
		}
		return ITemplateService.PREVIEW_FOLDER+PublishUtils.getDir(obj)+File.separator+PublishUtils.getPubFileName(obj, templates.get(0));
	}

	private void publishTempFile(Object obj) {
		
		ITemplateService templateService = BeanFactory.getBean(ITemplateService.class);
		List<TempFile> tempFiles = templateService.findTempFilesBySite(PublishUtils.getSite(obj));
		
		for(TempFile tf : tempFiles){
			try {
				FileUtils.witeFile(tf,getAbsoluteDir(PublishUtils.getSite(obj))+File.separator+IPublishServer.TEMPFILE_FOLDER);
			} catch (ServiceException e) {
				LOGGER.error("模板附件生成失败！",e);
			}
		}
	}

	private void publishInternal(Object obj, Template template) throws PublishException {
		
		try {
			publishTempFile(obj);
			String content = CommonUtils.getContent(template.getFile().getContent().getBinaryStream());
			
			StringBuilder baseHtml = new StringBuilder(content);
			content = parse(obj,obj,content,baseHtml,template);
			
			if(content!=null){
				content = updateContentTempFile(obj,content);
				FileUtils.writeFile(content,PublishUtils.getPubFileName(obj,template),getAbsoluteDir(obj));
			}
			
			
		} catch (Exception e) {
			throw new PublishException(e.getMessage(),e);
		} 

	}

	public String getAbsoluteDir(Object obj) {
		return isPreview?PublishUtils.getPreviewDir(obj):PublishUtils.getPublishDir(obj);
	}

	/**
	 * 解析标签
	 * @param obj 当前解析对象
	 * @param base 发布对象（根对象）
	 * @param content
	 * @return
	 * @throws ServiceException 
	 */
	public String parse(Object obj,Object base, String content,StringBuilder baseHtml,Template template) throws ServiceException {
		
		Pattern pattern = Pattern.compile("<(wk_[a-z0-9\\$]+)\\s+([^>]*)>",Pattern.CASE_INSENSITIVE);
		Matcher m = null;
		StringBuilder sb = new StringBuilder();
		while(true){
			sb.delete(0, sb.length());
			m = pattern.matcher(content);
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
			
			HtmlTag tag = new HtmlTag(tagName,attrStr,innerHtml,m.start(),m.start()+header.length());
			PublishContext ctx = new PublishContext(obj,base,new StringBuilder(content),tag,this,template);
			TagParser parser = getParser(ctx);
			String newCon = parser.parse();
			if(newCon==null){
				return null;
			}
			
			sb.append(content.substring(0, m.start()));
			sb.append(newCon);
			sb.append(content.substring(end));
			
			content = sb.toString();
		}
		
		return content;
	}

	private static TagParser getParser(PublishContext ctx) throws PublishException {
		HtmlTag tag = ctx.getTag();
		String tagName = tag.getName();
		tagName = tagName.split("\\$")[0];
		String pubBeanName = pubCompCfg.getProperty(tagName);
		if(!StringUtils.hasLength(pubBeanName)){
			throw new PublishException("未在配置文件中找到【k="+tagName+"】的项！");
		}
		TagParser parser = BeanFactory.getBean(pubBeanName);
		if(parser==null){
			throw new PublishException("未找到beanName为【"+pubBeanName+"】的发布组件，【k="+tagName+"】的项！");
		}
		parser.setContext(ctx);
		return parser;
	}

	public String updateContentTempFile(Object obj,String content) throws ServiceException {
		
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
			if("true".equalsIgnoreCase(tag.attr("ignore"))){
				continue;
			}
			tag.removeAttr("hasDone");
			if(StringUtils.hasLength(tag.attr(attrName))){
				tag.attr(attrName,tfPath+tag.attr(attrName));
			}
			m.appendReplacement(sb, StringEscapeUtils.escapeJava("<"+tagName+" "+tag.attrs()+">"));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
	}

}
