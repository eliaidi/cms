package com.wk.cms.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.model.annotations.Show;
import com.wk.cms.model.annotations.ShowArea;
import com.wk.cms.mvc.json.OneToManyFieldSerializer;

@Entity
@JsonIgnoreProperties({ "appendixs" })
@Cacheable
@Cache(region = "document", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Document {

	@Id
	private String id;

	@NotEmpty(message = "{doc.title.null}")
	private String title;

	@Column(length = 1000)
	private String abst;

	@Lob
	@NotEmpty(message = "{doc.content.null}")
	@Show(ShowArea.Detail)
	private String content;
	private String author;
	private Integer sort;
	private Integer status;
	@OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
	private Set<Appendix> appendixs;
	
	@OneToMany(mappedBy="document" ,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JsonSerialize(using=OneToManyFieldSerializer.class)
	private Set<FieldValue> fieldValues;

	@Temporal(TemporalType.TIMESTAMP)
	private Date writeTime;

	@ManyToOne
	private Channel channel;

	@ManyToOne
	private Site site;

	@ManyToOne
	private User crUser;

	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;

	public Document() {

	}

	public Document(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Set<Appendix> getAppendixs() {
		return appendixs;
	}

	public void setAppendixs(Set<Appendix> appendixs) {
		this.appendixs = appendixs;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbst() {
		return abst;
	}

	public void setAbst(String abst) {
		this.abst = abst;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
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

	public Set<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Set<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}

}
