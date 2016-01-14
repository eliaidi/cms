package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

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

	@Autowired
	private IRoleDao roleDao;
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
			
			return r;
		}else{
			attachResource(role);
			role.setCrTime(new Date());
			
			//role.setCrUser(crUser);
			roleDao.save(role);
			
			return role; 
		}
		
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
	}
	@Override
	public List<Role> find(String[] roleIds) {
		return roleDao.find(roleIds);
	}
	@Override
	public List<String> findByUserName(String username) {
		
		return roleDao.findNameByUserName(username);
	}

}
