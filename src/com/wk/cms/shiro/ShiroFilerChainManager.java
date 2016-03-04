package com.wk.cms.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Resource;
import com.wk.cms.service.IResourceService;
import com.wk.cms.service.IRoleService;
import com.wk.cms.utils.CommonUtils;

@Service
public class ShiroFilerChainManager {
	@Autowired
	private DefaultFilterChainManager filterChainManager;
	private Map<String, NamedFilterList> defaultFilterChains;
	
	private IResourceService resourceService;
	private IRoleService roleService;
	
	@Autowired
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@PostConstruct
	public void init() {
		defaultFilterChains = new LinkedHashMap<String, NamedFilterList>(//需要排序，shiro按照顺序比较filterChain
				filterChainManager.getFilterChains());
	}

	public void initFilterChains(List<Resource> resources) {
		// 1、首先删除以前老的filter chain并注册默认的
		filterChainManager.getFilterChains().clear();
		//添加默认的filterChain
		if (defaultFilterChains != null) {
			filterChainManager.getFilterChains().putAll(defaultFilterChains);
		}
		
		String roleStr = roleService.findAdminRoles();
		
		// 2、循环URL Filter 注册filter chain
		for (Resource r : resources) {
			String url = r.getValue();
			String rStr = "";
			// 注册roles filter
			List<String> roles = resourceService.findRoleNames(r);
			if(!CommonUtils.isEmpty(roles)){
				rStr += CommonUtils.join(roles, ",");
			}
			if(StringUtils.hasLength(roleStr)){
				rStr += ","+roleStr;
			}
			rStr = rStr.startsWith(",")?rStr.substring(1):rStr;
			if(StringUtils.hasLength(rStr)){
				filterChainManager.addToChain(url, "roles", rStr);
			}
		}
		
		filterChainManager.addToChain("/**", "user","admin");
	}
}
