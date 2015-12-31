package com.wk.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IResourceDao;
import com.wk.cms.model.Resource;
import com.wk.cms.service.IResourceService;
import com.wk.cms.utils.PageInfo;

@Service
public class ResourceService implements IResourceService {

	@Autowired
	private IResourceDao resourceDao;
	@Override
	public PageInfo list(PageInfo info, String query) {
		return resourceDao.find(info,query);
	}
	@Override
	public Resource save(Resource resource) {
		
		if(StringUtils.hasLength(resource.getId())){
			
			Resource r = findById(resource.getId());
			
			r.setName(resource.getName());
			r.setValue(resource.getValue());
			r.setType(resource.getType());
			
			resourceDao.save(r);
			return r;
		}else{
			
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
	}
	
	@Override
	public List<Resource> findByIds(String[] ids) {
		
		return resourceDao.find(ids);
	}

}
