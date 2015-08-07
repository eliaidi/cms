package com.wk.cms.publish.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.wk.cms.service.exception.ServiceException;

public abstract class AbstractTagParser implements TagParser {

	protected Element e;
	public AbstractTagParser(){
		
	}
	public AbstractTagParser(Element el){
		this.e = el;
	}
	
	@Override
	public void setElement(Element e) {
		this.e = e;
	}
	
	@Override
	public String parse(Object obj, String con) throws ServiceException {
		
		String c = this.parseInternal(obj, con);
		boolean isDel = "true".equalsIgnoreCase(e.attr("w_istemp"));
		Attributes attrs = e.attributes();
		for(Attribute attr : attrs){
			if(attr.getKey().toLowerCase().startsWith("w_")){
				e.removeAttr(attr.getKey());
			}
		}
		if(isDel){
			e.replaceWith(Jsoup.parse(c));
		}
		return c;
	}
	
	protected abstract String parseInternal(Object obj, String con) throws ServiceException ;
}
