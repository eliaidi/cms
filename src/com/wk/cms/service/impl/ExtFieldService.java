package com.wk.cms.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IExtFieldDao;
import com.wk.cms.model.ExtField;
import com.wk.cms.service.IExtFieldService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Service
public class ExtFieldService implements IExtFieldService {

	@Autowired
	private IExtFieldDao extFieldDao;
	@Override
	public PageInfo find(String channelId,PageInfo pageInfo ,String query) {
		return extFieldDao.find(channelId,pageInfo,query);
	}
	@Override
	public ExtField save(ExtField extField) throws ServiceException {
		
		if(!StringUtils.hasLength(extField.getId())){
			
			if(find(extField.getName(), extField.getChannel().getId())!=null){
				throw new ServiceException("栏目【ID:"+extField.getChannel().getId()+"】下已经存在名称为【NAME:"+extField.getName()+"】的扩展字段！");
			}
			
			extField.setId(UUID.randomUUID().toString());
			extField.setCrTime(new Date());
			extField.setCrUser(null);
			extFieldDao.save(extField);
			return extField;
		}else{
			ExtField persistEf = findById(extField.getId());
			
			BeanUtils.copyProperties(extField, persistEf, new String[]{"id","channel","crTime","crUser"});
			extFieldDao.save(persistEf);
			return persistEf;
		}
	}
	@Override
	public ExtField findById(String id) {
		return extFieldDao.findById(id);
	}
	@Override
	public ExtField findByName(String name) {
		return extFieldDao.findByName(name);
	}
	@Override
	public ExtField find(String name, String channelId) {
		return extFieldDao.find(name, channelId);
	}

}
