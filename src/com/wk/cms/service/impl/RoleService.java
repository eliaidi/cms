package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IRoleDao;
import com.wk.cms.model.Resource;
import com.wk.cms.model.Role;
import com.wk.cms.service.IResourceService;
import com.wk.cms.service.IRoleService;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Service
public class RoleService implements IRoleService {

	private static final String ROLE_CACHE_NAME = "roleCache";

	@Autowired
	private IRoleDao roleDao;
	
	private CacheManager cacheManager;
	
	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	@Override
	public PageInfo list(PageInfo info, String query,String userId) {
		return roleDao.find(info,query,userId);
	}
	@Override
	public Role save(Role role ) {
		
		if(StringUtils.hasLength(role.getId())){
			Role r = findById(role.getId());
			
			r.setName(role.getName());
			r.setIsAdmin(role.getIsAdmin());
			r.setResources(role.getResources());
			attachResource(r);
			roleDao.save(r);
			refreshCache();
			return r;
		}else{
			attachResource(role);
			role.setCrTime(new Date());
			
			//role.setCrUser(crUser);
			roleDao.save(role);
			
			return role; 
		}
		
	}
	private void refreshCache() {
		
		Cache<String, Object> cache = cacheManager.getCache(ROLE_CACHE_NAME);
		cache.clear();
	}
	private void attachResource(Role role) {
		List<Resource> resources = role.getResources();
		if(!CommonUtils.isEmpty(resources)){
			String[] ids = new String[resources.size()];
			for(int i=0;i<resources.size();i++){
				Resource r = resources.get(i);
				r.getRoles().add(role);
				ids[i] = r.getId();
			}
			
			IResourceService resourceService = BeanFactory.getBean(IResourceService.class);
			role.setResources(resourceService.findByIds(ids));
			
		}
	}
	@Override
	public Role findById(String id) {
		return roleDao.findById(id);
	}
	@Override
	public void delete(String[] ids) {
		roleDao.delete(ids);
		
		refreshCache();
	}
	@Override
	public List<Role> find(String[] roleIds) {
		return roleDao.find(roleIds);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findByUserName(String username) {
		
		Cache<String, Object> cache = cacheManager.getCache(ROLE_CACHE_NAME);
		Object c = cache.get(username);
		if(c!=null){
			return (List<String>) c;
		}
		List<String> rs = roleDao.findNameByUserName(username);
		cache.put(username, rs);
		return rs;
	}
	@Override
	public String findAdminRoles() {
		List<String> roles = roleDao.findAdmin();
		if(CommonUtils.isEmpty(roles)){
			return null;
		}
		return CommonUtils.list2String(roles, ",");
	}

}
