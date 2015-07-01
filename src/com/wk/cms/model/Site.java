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
@JsonIgnoreProperties({"channels","documents"})
public class Site {

	@Id
	private String id;
	private String name;
	private String descr;
	private String url;
	
	@ManyToOne
	private User crUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	@OneToMany(mappedBy="site",cascade={CascadeType.ALL})
	private Set<Channel> channels;
	
	@OneToMany(mappedBy="site",cascade={CascadeType.ALL})
	private Set<Document> documents;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
