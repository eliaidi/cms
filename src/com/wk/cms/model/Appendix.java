package com.wk.cms.model;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"content"})
public class Appendix {

	@Id
	private String id;
	
	@ManyToOne
	private Document document;
	private int type;
	private String addition;
	private String fileName;
	private String fileExt;
	
	@Lob
	private Blob content;
	private long fileSize;
	
	@Temporal(TemporalType.DATE)
	private Date crTime;
	
	@ManyToOne
	private User crUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getAddition() {
		return addition;
	}
	
	public void setAddition(String addition) {
		this.addition = addition;
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

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public Date getCrTime() {
		return crTime;
	}

	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}

	public User getCrUser() {
		return crUser;
	}

	public void setCrUser(User crUser) {
		this.crUser = crUser;
	}
	
	
}
