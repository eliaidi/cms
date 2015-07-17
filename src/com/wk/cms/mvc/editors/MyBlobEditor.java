package com.wk.cms.mvc.editors;

import java.beans.PropertyEditorSupport;
import java.io.UnsupportedEncodingException;

import com.wk.cms.utils.MyBlob;

public class MyBlobEditor extends PropertyEditorSupport {

	private String charsetName;

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public MyBlobEditor() {

	}

	public MyBlobEditor(String cname) {
		this.charsetName = cname;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		try {
			setValue(new MyBlob(text.getBytes(charsetName)));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("String转换成Blob失败！！", e);
		}
	}
}
