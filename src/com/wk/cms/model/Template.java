package com.wk.cms.model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;

@Entity
@JsonIgnoreProperties({"tempFiles"})
public class Template {
	
	public class Type {

		public static final int OUTLINE = 1;
		public static final int DETAIL = 2;
		public static final int INCLUDE = 3;

	}
	@NotEmpty
	private String ext = "html";
	
	@OneToOne(cascade=CascadeType.ALL)
	private File file;
	@Id
	private String id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String prefix = "index";
	
	private String remoteUrl;
	@ManyToOne
	private Site site;
	
	@ManyToMany(mappedBy="templates",cascade={CascadeType.ALL})
	private Set<TempFile> tempFiles;
	
	private int type;

	public Template(){}
	public Template(String id, String name, String prefix,int type,
			String fileExt, Site site) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.prefix = prefix;
		this.ext = fileExt;
		this.site = site;
	}

	public String getConInStr() throws ServiceException {
		try {
			return CommonUtils.getContent(this.file.getContent().getBinaryStream());
		} catch (Exception e) {
			throw new ServiceException("获取附件内容失败！",e);
		} 
	}

	public String getExt() {
		return ext;
	}

	public File getFile() {
		return file;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public Site getSite() {
		return site;
	}

	public Set<TempFile> getTempFiles() {
		return tempFiles;
	}

	public int getType() {
		return type;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setTempFiles(Set<TempFile> tempFiles) {
		this.tempFiles = tempFiles;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
