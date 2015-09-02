package com.wk.cms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.mvc.json.OneToManyFieldSerializer;

@Entity
@Table(name = "Sys_Field", schema = "Cms")
public class Field {

	public final class Type {
		public static final String INT = "int";
		public static final String STRING = "string";
		public static final String FLOAT = "float";
		public static final String DATE = "date";
		public static final String TEXT = "text";
	}

	@Id
	private String id;
	@NotEmpty
	@Length(min = 2, max = 10)
	private String name;
	private String type = Type.STRING;
	private Integer length;
	@OneToOne(mappedBy = "field")
	private ExtField extField;
	@ManyToOne
	private Site site;
	
	@ManyToOne
	private Field parent;
	
	@OneToMany(mappedBy="parent",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JsonSerialize(using=OneToManyFieldSerializer.class)
	private List<Field> children = new ArrayList<Field>();
	
	
	@ManyToOne
	private User crUser;
	
	@Temporal(TemporalType.DATE)
	private Date crTime;

	public Field(){}
	public Field(String id, String name2) {
		this.id = id;
		this.name = name2;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public ExtField getExtField() {
		return extField;
	}

	public void setExtField(ExtField extField) {
		this.extField = extField;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Field getParent() {
		return parent;
	}

	public void setParent(Field parent) {
		this.parent = parent;
	}

	public List<Field> getChildren() {
		return children;
	}

	public void setChildren(List<Field> children) {
		this.children = children;
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
