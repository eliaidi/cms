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
	private Field field;
	@Column(length = 2000)
	private String value;
	
	@Column(name="t_group")
	private Integer group;
	
	@ManyToOne
	private ExtField extField;

	public FieldValue() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public ExtField getExtField() {
		return extField;
	}

	public void setExtField(ExtField extField) {
		this.extField = extField;
	}

}
