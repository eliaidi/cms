package com.wk.cms.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.mvc.json.FileJsonSerializer;

@Entity
@Table(name="Sys_WallPaper",schema="cms")
public class WallPaper {

	@Id
	String id;
	@ManyToOne
	User user;
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
	@JsonSerialize(using=FileJsonSerializer.class)
	File file;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date useTime;
	@Temporal(TemporalType.TIMESTAMP)
	Date crTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public Date getCrTime() {
		return crTime;
	}
	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}
	
	
}
