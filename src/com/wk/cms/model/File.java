package com.wk.cms.model;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.model.annotations.Show;
import com.wk.cms.model.annotations.ShowArea;
import com.wk.cms.mvc.json.BlobJsonSerializer;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.MyBlob;

@Entity
@Table(schema="CMS",name="Sys_File")
public class File {

	@Id
	private String id;
	private String fileName;
	private String fileExt;
	
	@Lob
	@Show(ShowArea.Detail)
	@JsonSerialize(using=BlobJsonSerializer.class)
	private Blob content;
	private long fileSize;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	@ManyToOne
	private User crUser;

	public File(){}
	public File(String fileName, long size, String fileExt, MyBlob content) {
		
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.fileSize = size;
		this.content = content;
		this.crTime = new Date();
		this.crUser = null;
	}

	public File(Blob content) {
		this.content = content;
	}
	public File(String id ,String remoteUrl) throws ServiceException   {
		
		byte[] bytes = CommonUtils.getBytesFromUrl(remoteUrl);
		this.fileName = remoteUrl.substring(remoteUrl.lastIndexOf("/")+1);
		this.fileExt = FileUtils.getFileExt(this.fileName);
		this.fileSize = bytes.length;
		this.content = new MyBlob(bytes);
		this.crTime = new Date();
		this.crUser = null;
		this.id = id;
	}
	public File(String id, java.io.File eFile) throws FileParseException {
		this.id = id;
		this.crTime = new Date();
		this.crUser = null;
		
		byte[] bytes = FileUtils.getBytes(eFile);
		this.fileName = eFile.getName();
		this.fileExt = FileUtils.getFileExt(this.fileName);
		this.fileSize = bytes.length;
		this.content = new MyBlob(bytes);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
