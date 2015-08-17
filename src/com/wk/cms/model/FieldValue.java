package com.wk.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FieldValue {

	@Id
	private String id;
	
	@ManyToOne
	private Document document;
	
	@ManyToOne
	private ExtField extField;
	
	@Column(length=2000)
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public ExtField getExtField() {
		return extField;
	}
	public void setExtField(ExtField extField) {
		this.extField = extField;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
}
