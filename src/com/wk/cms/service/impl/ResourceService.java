package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.wk.cms.dao.IResourceDao;
import com.wk.cms.model.Resource;
import com.wk.cms.model.Role;
import com.wk.cms.service.IResourceService;
import com.wk.cms.shiro.ShiroFilerChainManager;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.PageInfo;

@Service
public class ResourceService implements IResourceService {

	private static final Logger log = LoggerFactory.getLogger(ResourceService.class);
	@Autowired
	private ShiroFilerChainManager shiroFilerChainManager;
	
	@Autowired
	private IResourceDao resourceDao;
	
	@PostConstruct  
	@Override
    public void initFilterChain() {  
        shiroFilerChainManager.initFilterChains(findAllWithRoles()); 
        
       // log.debug("\n\n\n\n\n\n\nhandlerMapping.getHandlerMethods():::"+handlerMapping.getHandlerMethods()+"\n\n\n\n\n");
    }  

	@Override
	public List<Resource> findAllWithRoles() {
		return resourceDao.findAllWithRoles();
	}

	@Override
	public PageInfo list(PageInfo info, String query) {
		return resourceDao.find(info, query);
	}

	@Override
	public Resource save(Resource resource) {

		if (StringUtils.hasLength(resource.getId())) {

			Resource r = findById(resource.getId());

			r.setName(resource.getName());
			r.setValue(resource.getValue());
			r.setType(resource.getType());

			resourceDao.save(r);
			return r;
		} else {

			resource.setCrTime(new Date());
			resource.setCrUser(null);

			resourceDao.save(resource);
		}
		return null;
	}

	@Override
	public Resource findById(String id) {
		return resourceDao.findById(id);
	}

	@Override
	public void delete(String[] ids) {
		resourceDao.delete(ids);
		
		initFilterChain();
	}

	@Override
	public List<Resource> findByIds(String[] ids) {

		return resourceDao.find(ids);
	}

	@Override
	public List<Role> findRoles(Resource r) {
		return resourceDao.findRoles(r);
	}

	@Override
	public List<String> findRoleNames(Resource r) {
		return resourceDao.findRoleNames(r);
	}

}
