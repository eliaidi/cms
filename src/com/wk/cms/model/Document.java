package com.wk.cms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
	
	public static class Status{
		public static final Integer NEW = 1;
		public static final Integer EDIT = 2;
		public static final Integer AUDIT = 3;
		public static final Integer PUBLISH = 4;
	}

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
	private Integer sort = 0;
	private Integer status = Status.NEW;
	@OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
	private Set<Appendix> appendixs;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonSerialize(using = OneToManyFieldSerializer.class)
	@Fetch(FetchMode.SUBSELECT)
	// i use type<Set> before,but type<Set> performed badly in SpringMvc arguments resolver,so i changed to type<List>
	// in fact,type<Set>'s meaning is more closer to what i want to express, because type<List>'s elements may be repeatable ,but type<Set> will never be
	private List<FieldValue> fieldValues = new ArrayList<FieldValue>();

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

	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}

}
