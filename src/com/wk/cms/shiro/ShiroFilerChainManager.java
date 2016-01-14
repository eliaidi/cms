package com.wk.cms.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wk.cms.model.Resource;
import com.wk.cms.service.IResourceService;
import com.wk.cms.utils.CommonUtils;

@Service
public class ShiroFilerChainManager {
	@Autowired
	private DefaultFilterChainManager filterChainManager;
	private Map<String, NamedFilterList> defaultFilterChains;
	
	private IResourceService resourceService;
	
	@Autowired
	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@PostConstruct
	public void init() {
		defaultFilterChains = new HashMap<String, NamedFilterList>(
				filterChainManager.getFilterChains());
	}

	public void initFilterChains(List<Resource> resources) {
		// 1、首先删除以前老的filter chain并注册默认的
		filterChainManager.getFilterChains().clear();
		if (defaultFilterChains != null) {
			filterChainManager.getFilterChains().putAll(defaultFilterChains);
		}
		
		// 2、循环URL Filter 注册filter chain
		for (Resource r : resources) {
			String url = r.getValue();
			// 注册roles filter
			List<String> roles = resourceService.findRoleNames(r);
			if(!CommonUtils.isEmpty(roles)){
				filterChainManager.addToChain(url, "roles", CommonUtils.join(roles, ","));
			}
		}
	}
}
