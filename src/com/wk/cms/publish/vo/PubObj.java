package com.wk.cms.publish.vo;

public class PubObj {

	private Integer index;
	private Object obj;

	public PubObj() {
	}

	public PubObj(int i, Object doc) {
		this.index = i;
		this.obj = doc;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
