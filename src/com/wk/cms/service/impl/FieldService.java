package com.wk.cms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IFieldDao;
import com.wk.cms.model.Field;
import com.wk.cms.model.Field.Type;
import com.wk.cms.service.IFieldService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.PageInfo;

@Service
public class FieldService implements IFieldService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FieldService.class);
	@Autowired
	private IFieldDao fieldDao;

	@Override
	public PageInfo find(String siteId, PageInfo pageInfo, String query) {
		return fieldDao.find(siteId, pageInfo, query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Field> findTypes(String siteId) throws ServiceException {

		List<Field> pFields = (List<Field>) find(siteId, new PageInfo(0, 100),
				null).getList();
//		List<Field> pFields = new ArrayList<Field>();

		java.lang.reflect.Field[] defFields = Type.class.getDeclaredFields();
		for (java.lang.reflect.Field f : defFields) {
			try {
				Object val = f.get(null);
				pFields.add(0, new Field(val.toString(), f.getName()));
			} catch (Exception e) {
//				LOGGER.error("查询字段类型出错！", e);
				//ignore
			}
		}

		return pFields;
	}

	@Override
	public Field save(Field field) {

		if (StringUtils.hasLength(field.getId())) {

			Field persistField = findById(field.getId());
			delete(persistField.getChildren());
			
			persistField.setName(field.getName());
			persistField.setChildren(field.getChildren());
			
			for(Field child : persistField.getChildren()){
				child.setId(UUID.randomUUID().toString());
				child.setParent(persistField);
				child.setSite(persistField.getSite());
			}
			fieldDao.save(persistField);
			
			return persistField;
		} else {
			field.setId(UUID.randomUUID().toString());
			field.setCrTime(new Date());
			field.setCrUser(null);
			for (Field child : field.getChildren()) {
				child.setId(UUID.randomUUID().toString());
				child.setCrTime(new Date());
				child.setCrUser(null);
				child.setParent(field);
				child.setSite(field.getSite());
			}
			fieldDao.save(field);
			return field;
		}
	}

	@Override
	public void delete(List<Field> children) {
		
		for(Field f : children){
			fieldDao.delete(f);
		}
	}

	@Override
	public Field findById(String id) {
		return fieldDao.findById(id);
	}

	@Override
	public void delete(String[] ids) {
		
		List<Field> fields = find(ids);
		
		for(Field f:fields){
			fieldDao.delete(f);
		}
	}

	@Override
	public List<Field> find(String[] ids) {
		return fieldDao.find(ids);
	}

	@Override
	public boolean isCustomField(String type) {
		
		java.lang.reflect.Field[] fields = Field.Type.class.getDeclaredFields();
		for(java.lang.reflect.Field field : fields){
			try {
				Object val = field.get(null);
				if(type.equals(val)){
					return false;
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			} 
		}
		return true;
	}

}
