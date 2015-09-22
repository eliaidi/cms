package com.wk.cms.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.mvc.json.FileJsonSerializer;

@Entity
public class Appendix {

	public static class Type {

		public static final Integer PIC = 1;
		public static final Integer FILE = 2;
		public static final Integer OTHER = 3;

		public static Integer valueOf(String type) {
			if (!StringUtils.hasLength(type))
				return null;
			return Integer.parseInt(type);
		}

	}

	@Id
	private String id;

	@ManyToOne
	private Document document;
	private int type;
	private String name;
	private String addition;

	@OneToOne
	@JsonSerialize(using=FileJsonSerializer.class)
	private File file;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
