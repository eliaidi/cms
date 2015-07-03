package com.wk.cms.utils.parser;

import com.wk.cms.exception.ParseException;


public abstract class RemoteDocParser {

	protected String url;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public RemoteDocParser(String url){
		this.url = url;
	}
	public abstract com.wk.cms.model.Document parse() throws ParseException;
	
}
