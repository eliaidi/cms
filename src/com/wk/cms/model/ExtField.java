package com.wk.cms.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.mvc.json.OneToManyFieldSerializer;

@Entity
public class ExtField {

	@Id
	private String id;
	@ManyToOne
	private Channel channel;

	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	@JsonSerialize(using=OneToManyFieldSerializer.class)
	private Field field;
	@ManyToOne
	private User crUser;
	private Date crTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
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
