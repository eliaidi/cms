package com.wk.cms.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IExtFieldDao;
import com.wk.cms.model.Document;
import com.wk.cms.model.ExtField;
import com.wk.cms.service.IExtFieldService;
import com.wk.cms.service.IFieldService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.service.exception.ValidationException;
import com.wk.cms.utils.PageInfo;

@Service
public class ExtFieldService implements IExtFieldService {

	@Autowired
	private IExtFieldDao extFieldDao;
	
	@Autowired
	private IFieldService fieldService;
	@Override
	public PageInfo find(String channelId,PageInfo pageInfo ,String query) {
		return extFieldDao.find(channelId,pageInfo,query);
	}
	@Override
	public ExtField save(ExtField extField) throws ServiceException {
		
		validateField(extField.getName());
		if(!StringUtils.hasLength(extField.getId())){
			
			if(find(extField.getName(), extField.getChannel().getId())!=null){
				throw new ServiceException("栏目【ID:"+extField.getChannel().getId()+"】下已经存在名称为【NAME:"+extField.getName()+"】的扩展字段！");
			}
//			extField.getField().setId(UUID.randomUUID().toString());
			extField.getField().setCrTime(new Date());
			extField.getField().setCrUser(null);
			extField.getField().setExtField(extField);
			extField.getField().setCustom(fieldService.isCustomField(extField.getField().getType()));
			
//			extField.setId(UUID.randomUUID().toString());
			extField.setCrTime(new Date());
			extField.setCrUser(null);
			extFieldDao.save(extField);
			return extField;
		}else{
			ExtField persistEf = findById(extField.getId());
			
			persistEf.getField().setName(extField.getField().getName());
			
			BeanUtils.copyProperties(extField, persistEf, new String[]{"id","field","channel","crTime","crUser"});
			extFieldDao.save(persistEf);
			return persistEf;
		}
	}
	private void validateField(String name) throws ValidationException {
		
		Field[] fields = Document.class.getDeclaredFields();
		for(Field f : fields){
			if(f.getName().equals(name)){
				throw new ValidationException("字段【"+name+"】是内置字段，不允许复用，请重新输入字段名！");
			}
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
	@Override
	public void delete(String[] ids) {
		
		for(String id : ids){
			extFieldDao.delete(id);
		}
	}

}
