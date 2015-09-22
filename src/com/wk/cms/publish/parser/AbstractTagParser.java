package com.wk.cms.publish.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.server.PublishContext;
import com.wk.cms.publish.server.PublishServer;
import com.wk.cms.publish.vo.PubObj;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.PageInfo;
import com.wk.cms.utils.PublishUtils;

public abstract class AbstractTagParser implements TagParser {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTagParser.class);
	protected PublishContext ctx;
	
	@Override
	public void setContext(PublishContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public String parse() throws ServiceException {
		
		Object obj = ctx.getObj();
		HtmlTag e = getTag();
		LOGGER.debug("开始解析对象"+obj+"，使用解析置标"+this.getClass());
		if(e.getName().equalsIgnoreCase("wk_index")){
			int index = 0;
			if(obj instanceof PubObj){
				index = ((PubObj)obj).getIndex();
			}
			return String.valueOf(index);
		}
		if(obj instanceof PubObj){
			obj = ((PubObj)obj).getObj();
		}
		
		
		String pubFileName = this.getPubFileName(obj);
		if("_url".equalsIgnoreCase(e.attr("field"))&&pubFileName!=null){
			return getPubDir(ctx.getBase(), obj)+pubFileName;
		}
		
		boolean isFirstPager = "true".equalsIgnoreCase(e.attr("pager"))&&!StringUtils.hasLength(e.attr("p"))&&isSupportPager();
		
		String c = this.parseInternal(obj);
		
		//第一页发布仅作为触发，不进行实际发布
		if(isFirstPager){
			return null;
		}
		return c==null?"":c;
	}
	
	/**
	 * 是否支持分页，默认不支持，若需要支持则实现类需要重写该方法并返回true
	 * @return
	 */
	protected boolean isSupportPager() {
		return false;
	}

	protected HtmlTag getTag() {
		return ctx.getTag();
	}

	protected String getPubDir(Object base,Object obj){
		return PublishUtils.getPath2Path(PublishUtils.getDir(base), PublishUtils.getDir(obj));
	}
	
	protected String getPubFileName(Object obj){
		return null;
	}
	/**
	 * 解析置标
	 * @param obj	当前对象
	 * @param base	发布对象（根）
	 * @param con	渲染内容
	 * @return
	 * @throws ServiceException
	 */
	protected abstract String parseInternal(Object obj) throws ServiceException ;
	protected Site getSite(Object obj) {

		return PublishUtils.getSite(obj);
	}
	
	protected void checkIfTriggerPager(PageInfo pageInfo) throws PublishException{
		HtmlTag e = ctx.getTag();
		if("true".equalsIgnoreCase(e.attr("pager"))&&isSupportPager()){
			if(!StringUtils.hasLength(e.attr("p"))||pageInfo.getTotalCount()>pageInfo.getStart()+pageInfo.getLimit()){
				LOGGER.debug("Need trigger pager function~~");
				triggerPublishNextPage(pageInfo);
			}
		}
		
	}
	
	protected void triggerPublishNextPage(PageInfo pageInfo) throws PublishException{
		
		HtmlTag tag = ctx.getTag();
		Object obj = ctx.getObj();
		PublishServer server = ctx.getServer();
		
		
		String currPage = tag.attr("p");
		int p = StringUtils.hasLength(currPage)?Integer.parseInt(currPage):0;
		
		int nextPage = p+1;
		tag.attr("p", String.valueOf(nextPage));
		pageInfo.setPage(nextPage);
		
		StringBuilder baseHtml = ctx.getBaseHtml();
		String newCon = baseHtml.substring(0, tag.getStartpos())+"<"+tag.getName()+" "+tag.attrs()+">"+baseHtml.substring(tag.getEndpos());
		
		try {
			String c = server.parse(obj, ctx.getBase(), newCon, new StringBuilder(newCon),ctx.getTemplate());
			c = server.updateContentTempFile(obj, c);
			
			c = addPageInfo(c,pageInfo,ctx.getTemplate());
			
			String pageName = PublishUtils.getPubFileName(obj,ctx.getTemplate(),nextPage);
			FileUtils.writeFile(c,pageName,server.getAbsoluteDir(obj));
		} catch (ServiceException e) {
			throw new PublishException("发布第"+p+"页失败！",e);
		}
	}

	private static String addPageInfo(String c, PageInfo pageInfo,Template template) {
		
		c = c.replaceAll("\\$\\{\\s*currPage\\s*\\}", String.valueOf(pageInfo.getPage()))
				.replaceAll("\\$\\{\\s*pageSize\\s*\\}", String.valueOf(pageInfo.getLimit()))
				.replaceAll("\\$\\{\\s*total\\s*\\}", String.valueOf(pageInfo.getTotalCount()))
				.replaceAll("\\$\\{\\s*prefix\\s*\\}", template.getPrefix())
				.replaceAll("\\$\\{\\s*ext\\s*\\}", template.getExt());
		return c;
	}
	
}
