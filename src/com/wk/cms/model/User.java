package com.wk.cms.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wk.cms.service.IUserService;
import com.wk.cms.utils.BeanFactory;

@Entity
@Table(name="Sys_User",schema="CMS")
@Cacheable
@Cache(region="user",usage=CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"roles"})
public class User {

	@Id
	private String id;
	private String username;
	private String password;
	private String salt;
	private String truename;
	private String email;
	
	@ManyToOne
	private User crUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	@ManyToMany(mappedBy="users")
	private Set<Role> roles = new HashSet<Role>();
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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

	public static User findByUsername(String username) {
		
		IUserService userService = BeanFactory.getBean(IUserService.class);
		return userService.findByUserName(username);
	}
	
	
}
