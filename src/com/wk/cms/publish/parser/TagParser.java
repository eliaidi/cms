package com.wk.cms.publish.parser;

import org.jsoup.nodes.Element;


public interface TagParser {

	void setElement(Element e);
	String parse(Object obj,String con);
}
