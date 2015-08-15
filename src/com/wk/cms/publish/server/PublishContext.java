package com.wk.cms.publish.server;

import com.wk.cms.parser.HtmlTag;

public class PublishContext {

	private Object base;
	private boolean isPreview;
	private HtmlTag tag;

	public PublishContext() {
	}

	public PublishContext(Object base2, boolean isPreivew, HtmlTag htmlTag) {

		this.base = base2;
		this.isPreview = isPreivew;
		this.tag = htmlTag;
	}

	public Object getBase() {
		return base;
	}

	public void setBase(Object base) {
		this.base = base;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public void setPreview(boolean isPreview) {
		this.isPreview = isPreview;
	}

	public HtmlTag getTag() {
		return tag;
	}

	public void setTag(HtmlTag tag) {
		this.tag = tag;
	}

}
