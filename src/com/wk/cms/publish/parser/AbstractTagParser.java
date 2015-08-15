package com.wk.cms.publish.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wk.cms.model.Site;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.server.PublishContext;
import com.wk.cms.publish.vo.PubObj;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PublishUtils;

public abstract class AbstractTagParser implements TagParser {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTagParser.class);
	protected PublishContext ctx;
	
	@Override
	public void setContext(PublishContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public String parse(Object obj) throws ServiceException {
		
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
			return getPubDir(ctx.getBase(), obj)+getPubFileName(obj);
		}
		
		String c = this.parseInternal(obj);
		return c;
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
}
