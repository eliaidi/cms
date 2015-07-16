package com.wk.cms.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"channels","documents"})
@Cacheable
@Cache(region="site",usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Site {

	@Id
	private String id;
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String descr;
	
	private String url;
	private String canPubSta;
	@NotEmpty
	private String folder;
	
	@ManyToOne
	private User crUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	@OneToMany(mappedBy="site",cascade={CascadeType.ALL})
	private Set<Channel> channels;
	@OneToMany(mappedBy="site",cascade={CascadeType.ALL})
	private Set<Document> documents;
	
	public Site() {
		super();
	}
	
	public Site(String id, String name, String descr, String url, User crUser,
			Date crTime, Set<Channel> channels, Set<Document> documents) {
		super();
		this.id = id;
		this.name = name;
		this.descr = descr;
		this.url = url;
		this.crUser = crUser;
		this.crTime = crTime;
		this.channels = channels;
		this.documents = documents;
	}
	
	public String getCanPubSta() {
		return canPubSta;
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
	
	public String getUrl() {
		return url;
	}
	public void setCanPubSta(String canPubSta) {
		this.canPubSta = canPubSta;
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
	public void setUrl(String url) {
		this.url = url;
	}
	
}
