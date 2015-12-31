package com.wk.cms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="Sys_Role",schema="CMS")
@JsonIgnoreProperties({"users"})
public class Role {

	@Id
	private String id;
	private String name;
	
	@Type(type="yes_no")
	private boolean isAdmin;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	@ManyToOne
	private User crUser;
	
	@ManyToMany
	private Set<User> users = new HashSet<User>();
	
	@ManyToMany(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
	private List<Resource> resources = new ArrayList<Resource>();
	
	public List<Resource> getResources() {
		return resources;
	}
	
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	public Set<User> getUsers() {
		return users;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
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
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
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
