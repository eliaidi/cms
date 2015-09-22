package com.wk.cms.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTag {

	private String name;
	private Map<String, String> attrs = new HashMap<String, String>();
	private String html;
	private int startpos;
	private int endpos;
	
	public HtmlTag(){}
	public HtmlTag(String tagName, String attrStr, String html,int sp,int ep) {
		
		this.name = tagName;
		this.html = html;
		this.startpos = sp;
		this.endpos = ep;
		parseAttr(attrStr);
	}
	public HtmlTag(String tagName, String attrStr, String html) {
		this.name = tagName;
		this.html = html;
		parseAttr(attrStr);
	}
	private void parseAttr(String attrStr) {
		
		Matcher m = Pattern.compile("([a-z0-9_]+)\\s*=\\s*['|\"]?([^'\"\\s]+)['|\"]?",Pattern.CASE_INSENSITIVE).matcher(attrStr);
		while(m.find()){
			attrs.put(m.group(1), m.group(2));
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}
	public String attr(String attrName) {
		return this.attrs.get(attrName);
	}
	public HtmlTag attr(String attrName, String attrVal) {

		this.attrs.put(attrName, attrVal);
		return this;
	}
	public HtmlTag removeAttr(String attrName) {
		this.attrs.remove(attrName);
		return this;
	}
	public String attrs() {
		StringBuilder sb = new StringBuilder();
		for(String key : this.attrs.keySet()){
			sb.append(" "+key+"='"+this.attrs.get(key)+"' ");
		}
		return sb.toString();
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public int getStartpos() {
		return startpos;
	}
	public void setStartpos(int startpos) {
		this.startpos = startpos;
	}
	public int getEndpos() {
		return endpos;
	}
	public void setEndpos(int endpos) {
		this.endpos = endpos;
	}

	
}
