package com.wk.cms.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"children","documents"})
public class Channel {

	@Id
	private String id;
	private String name;
	private String descr;
	
	@ManyToOne
	private Site site;
	@ManyToOne
	private Channel parent;
	
	@OneToMany(mappedBy="parent",cascade={CascadeType.REMOVE})
	private Set<Channel> children;
	
	@OneToMany(mappedBy="channel",cascade={CascadeType.REMOVE})
	private Set<Document> documents;
	@ManyToOne
	private User crUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setChildren(Set<Channel> children) {
		this.children = children;
	}
	
	public Set<Channel> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Channel getParent() {
		return parent;
	}
	
	public void setParent(Channel parent) {
		this.parent = parent;
	}
	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getCrUser() {
		return crUser;
	}

	public void setCrUser(User crUser) {
		this.crUser = crUser;
	}

	public Date getCrTime() {
		return crTime;
	}

	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}
	
	
}
