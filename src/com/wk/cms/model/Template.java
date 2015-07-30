package com.wk.cms.model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"tempFiles"})
public class Template {
	
	@Id
	private String id;
	
	@NotEmpty
	private String name;
	
	@OneToOne(cascade=CascadeType.ALL)
	private File file;
	
	@NotEmpty
	private String prefix = "index";
	
	@NotEmpty
	private String ext = "html";
	private String remoteUrl;
	
	@ManyToOne
	private Site site;
	
	@ManyToMany(mappedBy="templates",cascade={CascadeType.ALL})
	private Set<TempFile> tempFiles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Set<TempFile> getTempFiles() {
		return tempFiles;
	}

	public void setTempFiles(Set<TempFile> tempFiles) {
		this.tempFiles = tempFiles;
	}
	
	
}
