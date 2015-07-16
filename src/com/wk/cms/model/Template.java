package com.wk.cms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import com.wk.cms.model.annotations.Show;
import com.wk.cms.model.annotations.ShowArea;

@Entity
public class Template {
	
	@Id
	private String id;
	
	@NotEmpty
	private String name;
	
	@Lob
	@NotEmpty
	@Show(ShowArea.Detail)
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	@ManyToOne
	private User crUser;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
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
