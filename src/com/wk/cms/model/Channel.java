package com.wk.cms.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.mvc.json.OneToManyFieldSerializer;

@Entity
@JsonIgnoreProperties({ "children", "documents" })
@Cacheable
@Cache(region = "channel", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Channel {

	@Id
	private String id;

	private String name;

	private String descr;
	private String folder;
	private String otempIds;
	private String dtempIds;
	private Integer sort;
	@ManyToOne
	private Site site;
	@ManyToOne
	private Channel parent;

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE })
	private Set<Channel> children;
	@OneToMany(mappedBy = "channel", cascade = { CascadeType.REMOVE })
	private Set<Document> documents;
	@OneToMany(mappedBy = "channel", cascade = { CascadeType.ALL },fetch=FetchType.EAGER)
	@JsonSerialize(using=OneToManyFieldSerializer.class)
	private Set<ExtField> extFields;

	@ManyToOne
	private User crUser;

	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;

	public Channel() {
		super();
	}

	public Channel(String id, String name, String descr, String folder,
			Integer sort, Site site, Channel parent, Set<Channel> children,
			Set<Document> documents, User crUser, Date crTime) {
		super();
		this.id = id;
		this.name = name;
		this.descr = descr;
		this.folder = folder;
		this.site = site;
		this.parent = parent;
		this.children = children;
		this.documents = documents;
		this.crUser = crUser;
		this.crTime = crTime;
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getOtempIds() {
		return otempIds;
	}

	public void setOtempIds(String otempIds) {
		this.otempIds = otempIds;
	}

	public String getDtempIds() {
		return dtempIds;
	}

	public void setDtempIds(String dtempIds) {
		this.dtempIds = dtempIds;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public Set<Channel> getChildren() {
		return children;
	}

	public Date getCrTime() {
		return crTime;
	}

	public User getCrUser() {
		return crUser;
	}

	public String getDescr() {
		return descr;
	}

	public String getFolder() {
		return folder;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Channel getParent() {
		return parent;
	}

	public Site getSite() {
		return site;
	}

	public void setChildren(Set<Channel> children) {
		this.children = children;
	}

	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}

	public void setCrUser(User crUser) {
		this.crUser = crUser;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(Channel parent) {
		this.parent = parent;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Set<ExtField> getExtFields() {
		return extFields;
	}

	public void setExtFields(Set<ExtField> extFields) {
		this.extFields = extFields;
	}

	
}
