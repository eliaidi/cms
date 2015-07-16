package com.wk.cms.dao.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IFileDao;
import com.wk.cms.model.File;

@Repository
public class FileDao implements IFileDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public File findById(String id) {
		return hibernateTemplate.get(File.class, id);
	}
	@Override
	public void save(File f) {
		
		if(!StringUtils.hasLength(f.getId())){
			f.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(f);
		}else{
			hibernateTemplate.update(f);
		}
	}

}
