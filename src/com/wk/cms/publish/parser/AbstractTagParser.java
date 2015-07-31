package com.wk.cms.publish.parser;

import org.jsoup.nodes.Element;

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
}
