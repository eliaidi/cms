package com.wk.cms.publish.parser;

import java.io.File;


import com.wk.cms.model.Site;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PublishUtils;

public abstract class AbstractTagParser implements TagParser {

	protected HtmlTag e;
	public AbstractTagParser(){
		
	}
	public AbstractTagParser(HtmlTag el){
		this.e = el;
	}
	
	@Override
	public void setTag(HtmlTag htmlTag) {
		this.e = htmlTag;
	}
	
	@Override
	public String parse(Object obj,Object base, String con) throws ServiceException {
		
		String pubFileName = this.getPubFileName(obj);
		if("_url".equalsIgnoreCase(e.attr("field"))&&pubFileName!=null){
			return PublishUtils.getPath2Path(PublishUtils.getDir(obj), PublishUtils.getDir(base))+File.separator+pubFileName;
		}
		String c = this.parseInternal(obj,base, con);
		return c;
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
	protected abstract String parseInternal(Object obj,Object base, String con) throws ServiceException ;
	protected Site getSite(Object obj) {

		return PublishUtils.getSite(obj);
	}
}
