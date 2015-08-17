package com.wk.cms.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ExtField {

	public class Type {

		public static final int INT = 1;
		public static final int STRING = 2;
		public static final int FLOAT = 3;
		public static final int DATE = 4;
		public static final int TEXT = 5;

	}

	@Id
	private String id;
	@ManyToOne
	private Channel channel;
	private String label;
	private String name;
	private Integer type;
	private Integer length;
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

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
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
