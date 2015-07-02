package com.wk.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IAppendixDao;
import com.wk.cms.model.Appendix;
import com.wk.cms.utils.PageInfo;

@Repository
public class AppendixDao implements IAppendixDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override
	public PageInfo find(String documentId, Integer type, PageInfo pageInfo) {
		
		List<Appendix > appendixs = (List<Appendix>) hibernateTemplate.find("select a from Appendix a where a.document.id=? and a.type=?", documentId,type);
		if(appendixs==null){
			appendixs = new ArrayList<Appendix>();
		}
		return new PageInfo(appendixs,(long) appendixs.size());
	}

	@Override
	public void save(Appendix appendix) {
		
		if(!StringUtils.hasLength(appendix.getId())){
			appendix.setId(UUID.randomUUID().toString());
			
			hibernateTemplate.save(appendix);
		}else{
			hibernateTemplate.update(appendix);
		}
		
	}

	@Override
	public Appendix findById(String id) {
		
		return hibernateTemplate.get(Appendix.class, id);
	}

	@Override
	public List<Appendix> findByDocId(String documentId) {
		
		List<Appendix> appendixs = (List<Appendix>) hibernateTemplate.find("select a from Appendix a where a.document.id=? order by a.type", documentId);
		return appendixs;
	}

}
