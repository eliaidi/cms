package com.wk.cms.mvc.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class MyArrayEditor extends PropertyEditorSupport {

	private String sp;
	
	public MyArrayEditor(){
		
	}
	public MyArrayEditor(String f) {
		this.sp = f;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		
		if(!StringUtils.hasLength(text)){
			setValue(null);
		}else{
			String[] arr = text.split(sp==null?",":sp);
			setValue(arr);
		}
	}
	
}
