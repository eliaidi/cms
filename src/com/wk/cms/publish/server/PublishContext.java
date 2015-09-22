package com.wk.cms.publish.server;

import com.wk.cms.model.Template;
import com.wk.cms.parser.HtmlTag;

public class PublishContext {

	private HtmlTag tag;
	private Object obj;
	private Object base;
	private StringBuilder baseHtml;
	private PublishServer server;
	private Template template;

	public PublishContext() {
	}

	public PublishContext( HtmlTag htmlTag) {

		this.tag = htmlTag;
	}

	public PublishContext(Object obj,Object base,StringBuilder baseHtml, HtmlTag htmlTag, PublishServer publishServer, Template template) {
		this.tag = htmlTag;
		this.base = base;
		this.baseHtml = baseHtml;
		this.server = publishServer;
		this.template = template;
		this.obj = obj;
	}

	public HtmlTag getTag() {
		return tag;
	}

	public void setTag(HtmlTag tag) {
		this.tag = tag;
	}

	public PublishServer getServer() {
		return server;
	}

	public void setServer(PublishServer server) {
		this.server = server;
	}

	public Object getBase() {
		return base;
	}

	public void setBase(Object base) {
		this.base = base;
	}

	public StringBuilder getBaseHtml() {
		return baseHtml;
	}

	public void setBaseHtml(StringBuilder baseHtml) {
		this.baseHtml = baseHtml;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

}
