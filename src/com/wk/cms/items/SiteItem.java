package com.wk.cms.items;

public class SiteItem {

	private String id;
	private String name;
	private String iconCls;
	private String module;
	
	public SiteItem(){
		
	}
	public SiteItem(String id,String name, String iconCls, String module) {
		this.id = id;
		this.name = name ;
		this.iconCls = iconCls;
		this.module = module;
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
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
	
}
